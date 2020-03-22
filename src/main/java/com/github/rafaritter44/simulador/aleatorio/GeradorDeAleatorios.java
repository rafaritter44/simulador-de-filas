package com.github.rafaritter44.simulador.aleatorio;

public class GeradorDeAleatorios {
	
	private final MetodoGerador metodoGerador;
	private double anterior;
	
	public GeradorDeAleatorios() {
		this(System.currentTimeMillis());
	}
	
	public GeradorDeAleatorios(final double semente) {
		metodoGerador = new MetodoCongruenteLinear(4L, 9L, 4L);
		anterior = semente;
	}
	
	public double proximo(final long minimo, final long maximo) {
		anterior = metodoGerador.proximo(anterior);
		return minimo + (anterior % (maximo - minimo + 1L));
	}
	
}
