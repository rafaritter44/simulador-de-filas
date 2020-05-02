package com.github.rafaritter44.simulador.fila;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
	public void testarFilaSimples() {
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
		final int id = 1;
		fila.setId(id);
		fila.setServidores(1);
		fila.setCapacidade(3);
		fila.setComChegadasDaRua(true);
		fila.setTempoChegadaInicial(2D);
		fila.setTempoMinimoChegada(1L);
		fila.setTempoMaximoChegada(2L);
		fila.setTempoMinimoSaida(3L);
		fila.setTempoMaximoSaida(6L);
		CONTEXTO.setFilas(Collections.singletonMap(id, fila));
		final Simulacao simulacao = new Simulacao();
		simulacao.setExecucoes(1);
		simulacao.setMaximoAleatoriosConsumidos(pseudoAleatorios.size());
		final SimuladorDeFilas simulador = new SimuladorDeFilas();
		final Map<Integer, Double> resultado = simulador.simular(simulacao).get(id).getTempoPorClientes();
		assertAll(
				() -> assertEquals(2D, resultado.get(0), 0.0001),
				() -> assertEquals(1.8851, resultado.get(1), 0.0001),
				() -> assertEquals(1.7851, resultado.get(2), 0.0001),
				() -> assertEquals(2.6555, resultado.get(3), 0.0001)
		);
	}
	
	@Test
	public void testarFilasEmTandem() {
		final MetodoGerador metodoGerador = mock(MetodoGerador.class);
		final Queue<Double> pseudoAleatorios = new LinkedList<>();
		pseudoAleatorios.add(0.9921);
		pseudoAleatorios.add(0.0004);
		pseudoAleatorios.add(0.5534);
		pseudoAleatorios.add(0.2761);
		pseudoAleatorios.add(0.3398);
		pseudoAleatorios.add(0.8963);
		pseudoAleatorios.add(0.9023);
		pseudoAleatorios.add(0.0132);
		pseudoAleatorios.add(0.4569);
		pseudoAleatorios.add(0.5121);
		pseudoAleatorios.add(0.9208);
		pseudoAleatorios.add(0.0171);
		pseudoAleatorios.add(0.2299);
		pseudoAleatorios.add(0.8545);
		pseudoAleatorios.add(0.6001);
		pseudoAleatorios.add(0.2921);
		when(metodoGerador.proximo()).then(invocation -> pseudoAleatorios.poll());
		final GeradorDeAleatorios geradorDeAleatorios = new GeradorDeAleatorios(metodoGerador);
		CONTEXTO.setGeradorDeAleatoriosFactory(() -> geradorDeAleatorios);
		final Fila fila1 = new Fila();
		final int id1 = 1;
		final int id2 = 2;
		fila1.setId(id1);
		final Roteamento roteamento12 = new Roteamento();
		roteamento12.setDestino(id2);
		roteamento12.setProbabilidade(1D);
		fila1.setRoteamentos(Arrays.asList(roteamento12));
		fila1.setServidores(2);
		fila1.setCapacidade(3);
		fila1.setComChegadasDaRua(true);
		fila1.setTempoChegadaInicial(2.5);
		fila1.setTempoMinimoChegada(2L);
		fila1.setTempoMaximoChegada(3L);
		fila1.setTempoMinimoSaida(2L);
		fila1.setTempoMaximoSaida(5L);
		final Fila fila2 = new Fila();
		fila2.setId(id2);
		fila2.setServidores(1);
		fila2.setCapacidade(3);
		fila2.setTempoMinimoSaida(3L);
		fila2.setTempoMaximoSaida(5L);
		final Map<Integer, Fila> filas = new HashMap<>();
		filas.put(id1, fila1);
		filas.put(id2, fila2);
		CONTEXTO.setFilas(filas);
		final Simulacao simulacao = new Simulacao();
		simulacao.setExecucoes(1);
		simulacao.setMaximoAleatoriosConsumidos(pseudoAleatorios.size());
		final SimuladorDeFilas simulador = new SimuladorDeFilas();
		final Map<Integer, Resultado> resultados = simulador.simular(simulacao);
		assertAll(
				() -> assertEquals(2.6648, resultados.get(id1).getTempoPorClientes().get(0), 0.0001),
				() -> assertEquals(7.7764, resultados.get(id1).getTempoPorClientes().get(1), 0.0001),
				() -> assertEquals(6.2075, resultados.get(id1).getTempoPorClientes().get(2), 0.0001),
				() -> assertEquals(0.6998, resultados.get(id1).getTempoPorClientes().get(3), 0.0001),
				() -> assertEquals(0, resultados.get(id1).getPerdas()),
				() -> assertEquals(7.4763, resultados.get(id2).getTempoPorClientes().get(0), 0.0001),
				() -> assertEquals(0.6843, resultados.get(id2).getTempoPorClientes().get(1), 0.0001),
				() -> assertEquals(7.6925, resultados.get(id2).getTempoPorClientes().get(2), 0.0001),
				() -> assertEquals(1.4954, resultados.get(id2).getTempoPorClientes().get(3), 0.0001),
				() -> assertEquals(1, resultados.get(id2).getPerdas())
		);
	}
	
	@Test
	public void testarFilaComProbabilidadesDeRoteamento() {
		final MetodoGerador metodoGerador = mock(MetodoGerador.class);
		final Queue<Double> pseudoAleatorios = new LinkedList<>();
		pseudoAleatorios.add(0.2176);
		pseudoAleatorios.add(0.0103);
		pseudoAleatorios.add(0.1109);
		pseudoAleatorios.add(0.3456);
		pseudoAleatorios.add(0.9910);
		pseudoAleatorios.add(0.2323);
		pseudoAleatorios.add(0.9211);
		pseudoAleatorios.add(0.0322);
		pseudoAleatorios.add(0.1211);
		pseudoAleatorios.add(0.5131);
		pseudoAleatorios.add(0.7208);
		pseudoAleatorios.add(0.9172);
		pseudoAleatorios.add(0.9922);
		pseudoAleatorios.add(0.8324);
		pseudoAleatorios.add(0.5011);
		pseudoAleatorios.add(0.2931);
		when(metodoGerador.proximo()).then(invocation -> pseudoAleatorios.poll());
		final GeradorDeAleatorios geradorDeAleatorios = new GeradorDeAleatorios(metodoGerador);
		CONTEXTO.setGeradorDeAleatoriosFactory(() -> geradorDeAleatorios);
		final Fila fila1 = new Fila();
		final int id1 = 1;
		final int id2 = 2;
		fila1.setId(id1);
		final Roteamento roteamento12 = new Roteamento();
		roteamento12.setDestino(id2);
		roteamento12.setProbabilidade(0.7);
		fila1.setRoteamentos(Arrays.asList(roteamento12));
		fila1.setServidores(2);
		fila1.setCapacidade(4);
		fila1.setComChegadasDaRua(true);
		fila1.setTempoChegadaInicial(3D);
		fila1.setTempoMinimoChegada(2L);
		fila1.setTempoMaximoChegada(3L);
		fila1.setTempoMinimoSaida(4L);
		fila1.setTempoMaximoSaida(7L);
		final Fila fila2 = new Fila();
		fila2.setId(id2);
		fila2.setServidores(1);
		fila2.setCapacidadeInfinita(true);
		fila2.setTempoMinimoSaida(4L);
		fila2.setTempoMaximoSaida(8L);
		final Map<Integer, Fila> filas = new HashMap<>();
		filas.put(id1, fila1);
		filas.put(id2, fila2);
		CONTEXTO.setFilas(filas);
		final Simulacao simulacao = new Simulacao();
		simulacao.setExecucoes(1);
		simulacao.setMaximoAleatoriosConsumidos(pseudoAleatorios.size());
		final SimuladorDeFilas simulador = new SimuladorDeFilas();
		final Map<Integer, Resultado> resultados = simulador.simular(simulacao);
		assertAll(
				() -> assertEquals(3D, resultados.get(id1).getTempoPorClientes().get(0), 0.0001),
				() -> assertEquals(2.9164, resultados.get(id1).getTempoPorClientes().get(1), 0.0001),
				() -> assertEquals(4.8105, resultados.get(id1).getTempoPorClientes().get(2), 0.0001),
				() -> assertEquals(1.8502, resultados.get(id1).getTempoPorClientes().get(3), 0.0001),
				() -> assertFalse(resultados.get(id1).getTempoPorClientes().containsKey(4)),
				() -> assertEquals(0, resultados.get(id1).getPerdas()),
				() -> assertEquals(7.0309, resultados.get(id2).getTempoPorClientes().get(0), 0.0001),
				() -> assertEquals(4.6756, resultados.get(id2).getTempoPorClientes().get(1), 0.0001),
				() -> assertEquals(0.3774, resultados.get(id2).getTempoPorClientes().get(2), 0.0001),
				() -> assertEquals(0.4932, resultados.get(id2).getTempoPorClientes().get(3), 0.0001),
				() -> assertFalse(resultados.get(id1).getTempoPorClientes().containsKey(4)),
				() -> assertEquals(0, resultados.get(id2).getPerdas())
		);
	}
	
}
