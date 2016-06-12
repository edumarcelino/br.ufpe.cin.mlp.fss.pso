package MLP;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import dataset.Dataset;

import pso.PSO;
import pso.Particula;
import util.Util;

public class MLPHibrida {

	public static final String TREINAMENTO_BACK_PROPAGATION = "backpropagation";
	public static final String TREINAMENTO_PARTICLE_SWARM_OPTIMIZATION = "pso";
	public static final String TREINAMENTO_FISH_SCHOOL_SEARCH = "fss";

	// Numero de neuronios nas camadas
	private int numeroNeuroniosEntrada;
	private int numeroNeuroniosEscondida;
	private int numeroNeuroniosSaida;

	// Camadas da rede
	private double[] camadaEntrada;
	private double[] camadaEscondida;
	private double[] camadaSaida;

	// Pesos entre as camadas
	private double[][] pesosCamadaEntradaEscondida;
	private double[][] pesosCamadaEscondidaSaida;

	// Taxa de aprendizagem
	private double TAXA_APRENDIZAGEM = 0.5;

	/**
	 * Cria uma nova instancia da rede SimpleMLP.
	 * 
	 * @param numeroNeuroniosEntrada
	 *            numero de neuronios da camada de entrada
	 * @param numeroNeuroniosEscondida
	 *            numero de neuronios da camada escondida
	 * @param numeroNeuroniosSaida
	 *            numero de neuronios da camada de saída
	 */
	public MLPHibrida(int numeroNeuroniosEntrada, int numeroNeuroniosEscondida,
			int numeroNeuroniosSaida) {

		this.numeroNeuroniosEntrada = numeroNeuroniosEntrada;
		this.numeroNeuroniosEscondida = numeroNeuroniosEscondida;
		this.numeroNeuroniosSaida = numeroNeuroniosSaida;

		// Inicializa as camadas da rede MLP
		camadaEntrada = new double[numeroNeuroniosEntrada + 1];
		camadaEscondida = new double[numeroNeuroniosEscondida + 1];
		camadaSaida = new double[numeroNeuroniosSaida + 1];

		pesosCamadaEntradaEscondida = new double[numeroNeuroniosEscondida + 1][numeroNeuroniosEntrada + 1];
		pesosCamadaEscondidaSaida = new double[numeroNeuroniosSaida + 1][numeroNeuroniosEscondida + 1];

		geraRandomicamentePesos();
	}

	/**
	 * Seta a taxa de aprendizagem
	 * 
	 * @param taxaAprendizagem
	 *            taxa de aprendizagem para a rede
	 */
	public void setTaxaAprendizagem(double taxaAprendizagem) {
		TAXA_APRENDIZAGEM = taxaAprendizagem;
	}

	/**
	 * Gera os pesos randomicamente entre os valores 0.5 e -0.5
	 * 
	 */
	private void geraRandomicamentePesos() {

		for (int j = 1; j <= numeroNeuroniosEscondida; j++) {
			for (int i = 0; i <= numeroNeuroniosEntrada; i++) {
				pesosCamadaEntradaEscondida[j][i] = Math.random() - 0.5;
			}
		}

		for (int j = 1; j <= numeroNeuroniosSaida; j++) {
			for (int i = 0; i <= numeroNeuroniosEscondida; i++) {
				pesosCamadaEscondidaSaida[j][i] = Math.random() - 0.5;
			}
		}
	}

	/**
	 * Inicia o treinamento com o envio de um padrão e uma saida desejada
	 * 
	 * @param numeroNeuroniosEntrada
	 *            numero de neuronios da camada de entrada
	 * @param numeroNeuroniosEscondida
	 *            numero de neuronios da camada escondida
	 * @param numeroNeuroniosSaida
	 *            numero de neuronios da camada de saída
	 */
	public double[] treinamento(double[] padrao, double[] saidaDesejada,
			String tipoTreinamento) {

		double[] saidaRede = apresentaPadrao(padrao);

		if (tipoTreinamento.equals(TREINAMENTO_BACK_PROPAGATION)) {

			backPropagation(saidaDesejada);

		} else if (tipoTreinamento.equals(TREINAMENTO_FISH_SCHOOL_SEARCH)) {

			fssTreinamento(saidaDesejada);

		} else if (tipoTreinamento
				.equals(TREINAMENTO_PARTICLE_SWARM_OPTIMIZATION)) {

			psoTreinamento(saidaDesejada);

		}

		return camadaSaida;
	}

