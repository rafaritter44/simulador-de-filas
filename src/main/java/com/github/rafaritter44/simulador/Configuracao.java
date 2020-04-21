package com.github.rafaritter44.simulador;

import java.util.Map;

import com.github.rafaritter44.simulador.fila.Fila;
import com.github.rafaritter44.simulador.fila.Simulacao;

public class Configuracao {
	
	private Map<Integer, Fila> filas;
	private Simulacao simulacao;
	
	public Map<Integer, Fila> getFilas() {
		return filas;
	}
	
	public void setFilas(final Map<Integer, Fila> filas) {
		this.filas = filas;
	}
	
	public Simulacao getSimulacao() {
		return simulacao;
	}
	
	public void setSimulacao(final Simulacao simulacao) {
		this.simulacao = simulacao;
	}
	
}
