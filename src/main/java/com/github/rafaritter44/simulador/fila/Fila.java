package com.github.rafaritter44.simulador.fila;

public class Fila {
	
	private int servidores;
	private int capacidade;
	private transient int clientes;
	
	public Fila() {
		clientes = 0;
	}
	
	public void chegada() {
		clientes += 1;
	}
	
	public void saida() {
		clientes -= 1;
	}
	
	public void limpar() {
		clientes = 0;
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

	public int getClientes() {
		return clientes;
	}

	public void setClientes(final int clientes) {
		this.clientes = clientes;
	}
	
}
