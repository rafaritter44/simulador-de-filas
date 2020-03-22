package com.github.rafaritter44.simulador.aleatorio;

public class MetodoCongruenteLinear implements MetodoGerador {
	
	private final long a;
	private final long M;
	private final long c;
	
	public MetodoCongruenteLinear(final long a, final long M, final long c) {
		this.a = a;
		this.M = M;
		this.c = c;
	}
	
	public double proximo(final double anterior) {
		return ((a * anterior + c) % M) / M;
	}

}
