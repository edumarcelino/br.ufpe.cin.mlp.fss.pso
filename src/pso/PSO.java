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
				+ (Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA * Util.NUMERO_NEURONIOS_CAMADA_SAIDA)
				+ Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA
				+ Util.NUMERO_NEURONIOS_CAMADA_SAIDA;
		
		double[] randomPosition = new double[numeroPesos];
		
		double[] randomVelocity = new double[numeroPesos];
		

		// Itera no enxame de particulas para iniciar todas as particulas randomicamente
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
		
		for (int i = 0; i < enxame.length; ++i) {
		
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
						dataset.getDatasetTeste().size());

			}

						
			
			// does current Particle have global best position/solution?
			if (erro < bestGlobalError) {
				bestGlobalError = erro;
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
			double[] weights, int tamanhoBaseTreinamento) {

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

			for (int j = 0; j < saidaEsperada.length; ++j) {
				sumSquaredError += ((saidaRede[j + 1] - saidaEsperada[j]) * (saidaRede[j + 1] - saidaEsperada[j]));
			}
		}
		return sumSquaredError / tamanhoBaseTreinamento;
	}

}
