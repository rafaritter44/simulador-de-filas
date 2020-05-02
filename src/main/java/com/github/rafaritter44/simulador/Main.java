package com.github.rafaritter44.simulador;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.rafaritter44.simulador.fila.Resultado;
import com.github.rafaritter44.simulador.fila.SimuladorDeFilas;

public class Main {
	
	public static void main(final String[] args) throws IOException {
		final String arquivo = String.format("/%s.json", args[0]);
		final InputStream is = Main.class.getResourceAsStream(arquivo);
		final ObjectMapper mapper = new ObjectMapper()
				.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
				.enable(SerializationFeature.INDENT_OUTPUT);
		final List<Configuracao> configs = mapper.readValue(is, new TypeReference<List<Configuracao>>() {});
		for (final Configuracao config : configs) {
			Contexto.get().setFilas(config.getFilas());
			final SimuladorDeFilas simulador = new SimuladorDeFilas();
			final Map<Integer, Resultado> resultados = simulador.simular(config.getSimulacao());
			System.out.println("CONFIG:\n" + mapper.writeValueAsString(config));
			resultados
					.entrySet()
					.forEach(fila -> {
						final Resultado resultado = fila.getValue();
						System.out.printf("FILA %d\n", fila.getKey());
						final double tempoTotal = resultado
								.getTempoPorClientes()
								.values()
								.parallelStream()
								.reduce(0D, Double::sum);
						System.out.printf("TEMPO TOTAL: %.4f\n", tempoTotal);
						System.out.println("RESULTADO:");
						resultado.getTempoPorClientes().forEach((clientes, tempo) -> {
							System.out.printf("CLIENTES NA FILA: %d\t", clientes);
							System.out.printf("TEMPO: %.4f\t", tempo);
							System.out.printf("PORCENTAGEM: %.4f\n", tempo * 100D / tempoTotal);
						});
						System.out.printf("PERDAS: %d\n", resultado.getPerdas());
					});
		}
	}
	
}
