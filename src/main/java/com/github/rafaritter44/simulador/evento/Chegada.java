package com.github.rafaritter44.simulador.evento;

import java.util.List;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.fila.Fila;
import com.github.rafaritter44.simulador.fila.Roteamento;

/**
 * Representa um {@link Evento} de chegada.
 * 
 * @author Rafael Ritter
 */
public class Chegada extends Evento {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	private final Fila fila;
	
	/**
	 * Constrói um novo {@link Evento} de chegada na {@link Fila} passada por parâmetro.
	 * O {@link Evento#tempo} deste evento é definido com base nos métodos
	 * {@link Fila#getTempoMinimoChegada()} e {@link Fila#getTempoMaximoChegada()}, e no
	 * aleatório gerado pelo {@link GeradorDeAleatorios} definido no {@link Contexto} da simulação.
	 * 
	 * @param fila A fila na qual essa chegada ocorrerá
	 */
	public Chegada(final Fila fila) {
		this.fila = fila;
		final GeradorDeAleatorios geradorDeAleatorios = CONTEXTO.getGeradorDeAleatorios();
		super.setTempo(geradorDeAleatorios.proximo(fila.getTempoMinimoChegada(), fila.getTempoMaximoChegada()));
	}
	
	public Chegada(final Fila fila, final double tempo) {
		this.fila = fila;
		super.setTempo(tempo);
	}

	/**
	 * <p>Rotina que executa o algoritmo da chegada.</p>
	 * 
	 * <p>Primeiramente, contabiliza o tempo ({@link Evento#contabilizarTempo()}). Em seguida,
	 * verifica se a {@link Chegada#fila} está cheia. Caso esteja, contabiliza uma perda
	 * ({@link Fila#perda()}; caso contrário, o cliente entra na fila ({@link Fila#chegada()}),
	 * e é verificado se o cliente está de frente para um servidor. Caso esteja, é agendada a
	 * saída do cliente da {@link Chegada#fila}, seja por meio de uma {@link Saida}, seja por
	 * meio de uma {@link Passagem} para outra {@link Fila}. Isso dependerá da configuração da
	 * simulação, e possivelmente dos aleatórios gerados pelo {@link GeradorDeAleatorios},
	 * caso se trate de uma rede de filas com probabilidades de roteamento.</p>
	 */
	@Override
	public void executar() {
		super.contabilizarTempo();
		if (fila.isCapacidadeInfinita() || fila.getClientes() < fila.getCapacidade()) {
			fila.chegada();
			if (fila.getClientes() <= fila.getServidores()) {
				final List<Roteamento> roteamentos = fila.getRoteamentos();
				if (roteamentos.isEmpty()) {
					new Saida(fila).agendar();
				} else if (roteamentos.get(0).getProbabilidade() == 1D) {
					final Fila destino = CONTEXTO.getFilas().get(roteamentos.get(0).getDestino());
					new Passagem(fila, destino).agendar();
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
		} else {
			fila.perda();
		}
		new Chegada(fila).agendar();
	}
	
	@Override
	public String toString() {
		return super.toString() + ";fila=" + fila.getId();
	}
	
}
