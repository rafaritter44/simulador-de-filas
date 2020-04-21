package com.github.rafaritter44.simulador.evento;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.fila.Fila;

public class Saida extends Evento {
	
	private final Fila fila;
	
	public Saida(final Fila fila) {
		this.fila = fila;
		final GeradorDeAleatorios geradorDeAleatorios = Contexto.get().getGeradorDeAleatorios();
		super.setTempo(geradorDeAleatorios.proximo(fila.getTempoMinimoSaida(), fila.getTempoMaximoSaida()));
	}

	@Override
	public void executar() {
		super.contabilizarTempo();
		fila.saida();
		if (fila.getClientes() >= fila.getServidores()) {
			new Saida(fila).agendar();
		}
	}

}
