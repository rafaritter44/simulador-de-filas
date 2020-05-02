package com.github.rafaritter44.simulador.fila;

import java.util.HashMap;
import java.util.Map;

public class Resultado {
	
	private Map<Integer, Double> tempoPorClientes;
	private int perdas;
	
	public Resultado() {
		tempoPorClientes = new HashMap<>();
		perdas = 0;
	}
	
	public void perda() {
		perdas += 1;
	}
	
	public Map<Integer, Double> getTempoPorClientes() {
		return tempoPorClientes;
	}

	public void setTempoPorClientes(final Map<Integer, Double> tempoPorClientes) {
		this.tempoPorClientes = tempoPorClientes;
	}
	
	public int getPerdas() {
		return perdas;
	}
	
	public void setPerdas(final int perdas) {
		this.perdas = perdas;
	}
	
}
