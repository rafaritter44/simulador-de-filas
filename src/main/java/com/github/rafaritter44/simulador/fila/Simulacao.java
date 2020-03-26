package com.github.rafaritter44.simulador.fila;

public class Simulacao {
	
	private final long tempoMinimoChegada, tempoMaximoChegada;
	private final long tempoMinimoSaida, tempoMaximoSaida;
	private final double tempoChegadaInicial;
	private final int eventos;
	
	@SuppressWarnings("unused")
	private Simulacao() {
		this(0L, 0L, 0L, 0L, 0D, 0);
	}
	
	public Simulacao(final long tempoMinimoChegada, final long tempoMaximoChegada,
			final long tempoMinimoSaida, final long tempoMaximoSaida,
			final double tempoChegadaInicial, final int eventos) {
		this.tempoMinimoChegada = tempoMinimoChegada;
		this.tempoMaximoChegada = tempoMaximoChegada;
		this.tempoMinimoSaida = tempoMinimoSaida;
		this.tempoMaximoSaida = tempoMaximoSaida;
		this.tempoChegadaInicial = tempoChegadaInicial;
		this.eventos = eventos;
	}

	public long getTempoMinimoChegada() {
		return tempoMinimoChegada;
	}

	public long getTempoMaximoChegada() {
		return tempoMaximoChegada;
	}

	public long getTempoMinimoSaida() {
		return tempoMinimoSaida;
	}

	public long getTempoMaximoSaida() {
		return tempoMaximoSaida;
	}

	public double getTempoChegadaInicial() {
		return tempoChegadaInicial;
	}

	public int getEventos() {
		return eventos;
	}
	
}
