package pso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import util.Util;

import dataset.Dataset;

import MLP.MLPHibrida;

public class PSO {

	// Inercia e controla a capacidade de exploracao do espaco de solucoes
	public double w = 0.729;

	// Particula confia em si
	public double c1 = 1.4;

	// Particula confia no bando
	public double c2 = 1.4;

	public double r1, r2;

	public int numeroPesos = 15;

	public double bestGlobalError = Double.MAX_VALUE;

	double[] bestGlobalPosition;

	public Particula[] enxame;

	double minX = -10.0; // for each weight. assumes data has been normalized
							// about 0
	double maxX = 10.0;

	public void inicializaEnxame(int quantidadeParticulas) {

		enxame = new Particula[quantidadeParticulas];

		numeroPesos = (Util.NUMERO_NEURONIOS_CAMADA_ENTRADA * Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA)
				+ (Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA * Util.NUMERO_NEURONIOS_CAMADA_SAIDA)+ Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA + Util.NUMERO_NEURONIOS_CAMADA_SAIDA;

		// Itera no enxame de particulas
		for (int i = 0; i < enxame.length; ++i) {

			// Randomiza a posicao inicial das particulas.
			double[] randomPosition = new double[numeroPesos];

			
			for (int j = 0; j < randomPosition.length; ++j) {
				
				randomPosition[j] = (maxX - minX) * Math.random() + minX;
			
			}

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

				erro = meanSquaredError(padrao, saidaEsperada, randomPosition,
						Util.NUMERO_NEURONIOS_CAMADA_ENTRADA,
						Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA,
						Util.NUMERO_NEURONIOS_CAMADA_SAIDA, dataset
								.getDatasetTeste().size());

			}

			double[] randomVelocity = new double[numeroPesos];

			for (int j = 0; j < randomVelocity.length; ++j) {
				double lo = 0.1 * minX;
				double hi = 0.1 * maxX;
				randomVelocity[j] = (hi - lo) * Math.random() + lo;

			}
			enxame[i] = new Particula(randomPosition, randomVelocity, erro);
			// does current Particle have global best position/solution?
			if (enxame[i].erro < bestGlobalError) {
				bestGlobalError = enxame[i].erro;
				bestGlobalPosition = Arrays.copyOf(enxame[i].posicao, 0);
			}
		}
	}

	public double[] atualizaVelocidade(Particula particula) {
		r1 = Math.random();
		r2 = Math.random();
		double[] novaVelocidade = null;

		for (int j = 0; j < particula.velocidade.length; j++) {

			novaVelocidade[j] = (w * particula.velocidade[j])
					+ (c1 * r1 * (particula.melhorPosicao[j] - particula.posicao[j]))
					+ (c2 * r2 * (bestGlobalPosition[j] - particula.posicao[j]));

		}

		return novaVelocidade;
	}

	public double[] atualizaPosicao(Particula particula, double[] novaVelocidade) {
		double[] novaPosicao = null;

		for (int j = 0; j < particula.posicao.length; j++) {

			novaPosicao[j] = particula.posicao[j] + novaVelocidade[j];
		}

		return novaPosicao;
	}

	public double meanSquaredError(double[] padrao, double[] saidaEsperada,
			double[] weights, int numeroNeuroniosEntrada,
			int numeroNeuroniosEscondida, int numeroNeuroniosSaida,
			int tamanhoBaseTreinamento) {

		MLPHibrida mlpTemp = new MLPHibrida(
				Util.NUMERO_NEURONIOS_CAMADA_ENTRADA,
				Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA,
				Util.NUMERO_NEURONIOS_CAMADA_SAIDA);

		mlpTemp.setPesos(weights);

		double[] xValues = new double[Util.NUMERO_NEURONIOS_CAMADA_ENTRADA]; // inputs
		double[] tValues = new double[Util.NUMERO_NEURONIOS_CAMADA_SAIDA]; // targets

		double sumSquaredError = 0.0;

		for (int i = 0; i < padrao.length; ++i) {

			double[] saidaRede = mlpTemp.apresentaPadrao(padrao);

			for (int j = 0; j < saidaEsperada.length; ++j){
				System.out.println("j: "+ j);
				sumSquaredError += ((saidaRede[j+1] - saidaEsperada[j]) * (saidaRede[j+1] - saidaEsperada[j]));
			}	
		}
		return sumSquaredError / tamanhoBaseTreinamento;
	}

}
