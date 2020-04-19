package com.github.rafaritter44.simulador.aleatorio;

public class MetodoCongruenteLinear extends MetodoGerador {
	
	private final long a;
	private final long M;
	private final long c;
	
	public MetodoCongruenteLinear() {
		this(25214903917L, (long) Math.pow(2D, 48D), 11L);
	}
	
	public MetodoCongruenteLinear(final long a, final long M, final long c) {
		this.a = a;
		this.M = M;
		this.c = c;
	}
	
	@Override
	public double proximo() {
		super.anterior = (a * super.anterior + c) % M;
		return super.anterior / M;
	}
	
}
