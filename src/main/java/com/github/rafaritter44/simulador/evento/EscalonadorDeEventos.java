package com.github.rafaritter44.simulador.evento;

import static java.util.Comparator.comparing;

import java.util.Optional;
import java.util.PriorityQueue;

public class EscalonadorDeEventos {
	
	private final PriorityQueue<Evento> eventos = new PriorityQueue<>(comparing(Evento::getTempo));
	
	public Optional<Evento> proximo() {
		return Optional.ofNullable(eventos.poll());
	}
	
	public void add(final Evento e) {
		eventos.add(e);
	}
	
}