	private void fssTreinamento(double[] saidaDesejada) {

	}

	private void psoTreinamento(double[] saidaDesejada) {
		
		PSO pso = new PSO();

		// Inicializa 15 particulas no enxame do PSO.
		pso.inicializaEnxame(15);
		
		int numIteracoes = Util.NUMERO_ITERACOES_PSO;
		
		while (numIteracoes > 0 && pso.bestGlobalError > Util.ERRO_PARADA_PSO) {

			for (Particula particula : pso.enxame) {

				double[] novaVelocidade = pso.atualizaVelocidade(particula);
				
				double[] novaPosicao = pso.atualizaPosicao(particula,
						novaVelocidade);
				Dataset dataset = new Dataset();
				ArrayList<String[]> dt = dataset.getDatasetTreino();
				double erro = 0;

				for (Iterator iterator = dt.iterator(); iterator.hasNext();) {

					String[] linha = (String[]) iterator.next();

					// Converte a linha do dataset para treinar a rede MLP
					double[] padrao = new double[4];
					padrao[0] = Double.parseDouble(linha[0]);
					padrao[1] = Double.parseDouble(linha[1]);
					padrao[2] = Double.parseDouble(linha[2]);
					padrao[3] = Double.parseDouble(linha[3]);

					// Converte a saida esperada para o treinamento
					double[] saidaEsperada = new double[3];
					saidaEsperada[0] = Double.parseDouble(linha[4]);
					saidaEsperada[1] = Double.parseDouble(linha[5]);
					saidaEsperada[2] = Double.parseDouble(linha[6]);

					erro = pso.meanSquaredError(padrao, saidaEsperada,
							novaPosicao, dataset
									.getDatasetTeste().size());

				}
			}

			numIteracoes = numIteracoes - 1;
		}

	}

	/**
	 * Passa um padrao para a rede. Utiliza a funcao de ativacao a logistica
	 * 
	 * @param padrao
	 *            que sera passada pela rede
	 * 
	 * @return a saida da rede para o padrao
	 */
	public double[] apresentaPadrao(double[] padrao) {

		// A camada de entrada recebe o padrão para propagar na rede
		for (int i = 0; i < numeroNeuroniosEntrada; i++) {
			camadaEntrada[i + 1] = padrao[i];
		}

		// Seta o bias
		camadaEntrada[0] = 1.0;
		camadaEscondida[0] = 1.0;

		// Itera em todos os neuronios da camada escondida para repassar pela
		// camada escondida
		for (int j = 1; j <= numeroNeuroniosEscondida; j++) {
			camadaEscondida[j] = 0.0;
			for (int i = 0; i <= numeroNeuroniosEntrada; i++) {
				camadaEscondida[j] += pesosCamadaEntradaEscondida[j][i]
						* camadaEntrada[i];
			}
			camadaEscondida[j] = 1.0 / (1.0 + Math.exp(-camadaEscondida[j]));
		}

		// Itera em todos os neuronios da camada de saida para repassar pela
		// camada de saida
		for (int j = 1; j <= numeroNeuroniosSaida; j++) {
			camadaSaida[j] = 0.0;
			for (int i = 0; i <= numeroNeuroniosEscondida; i++) {

				camadaSaida[j] += pesosCamadaEscondidaSaida[j][i]
						* camadaEscondida[i];

			}
			camadaSaida[j] = 1.0 / (1.0 + Math.exp(-camadaSaida[j]));
		}

		// Retorna a saida atraves da rede
		return camadaSaida;
	}

