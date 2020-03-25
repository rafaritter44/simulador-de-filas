package com.github.rafaritter44.simulador.aleatorio;

public class GeradorDeAleatorios {
	
	private final MetodoGerador metodoGerador;
	private double anterior;
	
	public GeradorDeAleatorios() {
		this(System.currentTimeMillis());
	}
	
	public GeradorDeAleatorios(final double semente) {
		metodoGerador = new MetodoCongruenteLinear(25214903917L, 9L, (long) Math.pow(2D, 48D));
		anterior = semente;
	}
	
	public double proximo(final long minimo, final long maximo) {
		anterior = metodoGerador.proximo(anterior);
		return minimo + (anterior % (maximo - minimo + 1L));
	}
	
}
