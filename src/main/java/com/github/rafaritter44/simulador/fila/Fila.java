package com.github.rafaritter44.simulador.fila;

import java.util.HashMap;
import java.util.Map;

public class Fila {
	
	private int id;
	private int paraId;
	private int servidores;
	private int capacidade;
	private double tempoMinimoChegada, tempoMaximoChegada;
	private double tempoMinimoSaida, tempoMaximoSaida;
	private transient int clientes;
	private transient Map<Integer, Double> tempoPorClientes;
	
	public Fila() {
		clientes = 0;
		tempoPorClientes = new HashMap<>();
	}
	
	public void chegada() {
		clientes += 1;
	}
	
	public void saida() {
		clientes -= 1;
	}
	
	public void limpar() {
		clientes = 0;
		tempoPorClientes = new HashMap<>();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(final int id) {
		this.id = id;
	}
	
	public int getParaId() {
		return paraId;
	}
	
	public void setParaId(final int paraId) {
		this.paraId = paraId;
	}

	public int getServidores() {
		return servidores;
	}

	public void setServidores(final int servidores) {
		this.servidores = servidores;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(final int capacidade) {
		this.capacidade = capacidade;
	}

	public double getTempoMinimoChegada() {
		return tempoMinimoChegada;
	}

	public void setTempoMinimoChegada(final double tempoMinimoChegada) {
		this.tempoMinimoChegada = tempoMinimoChegada;
	}

	public double getTempoMaximoChegada() {
		return tempoMaximoChegada;
	}

	public void setTempoMaximoChegada(final double tempoMaximoChegada) {
		this.tempoMaximoChegada = tempoMaximoChegada;
	}

	public double getTempoMinimoSaida() {
		return tempoMinimoSaida;
	}

	public void setTempoMinimoSaida(final double tempoMinimoSaida) {
		this.tempoMinimoSaida = tempoMinimoSaida;
	}

	public double getTempoMaximoSaida() {
		return tempoMaximoSaida;
	}

	public void setTempoMaximoSaida(final double tempoMaximoSaida) {
		this.tempoMaximoSaida = tempoMaximoSaida;
	}

	public int getClientes() {
		return clientes;
	}

	public void setClientes(final int clientes) {
		this.clientes = clientes;
	}
	
	public Map<Integer, Double> getTempoPorClientes() {
		return tempoPorClientes;
	}

	public void setTempoPorClientes(final Map<Integer, Double> tempoPorClientes) {
		this.tempoPorClientes = tempoPorClientes;
	}
	
}
