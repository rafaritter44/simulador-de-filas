package com.github.rafaritter44.simulador;

import static com.fasterxml.jackson.databind.MapperFeature.PROPAGATE_TRANSIENT_MARKER;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
			Contexto.get().setFilas(config.getFilas());
			final SimuladorDeFilas simulador = new SimuladorDeFilas();
			final Map<Integer, Map<Integer, Double>> resultado = simulador.simular(config.getSimulacao());
			System.out.println("CONFIG:\n" + mapper.writeValueAsString(config));
			resultado
					.entrySet()
					.forEach(fila -> {
						System.out.printf("FILA %d\n", fila.getKey());
						final double tempoTotal = fila
								.getValue()
								.values()
								.parallelStream()
								.reduce(0D, Double::sum);
						System.out.printf("TEMPO TOTAL: %.4f\n", tempoTotal);
						System.out.println("RESULTADO:");
						fila.getValue().forEach((clientes, tempo) -> {
							System.out.printf("CLIENTES NA FILA: %d\t", clientes);
							System.out.printf("TEMPO: %.4f\t", tempo);
							System.out.printf("PORCENTAGEM: %.4f\n", tempo * 100D / tempoTotal);
						});
					});
		}
	}
	
}
