package pso;

import java.util.Arrays;

import util.Util;

public class Particula {

	// Posicao das particulas , equivalente a NN pesos da rede neural
	public double[] posicao;

	// Medida de velocidade da particula
	public double[] velocidade;

	// Melhor posicao encontrada pela particula
	public double[] melhorPosicao;

	public int quantidadePesos;

	public double erro;

	public Particula(double[] posicao, double[] velocidade, double erro) {

		int quantidadePesos = (Util.NUMERO_NEURONIOS_CAMADA_ENTRADA * Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA)+ (Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA* Util.NUMERO_NEURONIOS_CAMADA_SAIDA);
		
		this.posicao = new double[quantidadePesos];

		this.posicao = Arrays.copyOf(posicao, 0);

		this.velocidade = new double[quantidadePesos];

		this.velocidade = Arrays.copyOf(velocidade, 0);

		this.melhorPosicao = new double[quantidadePesos];

		this.melhorPosicao = Arrays.copyOf(melhorPosicao, 0);

		this.erro = erro;

	}

}
