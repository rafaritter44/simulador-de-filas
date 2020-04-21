package com.github.rafaritter44.simulador.aleatorio;

public class GeradorDeAleatorios {
	
	private final MetodoGerador metodoGerador;
	
	public GeradorDeAleatorios(final MetodoGerador metodoGerador) {
		this.metodoGerador = metodoGerador;
	}
	
	public double proximo(double minimo, double maximo) {
		return (maximo - minimo) * metodoGerador.proximo() + minimo;
	}
	
}
