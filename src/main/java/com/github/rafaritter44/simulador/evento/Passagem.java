package com.github.rafaritter44.simulador.evento;

import java.util.List;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.fila.Fila;
import com.github.rafaritter44.simulador.fila.Roteamento;

public class Passagem extends Evento {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	private final Fila origem;
	private final Fila destino;
	
	public Passagem(final Fila origem, final Fila destino) {
		this.origem = origem;
		this.destino = destino;
		final GeradorDeAleatorios geradorDeAleatorios = CONTEXTO.getGeradorDeAleatorios();
		super.setTempo(geradorDeAleatorios.proximo(origem.getTempoMinimoSaida(), origem.getTempoMaximoSaida()));
	}

	@Override
	public void executar() {
		super.contabilizarTempo();
		origem.saida();
		if (origem.getClientes() >= origem.getServidores()) {
			final List<Roteamento> roteamentos = origem.getRoteamentos();
			if (roteamentos.get(0).getProbabilidade() == 1D) {
				new Passagem(origem, destino).agendar();
			} else {
				final double aleatorio = CONTEXTO.getGeradorDeAleatorios().proximo();
				double probabilidadeAcumulada = 0D;
				boolean passagemAgendada = false;
				for (final Roteamento roteamento : roteamentos) {
					probabilidadeAcumulada += roteamento.getProbabilidade();
					if (aleatorio < probabilidadeAcumulada) {
						final Fila proximoDestino = CONTEXTO.getFilas().get(roteamento.getDestino());
						new Passagem(origem, proximoDestino).agendar();
						passagemAgendada = true;
						break;
					}
				}
				if (!passagemAgendada) {
					new Saida(origem).agendar();
				}
			}
		}
		if (destino.isCapacidadeInfinita() || destino.getClientes() < destino.getCapacidade()) {
			destino.chegada();
			if (destino.getClientes() <= destino.getServidores()) {
				final List<Roteamento> roteamentosDestino = destino.getRoteamentos();
				if (roteamentosDestino.isEmpty()) {
					new Saida(destino).agendar();
				} else if (roteamentosDestino.get(0).getProbabilidade() == 1D) {
					final Fila proximoDestino = CONTEXTO.getFilas().get(roteamentosDestino.get(0).getDestino());
					new Passagem(destino, proximoDestino).agendar();
				} else {
					final double aleatorio = CONTEXTO.getGeradorDeAleatorios().proximo();
					double probabilidadeAcumulada = 0D;
					boolean passagemAgendada = false;
					for (final Roteamento roteamento : roteamentosDestino) {
						probabilidadeAcumulada += roteamento.getProbabilidade();
						if (aleatorio < probabilidadeAcumulada) {
							final Fila proximoDestino = CONTEXTO.getFilas().get(roteamento.getDestino());
							new Passagem(destino, proximoDestino).agendar();
							passagemAgendada = true;
							break;
						}
					}
					if (!passagemAgendada) {
						new Saida(destino).agendar();
					}
				}
			}
		} else {
			destino.perda();
		}
	}
	
	@Override
	public String toString() {
		return super.toString() + ";origem=" + origem.getId() + ";destino=" + destino.getId();
	}
	
}
