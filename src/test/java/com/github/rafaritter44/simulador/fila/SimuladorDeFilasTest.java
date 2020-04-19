package com.github.rafaritter44.simulador.fila;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.aleatorio.MetodoGerador;

public class SimuladorDeFilasTest {
	
	@Test
	public void testaFilaSimples() {
		final MetodoGerador metodoGerador = mock(MetodoGerador.class);
		final Queue<Double> pseudoAleatorios = new LinkedList<>();
		pseudoAleatorios.add(0.3276);
		pseudoAleatorios.add(0.8851);
		pseudoAleatorios.add(0.1643);
		pseudoAleatorios.add(0.5542);
		pseudoAleatorios.add(0.6813);
		pseudoAleatorios.add(0.7221);
		pseudoAleatorios.add(0.9881);
		when(metodoGerador.proximo()).then(invocation -> pseudoAleatorios.poll());
		final Supplier<GeradorDeAleatorios> geradorDeAleatoriosFactory = () -> new GeradorDeAleatorios(metodoGerador);
		final Fila fila = new Fila();
		fila.setServidores(1);
		fila.setCapacidade(3);
		final Simulacao simulacao = new Simulacao();
		simulacao.setTempoMinimoChegada(1L);
		simulacao.setTempoMaximoChegada(2L);
		simulacao.setTempoMinimoSaida(3L);
		simulacao.setTempoMaximoSaida(6L);
		simulacao.setTempoChegadaInicial(2D);
		simulacao.setMaximoEventosAgendados(pseudoAleatorios.size());
		final SimuladorDeFilas simulador = new SimuladorDeFilas(fila, simulacao, geradorDeAleatoriosFactory);
		final HashMap<Integer, Double> resultado = simulador.simular(1);
		assertAll(
				() -> assertEquals(2D, resultado.get(0)),
				() -> assertEquals(1.8851, resultado.get(1)),
				() -> assertEquals(1.7851, resultado.get(2), 0.0001),
				() -> assertEquals(2.6555, resultado.get(3), 0.0001)
		);
	}
	
}
