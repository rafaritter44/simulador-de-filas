package com.github.rafaritter44.simulador.evento;

import java.util.List;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.fila.Fila;
import com.github.rafaritter44.simulador.fila.Roteamento;

/**
 * Representa um {@link Evento} de saída.
 * 
 * @author Rafael Ritter
 */
public class Saida extends Evento {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	private final Fila fila;
	
	/**
	 * Constrói um novo {@link Evento} de saída da {@link Fila} passada por parâmetro.
	 * O {@link Evento#tempo} deste evento é definido com base nos métodos
	 * {@link Fila#getTempoMinimoSaida()} e {@link Fila#getTempoMaximoSaida()}, e no
	 * aleatório gerado pelo {@link GeradorDeAleatorios} definido no {@link Contexto} da simulação.
	 * 
	 * @param fila A fila na qual essa saída ocorrerá
	 */
	public Saida(final Fila fila) {
		this.fila = fila;
		final GeradorDeAleatorios geradorDeAleatorios = CONTEXTO.getGeradorDeAleatorios();
		super.setTempo(geradorDeAleatorios.proximo(fila.getTempoMinimoSaida(), fila.getTempoMaximoSaida()));
	}

	/**
	 * <p>Rotina que executa o algoritmo da saída.</p>
	 * 
	 * <p>Primeiramente, contabiliza o tempo ({@link Evento#contabilizarTempo()}). Em seguida, o cliente
	 * sai da fila ({@link Fila#saida()}), e é verificado se existe algum cliente de frente para um
	 * servidor. Caso exista, é agendada a sua saída da {@link Saida#fila}, seja por meio de uma
	 * {@link Saida}, seja por meio de uma {@link Passagem} para outra {@link Fila}. Isso dependerá da
	 * configuração da simulação, e possivelmente dos aleatórios gerados pelo {@link GeradorDeAleatorios},
	 * caso se trate de uma rede de filas com probabilidades de roteamento.</p>
	 */
	@Override
	public void executar() {
		super.contabilizarTempo();
		fila.saida();
		if (fila.getClientes() >= fila.getServidores()) {
			final List<Roteamento> roteamentos = fila.getRoteamentos();
			if (roteamentos.isEmpty()) {
				new Saida(fila).agendar();
			} else {
				final double aleatorio = CONTEXTO.getGeradorDeAleatorios().proximo();
				double probabilidadeAcumulada = 0D;
				boolean passagemAgendada = false;
				for (final Roteamento roteamento : roteamentos) {
					probabilidadeAcumulada += roteamento.getProbabilidade();
					if (aleatorio < probabilidadeAcumulada) {
						final Fila destino = CONTEXTO.getFilas().get(roteamento.getDestino());
						new Passagem(fila, destino).agendar();
						passagemAgendada = true;
						break;
					}
				}
				if (!passagemAgendada) {
					new Saida(fila).agendar();
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return super.toString() + ";fila=" + fila.getId();
	}

}
