package com.github.rafaritter44.simulador.fila;

public class Fila {
	
	private final int servidores;
	private final int capacidade;
	private transient int clientes;
	
	public Fila() {
		this(0, 0);
	}
	
	public Fila(final int servidores, final int capacidade) {
		this.servidores = servidores;
		this.capacidade = capacidade;
		this.clientes = 0;
	}
	
	public void chegada() {
		clientes += 1;
	}
	
	public void saida() {
		clientes -= 1;
	}
	
	public int getServidores() {
		return servidores;
	}
	
	public int getCapacidade() {
		return capacidade;
	}
	
	public int getClientes() {
		return clientes;
	}
	
}
