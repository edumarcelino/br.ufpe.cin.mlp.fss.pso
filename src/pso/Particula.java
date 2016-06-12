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

	public Particula(double[] posicao, double[] velocidade) {

		this.quantidadePesos = (Util.NUMERO_NEURONIOS_CAMADA_ENTRADA * Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA)
				+ (Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA * Util.NUMERO_NEURONIOS_CAMADA_SAIDA)
				+ Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA
				+ Util.NUMERO_NEURONIOS_CAMADA_SAIDA;

		this.posicao = posicao;

		this.velocidade = velocidade;

		this.melhorPosicao = new double[quantidadePesos];

		this.melhorPosicao = Arrays.copyOf(melhorPosicao, 0);

	}

	public double getErro() {
		return erro;
	}

	public void setErro(double erro) {
		this.erro = erro;
	}

}
