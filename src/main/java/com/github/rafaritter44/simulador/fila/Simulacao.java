package com.github.rafaritter44.simulador.fila;

public class Simulacao {
	
	private long tempoMinimoChegada, tempoMaximoChegada;
	private long tempoMinimoSaida, tempoMaximoSaida;
	private double tempoChegadaInicial;
	private int maximoEventosAgendados;
	
	public long getTempoMinimoChegada() {
		return tempoMinimoChegada;
	}
	
	public void setTempoMinimoChegada(final long tempoMinimoChegada) {
		this.tempoMinimoChegada = tempoMinimoChegada;
	}
	
	public long getTempoMaximoChegada() {
		return tempoMaximoChegada;
	}
	
	public void setTempoMaximoChegada(final long tempoMaximoChegada) {
		this.tempoMaximoChegada = tempoMaximoChegada;
	}
	
	public long getTempoMinimoSaida() {
		return tempoMinimoSaida;
	}
	
	public void setTempoMinimoSaida(final long tempoMinimoSaida) {
		this.tempoMinimoSaida = tempoMinimoSaida;
	}
	
	public long getTempoMaximoSaida() {
		return tempoMaximoSaida;
	}
	
	public void setTempoMaximoSaida(final long tempoMaximoSaida) {
		this.tempoMaximoSaida = tempoMaximoSaida;
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
