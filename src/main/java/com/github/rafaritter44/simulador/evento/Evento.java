package com.github.rafaritter44.simulador.evento;

public class Evento {
	
	private final TipoDeEvento tipo;
	private final double tempo;
	
	public Evento(final TipoDeEvento tipo, final double tempo) {
		this.tipo = tipo;
		this.tempo = tempo;
	}
	
	public TipoDeEvento getTipo() {
		return tipo;
	}
	
	public double getTempo() {
		return tempo;
	}
	
}
