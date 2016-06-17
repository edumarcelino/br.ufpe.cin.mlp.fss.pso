package pso;

import java.util.Arrays;

import util.Util;

public class Particula {

	public int identificadorParticula;

	// Posicao das particulas , equivalente a NN pesos da rede neural
	public double[] posicao;

	// Medida de velocidade da particula
	public double[] velocidade;

	// Melhor posicao encontrada pela particula
	public double[] melhorPosicao;

	public double melhorErroParticula = Double.MAX_VALUE;

	public int quantidadePesos;

	public double erro;

	public Particula(double[] posicao, double[] velocidade, int identificadorParticula) {

		int pesosTemp1 = (((Util.NUMERO_NEURONIOS_CAMADA_ENTRADA + 1)) * (Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA + 1));
		int pesosTemp2 = ((Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA + 1) * Util.NUMERO_NEURONIOS_CAMADA_SAIDA);
		this.quantidadePesos = pesosTemp1 + pesosTemp2;

		this.posicao = posicao;

		this.velocidade = velocidade;

		this.melhorPosicao = new double[quantidadePesos];
		
		this.identificadorParticula = identificadorParticula;

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

	public int getIdentificadorParticula() {
		return identificadorParticula;
	}

	public void setIdentificadorParticula(int identificadorParticula) {
		this.identificadorParticula = identificadorParticula;
	}
}
