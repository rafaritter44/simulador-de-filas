package com.github.rafaritter44.simulador.evento;

import java.util.Optional;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.fila.Fila;

public class Passagem extends Evento {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	private final Fila de;
	private final Fila para;
	
	public Passagem(final Fila de, final Fila para) {
		this.de = de;
		this.para = para;
		final GeradorDeAleatorios geradorDeAleatorios = CONTEXTO.getGeradorDeAleatorios();
		super.setTempo(geradorDeAleatorios.proximo(de.getTempoMinimoSaida(), de.getTempoMaximoSaida()));
	}

	@Override
	public void executar() {
		super.contabilizarTempo();
		de.saida();
		if (de.getClientes() >= de.getServidores()) {
			new Passagem(de, para).agendar();
		}
		if (para.getClientes() < para.getCapacidade()) {
			para.chegada();
			if (para.getClientes() <= para.getServidores()) {
				final Optional<Fila> para = Optional.ofNullable(CONTEXTO.getFilas().get(this.para.getParaId()));
				if (para.isPresent()) {
					new Passagem(this.para, para.get()).agendar();
				} else {
					new Saida(this.para).agendar();	
				}
			}
		}
	}
	
}
