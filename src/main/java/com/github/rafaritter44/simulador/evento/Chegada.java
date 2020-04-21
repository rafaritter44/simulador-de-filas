package com.github.rafaritter44.simulador.evento;

import java.util.Optional;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.fila.Fila;

public class Chegada extends Evento {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	private final Fila fila;
	
	public Chegada(final Fila fila) {
		this.fila = fila;
		final GeradorDeAleatorios geradorDeAleatorios = CONTEXTO.getGeradorDeAleatorios();
		super.setTempo(geradorDeAleatorios.proximo(fila.getTempoMinimoChegada(), fila.getTempoMaximoChegada()));
	}
	
	public Chegada(final Fila fila, final double tempo) {
		this.fila = fila;
		super.setTempo(tempo);
	}

	@Override
	public void executar() {
		super.contabilizarTempo();
		if (fila.getClientes() < fila.getCapacidade()) {
			fila.chegada();
			if (fila.getClientes() <= fila.getServidores()) {
				final Optional<Fila> para = Optional.ofNullable(CONTEXTO.getFilas().get(fila.getParaId()));
				if (para.isPresent()) {
					new Passagem(fila, para.get()).agendar();
				} else {
					new Saida(fila).agendar();	
				}
			}
		}
		new Chegada(fila).agendar();
	}
	
}
