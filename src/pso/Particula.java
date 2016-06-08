package pso;

import java.util.Arrays;

public class Particula {
	
	// Posicao das particulas , equivalente a NN pesos da rede neural
	public double[] position; 
	
	// measure of fitness
	public double error; 
	
	// Medida de velocidade da particula
	public double[] velocity;

	// Melhor posicao encontrada pela particula
	public double[] bestPosition; 
	
	public double bestError;


	public Particula(double[] posicao, double error, double[] velocidade,
			double[] melhorPosicao, double melhorErro) {
		this.position = new double[posicao.length];

		this.position = Arrays.copyOf(posicao, 0);

		this.error = error;

		this.velocity = new double[velocidade.length];

		this.velocity = Arrays.copyOf(velocidade, 0);

		this.bestPosition = new double[melhorPosicao.length];

		this.bestPosition = Arrays.copyOf(melhorPosicao, 0);

		this.bestError = melhorErro;

	}

}
