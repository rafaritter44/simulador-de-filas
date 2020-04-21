package com.github.rafaritter44.simulador.fila;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.aleatorio.MetodoGerador;

public class SimuladorDeFilasTest {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	@BeforeEach
	public void setUp() {
		CONTEXTO.limpar();
	}
	
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
		final GeradorDeAleatorios geradorDeAleatorios = new GeradorDeAleatorios(metodoGerador);
		CONTEXTO.setGeradorDeAleatoriosFactory(() -> geradorDeAleatorios);
		final Fila fila = new Fila();
		final int id = 0;
		fila.setId(id);
		fila.setParaId(-1);
		fila.setServidores(1);
		fila.setCapacidade(3);
		fila.setTempoMinimoChegada(1L);
		fila.setTempoMaximoChegada(2L);
		fila.setTempoMinimoSaida(3L);
		fila.setTempoMaximoSaida(6L);
		CONTEXTO.setFilas(Collections.singletonMap(id, fila));
		final Simulacao simulacao = new Simulacao();
		simulacao.setVezes(1);
		simulacao.setTempoChegadaInicial(2D);
		simulacao.setMaximoEventosAgendados(pseudoAleatorios.size());
		final SimuladorDeFilas simulador = new SimuladorDeFilas();
		final Map<Integer, Double> resultado = simulador.simular(simulacao).get(id);
		assertAll(
				() -> assertEquals(2D, resultado.get(0)),
				() -> assertEquals(1.8851, resultado.get(1)),
				() -> assertEquals(1.7851, resultado.get(2), 0.0001),
				() -> assertEquals(2.6555, resultado.get(3), 0.0001)
		);
	}
	
}
