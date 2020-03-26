package com.github.rafaritter44.simulador.aleatorio;

public class GeradorDeAleatorios {
	
	private final long a;
	private final long M;
	private final long c;
	private double anterior;
	
	public GeradorDeAleatorios() {
		this(System.currentTimeMillis());
	}
	
	public GeradorDeAleatorios(final double semente) {
		this.a = 25214903917L;
		this.M = (long) Math.pow(2D, 48D);
		this.c = 11L;
		anterior = semente;
	}
	
	public double proximo(final long minimo, final long maximo) {
		anterior = (a * anterior + c) % M;
		return (maximo - minimo) * (anterior / M) + minimo;
	}
	
}
