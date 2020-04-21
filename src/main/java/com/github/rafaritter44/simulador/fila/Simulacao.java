package com.github.rafaritter44.simulador.fila;

public class Simulacao {
	
	private int vezes;
	private double tempoChegadaInicial;
	private int maximoEventosAgendados;
	
	public int getVezes() {
		return vezes;
	}
	
	public void setVezes(final int vezes) {
		this.vezes = vezes;
	}
	
	public double getTempoChegadaInicial() {
		return tempoChegadaInicial;
	}
	
	public void setTempoChegadaInicial(final double tempoChegadaInicial) {
		this.tempoChegadaInicial = tempoChegadaInicial;
	}
	
	public int getMaximoEventosAgendados() {
		return maximoEventosAgendados;
	}
	
	public void setMaximoEventosAgendados(final int maximoEventosAgendados) {
		this.maximoEventosAgendados = maximoEventosAgendados;
	}
	
}
