package com.github.rafaritter44.simulador.fila;

import static com.github.rafaritter44.simulador.evento.TipoDeEvento.CHEGADA;
import static com.github.rafaritter44.simulador.evento.TipoDeEvento.SAIDA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.aleatorio.MetodoCongruenteLinear;
import com.github.rafaritter44.simulador.evento.EscalonadorDeEventos;
import com.github.rafaritter44.simulador.evento.Evento;

public class SimuladorDeFilas {
	
	private final Fila fila;
	private final Simulacao simulacao;
	private final Supplier<GeradorDeAleatorios> geradorDeAleatoriosFactory;
	private EscalonadorDeEventos escalonador;
	private GeradorDeAleatorios geradorDeAleatorios;
	private HashMap<Integer, Double> tempoPorClientes;
	private double tempo;
	
	public SimuladorDeFilas(final Fila fila, final Simulacao simulacao) {
		this(fila, simulacao, MetodoCongruenteLinear::new);
	}
	
	public SimuladorDeFilas(final Fila fila, final Simulacao simulacao,
			final Supplier<GeradorDeAleatorios> geradorDeAleatoriosFactory) {
		this.fila = fila;
		this.simulacao = simulacao;
		this.geradorDeAleatoriosFactory = geradorDeAleatoriosFactory;
	}
	
	public HashMap<Integer, Double> simular(final int vezes) {
		final ArrayList<HashMap<Integer, Double>> resultados = new ArrayList<>();
		for (int simulacao = 0; simulacao < vezes; simulacao++) {
			resultados.add(simular());
		}
		final int maximoDeClientesNaFila = resultados
				.stream()
				.map(HashMap::keySet)
				.flatMap(Set::stream)
				.max(Comparator.naturalOrder())
				.orElse(0);
		final HashMap<Integer, Double> mediaDosResultados = new HashMap<>();
		for (int clientesNaFila = 0; clientesNaFila <= maximoDeClientesNaFila; clientesNaFila++) {
			double tempo = 0D;
			for (final HashMap<Integer, Double> resultado : resultados) {
				tempo += Optional.ofNullable(resultado.get(clientesNaFila)).orElse(0D);
			}
			mediaDosResultados.put(clientesNaFila, tempo / vezes);
		}
		return mediaDosResultados;
	}
	
	private HashMap<Integer, Double> simular() {
		this.fila.limpar();
		this.escalonador = new EscalonadorDeEventos();
		this.geradorDeAleatorios = geradorDeAleatoriosFactory.get();
		this.tempoPorClientes = new HashMap<>();
		this.tempo = 0D;
		chegada(new Evento(CHEGADA, simulacao.getTempoChegadaInicial()));
		for (int i = 0; i < simulacao.getEventos(); i++) {
			final Evento e = escalonador.proximo();
			switch (e.getTipo()) {
			case CHEGADA:
				chegada(e);
				break;
			case SAIDA:
				saida(e);
				break;
			}
		}
		return tempoPorClientes;
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
		if (fila.getClientes() >= fila.getServidores()) {
			agendaSaida();
		}
	}
	
	private void agendaChegada() {
		escalonador.add(new Evento(CHEGADA, tempo + geradorDeAleatorios.proximo(
				simulacao.getTempoMinimoChegada(), simulacao.getTempoMaximoChegada())));
	}
	
	private void agendaSaida() {
		escalonador.add(new Evento(SAIDA, tempo + geradorDeAleatorios.proximo(
				simulacao.getTempoMinimoSaida(), simulacao.getTempoMaximoSaida())));
	}
	
	private void contabilizaTempo(final Evento e) {
		final double tempoJaContabilizado = Optional
				.ofNullable(tempoPorClientes.get(fila.getClientes()))
				.orElse(0D);
		tempoPorClientes.put(fila.getClientes(), e.getTempo() - tempo + tempoJaContabilizado);
		tempo = e.getTempo();
	}
	
}
