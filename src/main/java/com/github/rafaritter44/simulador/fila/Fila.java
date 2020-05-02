package com.github.rafaritter44.simulador.fila;

import java.util.Collections;
import java.util.List;

public class Fila {
	
	private int id;
	private List<Roteamento> roteamentos;
	private int servidores;
	private int capacidade;
	private boolean capacidadeInfinita;
	private boolean comChegadasDaRua;
	private double tempoChegadaInicial;
	private double tempoMinimoChegada, tempoMaximoChegada;
	private double tempoMinimoSaida, tempoMaximoSaida;
	private transient int clientes;
	private transient Resultado resultado;
	
	public Fila() {
		roteamentos = Collections.emptyList();
		clientes = 0;
		resultado = new Resultado();
	}
	
	public void chegada() {
		clientes += 1;
	}
	
	public void saida() {
		clientes -= 1;
	}
	
	public void perda() {
		resultado.perda();
	}
	
	public void limpar() {
		clientes = 0;
		resultado = new Resultado();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(final int id) {
		this.id = id;
	}
	
	public List<Roteamento> getRoteamentos() {
		return roteamentos;
	}
	
	public void setRoteamentos(final List<Roteamento> roteamentos) {
		this.roteamentos = roteamentos;
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
	
	public boolean isCapacidadeInfinita() {
		return capacidadeInfinita;
	}
	
	public void setCapacidadeInfinita(final boolean capacidadeInfinita) {
		this.capacidadeInfinita = capacidadeInfinita;
	}
	
	public boolean isComChegadasDaRua() {
		return comChegadasDaRua;
	}
	
	public void setComChegadasDaRua(final boolean comChegadasDaRua) {
		this.comChegadasDaRua = comChegadasDaRua;
	}
	
	public double getTempoChegadaInicial() {
		return tempoChegadaInicial;
	}
	
	public void setTempoChegadaInicial(final double tempoChegadaInicial) {
		this.tempoChegadaInicial = tempoChegadaInicial;
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
	
	public Resultado getResultado() {
		return resultado;
	}
	
	public void setResultado(final Resultado resultado) {
		this.resultado = resultado;
	}
	
}
