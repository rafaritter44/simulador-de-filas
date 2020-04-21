package com.github.rafaritter44.simulador;

import java.util.Map;
import java.util.function.Supplier;

import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.aleatorio.MetodoCongruenteLinear;
import com.github.rafaritter44.simulador.evento.EscalonadorDeEventos;
import com.github.rafaritter44.simulador.fila.Fila;

public class Contexto {
	
	private static class Holder {
		private static final Contexto CONTEXTO = new Contexto();
	}
	
	private EscalonadorDeEventos escalonador;
	private GeradorDeAleatorios geradorDeAleatorios;
	private Supplier<GeradorDeAleatorios> geradorDeAleatoriosFactory;
	private Map<Integer, Fila> filas;
	private double tempo;
	private int eventosAgendados;
	
	private Contexto() {
		geradorDeAleatoriosFactory = () -> new GeradorDeAleatorios(new MetodoCongruenteLinear());
	}
	
	public static Contexto get() {
		return Holder.CONTEXTO;
	}
	
	public void limpar() {
		escalonador = new EscalonadorDeEventos();
		geradorDeAleatorios = geradorDeAleatoriosFactory == null ? null : geradorDeAleatoriosFactory.get();
		if (filas != null) {
			filas.values().parallelStream().forEach(Fila::limpar);
		}
		tempo = 0D;
		eventosAgendados = 0;
	}

	public EscalonadorDeEventos getEscalonador() {
		return escalonador;
	}

	public void setEscalonador(final EscalonadorDeEventos escalonador) {
		this.escalonador = escalonador;
	}

	public GeradorDeAleatorios getGeradorDeAleatorios() {
		return geradorDeAleatorios;
	}

	public void setGeradorDeAleatorios(final GeradorDeAleatorios geradorDeAleatorios) {
		this.geradorDeAleatorios = geradorDeAleatorios;
	}
	
	public Supplier<GeradorDeAleatorios> getGeradorDeAleatoriosFactory() {
		return geradorDeAleatoriosFactory;
	}

	public void setGeradorDeAleatoriosFactory(final Supplier<GeradorDeAleatorios> geradorDeAleatoriosFactory) {
		this.geradorDeAleatoriosFactory = geradorDeAleatoriosFactory;
	}

	public Map<Integer, Fila> getFilas() {
		return filas;
	}

	public void setFilas(final Map<Integer, Fila> filas) {
		this.filas = filas;
	}

	public double getTempo() {
		return tempo;
	}

	public void setTempo(final double tempo) {
		this.tempo = tempo;
	}

	public int getEventosAgendados() {
		return eventosAgendados;
	}

	public void setEventosAgendados(final int eventosAgendados) {
		this.eventosAgendados = eventosAgendados;
	}
	
}
