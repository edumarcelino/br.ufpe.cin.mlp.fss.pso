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
	public double c1 = 1.49445;

	// Particula confia no bando
	public double c2 = 1.49445;

	public double r1, r2;

	int pesosTemp1 = (((Util.NUMERO_NEURONIOS_CAMADA_ENTRADA + 1)) * (Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA + 1));
	int pesosTemp2 = ((Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA + 1) * Util.NUMERO_NEURONIOS_CAMADA_SAIDA);
	
	public int numeroPesos = pesosTemp1 + pesosTemp2;

	public double bestGlobalError = Double.MAX_VALUE;

	public double[] bestGlobalPosition = new double[numeroPesos];

	public Particula[] enxame;

	double minX = -10.0; // for each weight. assumes data has been normalized
							// about 0
	double maxX = 10.0;

	public void inicializaEnxame(int quantidadeParticulas) {

		enxame = new Particula[quantidadeParticulas];

		int pesosTemp1 = (((Util.NUMERO_NEURONIOS_CAMADA_ENTRADA + 1)) * (Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA + 1));
		int pesosTemp2 = ((Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA + 1) * Util.NUMERO_NEURONIOS_CAMADA_SAIDA);
		numeroPesos = pesosTemp1 + pesosTemp2;

		double[] randomPosition = new double[numeroPesos];

		double[] randomVelocity = new double[numeroPesos];

		// Itera no enxame de particulas para iniciar todas as particulas
		// randomicamente
		for (int i = 0; i < enxame.length; ++i) {

			// Randomiza a posicao inicial das particulas.

			for (int j = 0; j < randomPosition.length; ++j) {

				randomPosition[j] = (maxX - minX) * Math.random() + minX;

			}
			for (int j = 0; j < randomVelocity.length; ++j) {

				double lo = 0.1 * minX;

				double hi = 0.1 * maxX;

				randomVelocity[j] = (hi - lo) * Math.random() + lo;
			}

			enxame[i] = new Particula(randomPosition, randomVelocity);
		}

	}

	public double[] atualizaVelocidade(Particula particula) {
		r1 = Math.random();
		r2 = Math.random();
		double[] novaVelocidade = new double[particula.velocidade.length];

		for (int j = 0; j < particula.velocidade.length; j++) {

			novaVelocidade[j] = (w * particula.velocidade[j])
					+ (c1 * r1 * (particula.melhorPosicao[j] - particula.posicao[j]))
					+ (c2 * r2 * (bestGlobalPosition[j] - particula.posicao[j]));

		}

		return novaVelocidade;
	}

	public double[] atualizaPosicao(Particula particula, double[] novaVelocidade) {
		double[] novaPosicao = new double[particula.posicao.length];

		for (int j = 0; j < particula.posicao.length; j++) {

			novaPosicao[j] = particula.posicao[j] + novaVelocidade[j];
		}

		return novaPosicao;
	}


	
	public double meanSquaredError(double[] padrao, double[] saidaEsperada,
			double[] weights, int tamanhoBaseTreinamento) {

		MLPHibrida mlpTemp = new MLPHibrida(
				Util.NUMERO_NEURONIOS_CAMADA_ENTRADA,
				Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA,
				Util.NUMERO_NEURONIOS_CAMADA_SAIDA);

		mlpTemp.setPesos(weights);

		double sumSquaredError = 0.0;

		double[] saidaRede = mlpTemp.apresentaPadrao(padrao);

		//System.out.print(" Saida Rede: " + saidaRede[1] + " - " + saidaRede[2]
		//		+ " - " + saidaRede[3]);

		for (int i = 0; i < tamanhoBaseTreinamento; ++i) {

			for (int j = 0; j < saidaEsperada.length; ++j) {
				sumSquaredError += ((saidaRede[j] - saidaEsperada[j]) * (saidaRede[j] - saidaEsperada[j]));
			}
		}
		return sumSquaredError / tamanhoBaseTreinamento;
	}

	public double getBestGlobalError() {
		return bestGlobalError;
	}

	public void setBestGlobalError(double bestGlobalError) {
		this.bestGlobalError = bestGlobalError;
	}

	public double[] getBestGlobalPosition() {
		return bestGlobalPosition;
	}

	public void setBestGlobalPosition(double[] bestGlobalPosition) {
		this.bestGlobalPosition = bestGlobalPosition;
	}

	
	
}
