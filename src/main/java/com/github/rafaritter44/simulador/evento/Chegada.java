package com.github.rafaritter44.simulador.evento;

import java.util.List;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.fila.Fila;
import com.github.rafaritter44.simulador.fila.Roteamento;

public class Chegada extends Evento {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	private final Fila fila;
	
	public Chegada(final Fila fila) {
		this.fila = fila;
		final GeradorDeAleatorios geradorDeAleatorios = CONTEXTO.getGeradorDeAleatorios();
		super.setTempo(geradorDeAleatorios.proximo(fila.getTempoMinimoChegada(), fila.getTempoMaximoChegada()));
	}
	
	public Chegada(final Fila fila, final double tempo) {
		this.fila = fila;
		super.setTempo(tempo);
	}

	@Override
	public void executar() {
		super.contabilizarTempo();
		if (fila.isCapacidadeInfinita() || fila.getClientes() < fila.getCapacidade()) {
			fila.chegada();
			if (fila.getClientes() <= fila.getServidores()) {
				final List<Roteamento> roteamentos = fila.getRoteamentos();
				if (roteamentos.isEmpty()) {
					new Saida(fila).agendar();
				} else if (roteamentos.get(0).getProbabilidade() == 1D) {
					final Fila destino = CONTEXTO.getFilas().get(roteamentos.get(0).getDestino());
					new Passagem(fila, destino).agendar();
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
		} else {
			fila.perda();
		}
		new Chegada(fila).agendar();
	}
	
	@Override
	public String toString() {
		return super.toString() + ";fila=" + fila.getId();
	}
	
}
