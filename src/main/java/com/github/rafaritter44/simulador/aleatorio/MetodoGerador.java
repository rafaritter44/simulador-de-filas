package com.github.rafaritter44.simulador.aleatorio;

public abstract class MetodoGerador {
	
	protected double anterior;
	
	public MetodoGerador() {
		this(System.currentTimeMillis());
	}
	
	public MetodoGerador(final double semente) {
		anterior = semente;
	}
	
	public abstract double proximo();
	
}
