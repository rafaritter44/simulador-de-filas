package com.github.rafaritter44.simulador.fila;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.evento.Chegada;
import com.github.rafaritter44.simulador.evento.EscalonadorDeEventos;

public class SimuladorDeFilas {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	public Map<Integer, Map<Integer, Double>> simular(final Simulacao simulacao) {
		final int vezes = simulacao.getVezes();
		final List<Map<Integer, Map<Integer, Double>>> resultados = new ArrayList<>();
		for (int numeroDaSimulacao = 0; numeroDaSimulacao < vezes; numeroDaSimulacao++) {
			simularUmaVez(simulacao);
			final Map<Integer, Map<Integer, Double>> resultado = CONTEXTO
					.getFilas()
					.values()
					.parallelStream()
					.collect(Collectors.toMap(Fila::getId, Fila::getTempoPorClientes));
			resultados.add(resultado);
		}
		final Map<Integer, Integer> maximoDeClientesPorFila = resultados
				.parallelStream()
				.map(Map::entrySet)
				.flatMap(Set::parallelStream)
				.collect(Collectors.toMap(Entry::getKey, fila -> fila
						.getValue()
						.keySet()
						.parallelStream()
						.max(Comparator.naturalOrder())
						.orElse(0), Integer::max));
		final Map<Integer, Map<Integer, Double>> mediaDosResultados = new HashMap<>();
		for (final int fila : maximoDeClientesPorFila.keySet()) {
			mediaDosResultados.put(fila, new HashMap<>());
			final int maximoDeClientesNaFila = maximoDeClientesPorFila.get(fila);
			for (int clientesNaFila = 0; clientesNaFila <= maximoDeClientesNaFila; clientesNaFila++) {
				double tempo = 0D;
				final List<Map<Integer, Double>> resultadosPorFila = resultados
						.stream()
						.map(resultado -> resultado.get(fila))
						.collect(Collectors.toList());
				for (final Map<Integer, Double> tempoPorClientes : resultadosPorFila) {
					tempo += Optional.ofNullable(tempoPorClientes.get(clientesNaFila)).orElse(0D);
				}
				mediaDosResultados.get(fila).put(clientesNaFila, tempo / vezes);
			}
		}
		return mediaDosResultados;
	}
	
	private void simularUmaVez(final Simulacao simulacao) {
		CONTEXTO.limpar();
		final Fila primeiraFila = CONTEXTO
				.getFilas()
				.entrySet()
				.parallelStream()
				.sorted(Entry.comparingByKey())
				.findFirst()
				.map(Entry::getValue)
				.orElseThrow(() -> new IllegalStateException("Nenhuma fila no contexto"));
		new Chegada(primeiraFila, simulacao.getTempoChegadaInicial()).executar();
		final int maximoEventosAgendados = simulacao.getMaximoEventosAgendados();
		final EscalonadorDeEventos escalonador = CONTEXTO.getEscalonador();
		while (CONTEXTO.getEventosAgendados() < maximoEventosAgendados) {
			escalonador.proximo().executar();
		}
	}
	
}
