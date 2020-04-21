package com.github.rafaritter44.simulador.evento;

import java.util.Map;
import java.util.Optional;

import com.github.rafaritter44.simulador.Contexto;

public abstract class Evento {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	private double tempo;
	
	public abstract void executar();
	
	public void agendar() {
		CONTEXTO.setEventosAgendados(CONTEXTO.getEventosAgendados() + 1);
		CONTEXTO.getEscalonador().add(this);
	}
	
	public double getTempo() {
		return tempo;
	}
	
	protected void setTempo(final double tempo) {
		this.tempo = tempo + CONTEXTO.getTempo();
	}
	
	protected void contabilizarTempo() {
		final double tempoGlobal = CONTEXTO.getTempo();
		CONTEXTO
				.getFilas()
				.values()
				.parallelStream()
				.forEach(fila -> {
					final Map<Integer, Double> tempoPorClientes = fila.getTempoPorClientes();
					final double tempoJaContabilizado = Optional
							.ofNullable(tempoPorClientes.get(fila.getClientes()))
							.orElse(0D);
					tempoPorClientes.put(fila.getClientes(), this.tempo - tempoGlobal + tempoJaContabilizado);
				});
		CONTEXTO.setTempo(this.tempo);
	}
	
}
