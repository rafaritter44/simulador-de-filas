package com.github.rafaritter44.simulador;

import com.github.rafaritter44.simulador.fila.Fila;
import com.github.rafaritter44.simulador.fila.Simulacao;

public class Configuracao {
	
	private Fila fila;
	private Simulacao simulacao;
	private int vezes;
	
	public Fila getFila() {
		return fila;
	}
	
	public void setFila(final Fila fila) {
		this.fila = fila;
	}
	
	public Simulacao getSimulacao() {
		return simulacao;
	}
	
	public void setSimulacao(final Simulacao simulacao) {
		this.simulacao = simulacao;
	}
	
	public int getVezes() {
		return vezes;
	}
	
	public void setVezes(final int vezes) {
		this.vezes = vezes;
	}
	
}
