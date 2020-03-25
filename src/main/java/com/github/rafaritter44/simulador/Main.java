package com.github.rafaritter44.simulador;

import static com.fasterxml.jackson.databind.MapperFeature.PROPAGATE_TRANSIENT_MARKER;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rafaritter44.simulador.fila.SimuladorDeFilas;

public class Main {
	
	public static void main(final String[] args) throws IOException {
		final String arquivo = String.format("/%s.json", args[0]);
		final InputStream is = Main.class.getResourceAsStream(arquivo);
		final ObjectMapper mapper = new ObjectMapper().configure(PROPAGATE_TRANSIENT_MARKER, true);
		final List<Configuracao> configs = mapper.readValue(is, new TypeReference<List<Configuracao>>() {});
		for (final Configuracao config : configs) {
			final SimuladorDeFilas simulador = new SimuladorDeFilas(config.getFila(), config.getSimulacao());
			final HashMap<Integer, Double> resultado = simulador.simular(config.getVezes());
			System.out.println("CONFIG:\n" + mapper.writeValueAsString(config));
			final double tempoTotal = resultado
					.values()
					.stream()
					.reduce(0D, (a, b) -> a + b);
			System.out.printf("TEMPO TOTAL: %.2f\n", tempoTotal);
			System.out.println("RESULTADO:");
			resultado.forEach((clientes, tempo) -> {
				System.out.printf("CLIENTES NA FILA: %d\t", clientes);
				System.out.printf("TEMPO: %.2f\t", tempo);
				System.out.printf("PORCENTAGEM: %.2f\n", tempo * 100 / tempoTotal);
			});
		}
	}
	
}
