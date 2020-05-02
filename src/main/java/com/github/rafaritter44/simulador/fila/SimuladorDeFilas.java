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
	
	/**
	 * Algoritmo que executa uma simulação {@link Simulacao#getExecucoes()} vezes, e que depois faz a média dos resultados de
	 * cada execução -- isto é, tanto do {@link Resultado#getTempoPorClientes()} quanto das {@link Resultado#getPerdas()}.
	 * 
	 * @param simulacao Configuração da simulação a ser executada
	 * @return O objeto {@link Resultado} correspondente a cada {@link Fila}, definida pelo seu {@link Fila#getId()}
	 */
	public Map<Integer, Resultado> simular(final Simulacao simulacao) {
		final int execucoes = simulacao.getExecucoes();
		final int maximoAleatoriosConsumidos = simulacao.getMaximoAleatoriosConsumidos();
		final List<Map<Integer, Resultado>> resultadosPorExecucao = new ArrayList<>();
		for (int execucao = 0; execucao < execucoes; execucao++) {
			simular(maximoAleatoriosConsumidos);
			final Map<Integer, Resultado> resultadosDaExecucao = CONTEXTO
					.getFilas()
					.values()
					.parallelStream()
					.collect(Collectors.toMap(Fila::getId, Fila::getResultado));
			resultadosPorExecucao.add(resultadosDaExecucao);
		}
		final Map<Integer, Integer> maximoDeClientesPorFila = resultadosPorExecucao
				.parallelStream()
				.map(Map::entrySet)
				.flatMap(Set::parallelStream)
				.collect(Collectors.toMap(Entry::getKey, fila -> fila
						.getValue()
						.getTempoPorClientes()
						.keySet()
						.parallelStream()
						.max(Comparator.naturalOrder())
						.orElse(0), Integer::max));
		final Map<Integer, Resultado> mediaDosResultados = new HashMap<>();
		for (final int fila : maximoDeClientesPorFila.keySet()) {
			mediaDosResultados.put(fila, new Resultado());
			final int maximoDeClientesNaFila = maximoDeClientesPorFila.get(fila);
			for (int clientesNaFila = 0; clientesNaFila <= maximoDeClientesNaFila; clientesNaFila++) {
				mediaDosResultados.get(fila).getTempoPorClientes().put(clientesNaFila, 0D);
			}
			final List<Resultado> resultadosDaFila = resultadosPorExecucao
					.parallelStream()
					.map(resultado -> resultado.get(fila))
					.collect(Collectors.toList());
			int perdas = 0;
			for (final Resultado resultadoDaFila : resultadosDaFila) {
				perdas += resultadoDaFila.getPerdas();
				for (int clientesNaFila = 0; clientesNaFila <= maximoDeClientesNaFila; clientesNaFila++) {
					final double value = Optional.ofNullable(resultadoDaFila.getTempoPorClientes().get(clientesNaFila)).orElse(0D);
					mediaDosResultados.get(fila).getTempoPorClientes().merge(clientesNaFila, value, Double::sum);
				}
			}
			mediaDosResultados.get(fila).setPerdas(perdas / execucoes);
			for (int clientesNaFila = 0; clientesNaFila <= maximoDeClientesNaFila; clientesNaFila++) {
				mediaDosResultados
						.get(fila)
						.getTempoPorClientes()
						.merge(clientesNaFila, (double) execucoes, (tempo, numeroDeExecucoes) -> tempo / numeroDeExecucoes);
			}
		}
		return mediaDosResultados;
	}
	
	/**
	 * <p>Algoritmo que executa uma única simulação.</p>
	 * 
	 * <p>Primeiramente, as chegadas ({@link Chegada}) iniciais são agendadas, para as filas que recebem chegadas da rua
	 * ({@link Fila#isComChegadasDaRua()}), com base no método {@link Fila#getTempoChegadaInicial()}. Em seguida, são executados
	 * ({@link com.github.rafaritter44.simulador.evento.Evento#executar()}) os eventos
	 * ({@link com.github.rafaritter44.simulador.evento.Evento}) priorizados pelo {@link EscalonadorDeEventos} até que o número
	 * máximo de aleatórios (definido pela {@link com.github.rafaritter44.simulador.Configuracao} da {@link Simulacao}) seja
	 * consumido.</p>
	 * 
	 * @param maximoAleatoriosConsumidos limite de aleatórios a serem consumidos pela simulação, definindo o fim da simulação
	 */
	private void simular(final int maximoAleatoriosConsumidos) {
		CONTEXTO.limpar();
		CONTEXTO
				.getFilas()
				.values()
				.stream()
				.filter(Fila::isComChegadasDaRua)
				.map(fila -> new Chegada(fila, fila.getTempoChegadaInicial()))
				.forEach(Chegada::agendar);
		final EscalonadorDeEventos escalonador = CONTEXTO.getEscalonador();
		while (CONTEXTO.getAleatoriosConsumidos() < maximoAleatoriosConsumidos) {
			escalonador.proximo().executar();
		}
	}
	
}
