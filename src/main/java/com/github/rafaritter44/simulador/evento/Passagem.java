package com.github.rafaritter44.simulador.evento;

import java.util.List;

import com.github.rafaritter44.simulador.Contexto;
import com.github.rafaritter44.simulador.aleatorio.GeradorDeAleatorios;
import com.github.rafaritter44.simulador.fila.Fila;
import com.github.rafaritter44.simulador.fila.Roteamento;

/**
 * Representa um {@link Evento} de passagem.
 * 
 * @author Rafael Ritter
 */
public class Passagem extends Evento {
	
	private static final Contexto CONTEXTO = Contexto.get();
	
	private final Fila origem;
	private final Fila destino;
	
	/**
	 * Constrói um novo {@link Evento} de passagem da {@link Fila} origem para a destino.
	 * O {@link Evento#tempo} deste evento é definido com base nos métodos
	 * {@link Fila#getTempoMinimoSaida()} e {@link Fila#getTempoMaximoSaida()} da origem, e no
	 * aleatório gerado pelo {@link GeradorDeAleatorios} definido no {@link Contexto} da simulação.
	 * 
	 * @param origem A fila da qual o cliente sairá
	 * @param destino A fila na qual o cliente chegará
	 */
	public Passagem(final Fila origem, final Fila destino) {
		this.origem = origem;
		this.destino = destino;
		final GeradorDeAleatorios geradorDeAleatorios = CONTEXTO.getGeradorDeAleatorios();
		super.setTempo(geradorDeAleatorios.proximo(origem.getTempoMinimoSaida(), origem.getTempoMaximoSaida()));
	}

	/**
	 * <p>Rotina que executa o algoritmo da passagem.</p>
	 * 
	 * <p>Primeiramente, contabiliza o tempo ({@link Evento#contabilizarTempo()}). Em seguida, o cliente
	 * sai da {@link Passagem#origem} ({@link Fila#saida()}), e é verificado se existe algum cliente de frente
	 * para um servidor na {@link Passagem#origem}. Caso exista, é agendada a sua saída da
	 * {@link Passagem#origem}, seja por meio de uma {@link Saida}, seja por meio de uma {@link Passagem} para
	 * outra {@link Fila}. Isso dependerá da configuração da simulação, e possivelmente dos aleatórios gerados
	 * pelo {@link GeradorDeAleatorios}, caso se trate de uma rede de filas com probabilidades de roteamento.</p>
	 * 
	 * <p>Em seguida, verifica se o {@link Passagem#destino} está cheio. Caso esteja, contabiliza uma perda
	 * ({@link Fila#perda()} para o {@link Passagem#destino}; caso contrário, o cliente entra na fila
	 * ({@link Fila#chegada()}), e é verificado se o cliente está de frente para um servidor. Caso esteja, é
	 * agendada a saída do cliente da {@link Passagem#destino}, seja por meio de uma {@link Saida}, seja por
	 * meio de uma {@link Passagem} para outra {@link Fila}. Isso dependerá da configuração da simulação, e
	 * possivelmente dos aleatórios gerados pelo {@link GeradorDeAleatorios}, caso se trate de uma rede de
	 * filas com probabilidades de roteamento.</p>
	 */
	@Override
	public void executar() {
		super.contabilizarTempo();
		origem.saida();
		if (origem.getClientes() >= origem.getServidores()) {
			final List<Roteamento> roteamentos = origem.getRoteamentos();
			if (roteamentos.get(0).getProbabilidade() == 1D) {
				new Passagem(origem, destino).agendar();
			} else {
				final double aleatorio = CONTEXTO.getGeradorDeAleatorios().proximo();
				double probabilidadeAcumulada = 0D;
				boolean passagemAgendada = false;
				for (final Roteamento roteamento : roteamentos) {
					probabilidadeAcumulada += roteamento.getProbabilidade();
					if (aleatorio < probabilidadeAcumulada) {
						final Fila proximoDestino = CONTEXTO.getFilas().get(roteamento.getDestino());
						new Passagem(origem, proximoDestino).agendar();
						passagemAgendada = true;
						break;
					}
				}
				if (!passagemAgendada) {
					new Saida(origem).agendar();
				}
			}
		}
		if (destino.isCapacidadeInfinita() || destino.getClientes() < destino.getCapacidade()) {
			destino.chegada();
			if (destino.getClientes() <= destino.getServidores()) {
				final List<Roteamento> roteamentosDestino = destino.getRoteamentos();
				if (roteamentosDestino.isEmpty()) {
					new Saida(destino).agendar();
				} else if (roteamentosDestino.get(0).getProbabilidade() == 1D) {
					final Fila proximoDestino = CONTEXTO.getFilas().get(roteamentosDestino.get(0).getDestino());
					new Passagem(destino, proximoDestino).agendar();
				} else {
					final double aleatorio = CONTEXTO.getGeradorDeAleatorios().proximo();
					double probabilidadeAcumulada = 0D;
					boolean passagemAgendada = false;
					for (final Roteamento roteamento : roteamentosDestino) {
						probabilidadeAcumulada += roteamento.getProbabilidade();
						if (aleatorio < probabilidadeAcumulada) {
							final Fila proximoDestino = CONTEXTO.getFilas().get(roteamento.getDestino());
							new Passagem(destino, proximoDestino).agendar();
							passagemAgendada = true;
							break;
						}
					}
					if (!passagemAgendada) {
						new Saida(destino).agendar();
					}
				}
			}
		} else {
			destino.perda();
		}
	}
	
	@Override
	public String toString() {
		return super.toString() + ";origem=" + origem.getId() + ";destino=" + destino.getId();
	}
	
}
