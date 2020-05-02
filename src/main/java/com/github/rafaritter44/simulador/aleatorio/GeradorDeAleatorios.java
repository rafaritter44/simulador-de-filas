package com.github.rafaritter44.simulador.aleatorio;

import com.github.rafaritter44.simulador.Contexto;

public class GeradorDeAleatorios {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	private final MetodoGerador metodoGerador;
	
	public GeradorDeAleatorios(final MetodoGerador metodoGerador) {
		this.metodoGerador = metodoGerador;
	}
	
	public double proximo() {
		CONTEXTO.consumirAleatorio();
		return metodoGerador.proximo();
	}
	
	public double proximo(double minimo, double maximo) {
		return (maximo - minimo) * proximo() + minimo;
	}
	
}
