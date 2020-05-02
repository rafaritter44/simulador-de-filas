package com.github.rafaritter44.simulador.evento;

import java.util.List;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.fila.Fila;
import com.github.rafaritter44.simulador.fila.Roteamento;

public class Saida extends Evento {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	private final Fila fila;
	
	public Saida(final Fila fila) {
		this.fila = fila;
		final GeradorDeAleatorios geradorDeAleatorios = Contexto.get().getGeradorDeAleatorios();
		super.setTempo(geradorDeAleatorios.proximo(fila.getTempoMinimoSaida(), fila.getTempoMaximoSaida()));
	}

	@Override
	public void executar() {
		super.contabilizarTempo();
		fila.saida();
		if (fila.getClientes() >= fila.getServidores()) {
			final List<Roteamento> roteamentos = fila.getRoteamentos();
			if (roteamentos.isEmpty()) {
				new Saida(fila).agendar();
			} else {
				final double aleatorio = CONTEXTO.getGeradorDeAleatorios().proximo();
				double probabilidadeAcumulada = 0D;
				boolean passagemAgendada = false;
				for (final Roteamento roteamento : roteamentos) {
					probabilidadeAcumulada += roteamento.getProbabilidade();
					if (aleatorio < probabilidadeAcumulada) {
						final Fila destino = CONTEXTO.getFilas().get(roteamento.getDestino());
						new Passagem(fila, destino).agendar();
						passagemAgendada = true;
						break;
					}
				}
				if (!passagemAgendada) {
					new Saida(fila).agendar();
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return super.toString() + ";fila=" + fila.getId();
	}

}
