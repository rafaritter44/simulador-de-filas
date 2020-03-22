package com.github.rafaritter44.simulador.fila;

import static com.github.rafaritter44.simulador.evento.TipoDeEvento.CHEGADA;
import static com.github.rafaritter44.simulador.evento.TipoDeEvento.SAIDA;

import java.util.HashMap;
import java.util.Optional;

import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.evento.EscalonadorDeEventos;
import com.github.rafaritter44.simulador.evento.Evento;

public class SimuladorDeFilas {
	
	private final Fila fila;
	private final long tempoMinimoChegada, tempoMaximoChegada;
	private final long tempoMinimoSaida, tempoMaximoSaida;
	private final EscalonadorDeEventos escalonador;
	private final GeradorDeAleatorios aleatorio;
	private final HashMap<Integer, Double> tempoPorClientes;
	private double tempo;
	
	public SimuladorDeFilas(final Fila fila,
			final long tempoMinimoChegada, final long tempoMaximoChegada,
			final long tempoMinimoSaida, final long tempoMaximoSaida) {
		this.fila = fila;
		this.tempoMinimoChegada = tempoMinimoChegada;
		this.tempoMaximoChegada = tempoMaximoChegada;
		this.tempoMinimoSaida = tempoMinimoSaida;
		this.tempoMaximoSaida = tempoMaximoSaida;
		this.escalonador = new EscalonadorDeEventos();
		this.aleatorio = new GeradorDeAleatorios();
		this.tempoPorClientes = new HashMap<>();
		this.tempo = 0D;
	}
	
	public static void main(String[] args) throws InterruptedException {
		final SimuladorDeFilas simulador = new SimuladorDeFilas(new Fila(1, 3), 1L, 2L, 3L, 6L);
		simulador.chegada(new Evento(CHEGADA, 2D));
		Optional<Evento> evento;
		while ((evento = simulador.escalonador.proximo()).isPresent()) {
			System.out.println(simulador.tempoPorClientes);
			Thread.sleep(1000L);
			switch (evento.get().getTipo()) {
			case CHEGADA:
				simulador.chegada(evento.get());
				break;
			case SAIDA:
				simulador.saida(evento.get());
				break;
			}
		}
	}
	
	private void chegada(final Evento chegada) {
		contabilizaTempo(chegada);
		if (fila.getClientes() < fila.getCapacidade()) {
			fila.chegada();
			if (fila.getClientes() <= fila.getServidores()) {
				agendaSaida();
			}
		}
		agendaChegada();
	}
	
	private void saida(final Evento saida) {
		contabilizaTempo(saida);
		fila.saida();
		if (fila.getClientes() >= 1) {
			agendaSaida();
		}
	}
	
	private void agendaChegada() {
		escalonador.add(new Evento(CHEGADA, tempo + aleatorio.proximo(tempoMinimoChegada, tempoMaximoChegada)));
	}
	
	private void agendaSaida() {
		escalonador.add(new Evento(SAIDA, tempo + aleatorio.proximo(tempoMinimoSaida, tempoMaximoSaida)));
	}
	
	private void contabilizaTempo(final Evento e) {
		final double tempoJaContabilizado = Optional
				.ofNullable(tempoPorClientes.get(fila.getClientes()))
				.orElse(0D);
		tempoPorClientes.put(fila.getClientes(), e.getTempo() - tempo + tempoJaContabilizado);
		tempo = e.getTempo();
	}
	
}
