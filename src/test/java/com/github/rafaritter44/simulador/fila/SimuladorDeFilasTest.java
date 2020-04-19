package com.github.rafaritter44.simulador.fila;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;

public class SimuladorDeFilasTest {
	
	@Test
	public void test() {
		final GeradorDeAleatorios geradorDeAleatorios = mock(GeradorDeAleatorios.class);
		final Queue<Double> pseudoAleatorios = new LinkedList<>();
		pseudoAleatorios.add(3.9828);
		pseudoAleatorios.add(1.8851);
		pseudoAleatorios.add(1.1643);
		pseudoAleatorios.add(1.5542);
		pseudoAleatorios.add(5.0439);
		pseudoAleatorios.add(1.7221);
		pseudoAleatorios.add(1.9881);
		when(geradorDeAleatorios.proximo(anyLong(), anyLong())).then(invocation -> pseudoAleatorios.poll());
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
		final SimuladorDeFilas simulador = new SimuladorDeFilas(fila, simulacao, () -> geradorDeAleatorios);
		final HashMap<Integer, Double> resultado = simulador.simular(1);
		assertAll(
				() -> assertEquals(2D, resultado.get(0)),
				() -> assertEquals(1.8851, resultado.get(1)),
				() -> assertEquals(1.7851, resultado.get(2), 0.0001),
				() -> assertEquals(2.6555, resultado.get(3), 0.0001)
		);
	}
	
}