	/**
	 * Metodo para ajustar os pesos utilizando o algoritmo backPropagation. A
	 * saida é comparada com a saida da rede e os pesos sao ajustado de acordo
	 * com a taxa de aprendizagem.
	 * 
	 * @param saidaDesejada
	 *            saida desejada de acordo com o padrao apresentado
	 */
	private void backPropagation(double[] saidaDesejada) {

		double[] erroL2 = new double[numeroNeuroniosSaida + 1];
		double[] erroL1 = new double[numeroNeuroniosEscondida + 1];

		double eSum = 0.0;

		for (int i = 1; i <= numeroNeuroniosSaida; i++)

			// Layer 2 error gradient
			erroL2[i] = camadaSaida[i] * (1.0 - camadaSaida[i])
					* (saidaDesejada[i - 1] - camadaSaida[i]);

		for (int i = 0; i <= numeroNeuroniosEscondida; i++) {
			// Layer 1 error gradient
			for (int j = 1; j <= numeroNeuroniosSaida; j++) {
				eSum += pesosCamadaEscondidaSaida[j][i] * erroL2[j];
			}

			erroL1[i] = camadaEscondida[i] * (1.0 - camadaEscondida[i]) * eSum;
			eSum = 0.0;
		}

		for (int j = 1; j <= numeroNeuroniosSaida; j++) {
			for (int i = 0; i <= numeroNeuroniosEscondida; i++) {
				pesosCamadaEscondidaSaida[j][i] += TAXA_APRENDIZAGEM
						* erroL2[j] * camadaEscondida[i];
			}
		}

		for (int j = 1; j <= numeroNeuroniosEscondida; j++) {
			for (int i = 0; i <= numeroNeuroniosEntrada; i++) {
				pesosCamadaEntradaEscondida[j][i] += TAXA_APRENDIZAGEM
						* erroL1[j] * camadaEntrada[i];
			}
		}
	}

	public void setPesos(double[] pesos) {

		int numeroPesos = (Util.NUMERO_NEURONIOS_CAMADA_ENTRADA * Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA)
				+ (Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA + Util.NUMERO_NEURONIOS_CAMADA_SAIDA);

		if (pesos.length != numeroPesos) {
			new Exception("Erro no tamanho dos pesos.");
		}

		int k = 0;

		for (int j = 1; j <= Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA; j++) {
			for (int i = 0; i <= Util.NUMERO_NEURONIOS_CAMADA_ENTRADA; i++) {
				System.out.println("i:" + i + " j: " + j + " K:" + k
						+ " peso: " + pesos[k]);
				pesosCamadaEntradaEscondida[j][i] = pesos[k++];
			}
		}

		System.out.println("--------------------------------");

		for (int j = 1; j <= Util.NUMERO_NEURONIOS_CAMADA_SAIDA; j++) {
			for (int i = 0; i <= Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA; i++) {
				System.out.println("i:" + i + " j: " + j + " K:" + k + " peso:"
						+ pesos[k]);
				pesosCamadaEscondidaSaida[j][i] = pesos[k++];
			}
		}

	}

	public int getNumeroNeuroniosEntrada() {
		return numeroNeuroniosEntrada;
	}

	public void setNumeroNeuroniosEntrada(int numeroNeuroniosEntrada) {
		this.numeroNeuroniosEntrada = numeroNeuroniosEntrada;
	}

	public int getNumeroNeuroniosEscondida() {
		return numeroNeuroniosEscondida;
	}

	public void setNumeroNeuroniosEscondida(int numeroNeuroniosEscondida) {
		this.numeroNeuroniosEscondida = numeroNeuroniosEscondida;
	}

	public int getNumeroNeuroniosSaida() {
		return numeroNeuroniosSaida;
	}

	public void setNumeroNeuroniosSaida(int numeroNeuroniosSaida) {
		this.numeroNeuroniosSaida = numeroNeuroniosSaida;
	}

	public double[][] getPesosCamadaEntradaEscondida() {
		return pesosCamadaEntradaEscondida;
	}

	public void setPesosCamadaEntradaEscondida(
			double[][] pesosCamadaEntradaEscondida) {
		this.pesosCamadaEntradaEscondida = pesosCamadaEntradaEscondida;
	}

	public double[][] getPesosCamadaEscondidaSaida() {
		return pesosCamadaEscondidaSaida;
	}

	public void setPesosCamadaEscondidaSaida(
			double[][] pesosCamadaEscondidaSaida) {
		this.pesosCamadaEscondidaSaida = pesosCamadaEscondidaSaida;
	}

}