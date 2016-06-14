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
	
	public double melhorErroParticula = Double.MAX_VALUE;

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


	}

	public double getErro() {
		return erro;
	}

	public void setErro(double erro) {
		this.erro = erro;
	}

	public double[] getPosicao() {
		return posicao;
	}

	public void setPosicao(double[] posicao) {
		this.posicao = posicao;
	}

	public double[] getVelocidade() {
		return velocidade;
	}

	public void setVelocidade(double[] velocidade) {
		this.velocidade = velocidade;
	}

	public double[] getMelhorPosicao() {
		return melhorPosicao;
	}

	public void setMelhorPosicao(double[] melhorPosicao) {
		this.melhorPosicao = melhorPosicao;
	}

	public double getMelhorErroParticula() {
		return melhorErroParticula;
	}

	public void setMelhorErroParticula(double melhorErroParticula) {
		this.melhorErroParticula = melhorErroParticula;
	}

	
	
}
