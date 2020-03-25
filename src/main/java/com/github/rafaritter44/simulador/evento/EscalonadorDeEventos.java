package com.github.rafaritter44.simulador.evento;

import static java.util.Comparator.comparing;

import java.util.PriorityQueue;

public class EscalonadorDeEventos {
	
	private final PriorityQueue<Evento> eventos = new PriorityQueue<>(comparing(Evento::getTempo));
	
	public Evento proximo() {
		return eventos.poll();
	}
	
	public void add(final Evento e) {
		eventos.add(e);
	}
	
}
