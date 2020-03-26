package com.github.rafaritter44.simulador;

import com.github.rafaritter44.simulador.fila.Fila;
import com.github.rafaritter44.simulador.fila.Simulacao;

public class Configuracao {
	
	private final Fila fila;
	private final Simulacao simulacao;
	private final int vezes;
	
	@SuppressWarnings("unused")
	private Configuracao() {
		this(null, null, 0);
	}
	
	public Configuracao(final Fila fila, final Simulacao simulacao, final int vezes) {
		this.fila = fila;
		this.simulacao = simulacao;
		this.vezes = vezes;
	}
	
	public Fila getFila() {
		return fila;
	}
	
	public Simulacao getSimulacao() {
		return simulacao;
	}
	
	public int getVezes() {
		return vezes;
	}
	
}
