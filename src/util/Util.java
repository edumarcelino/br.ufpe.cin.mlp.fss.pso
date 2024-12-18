package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Util {


	public static double FUNCTION_INF_LIMIT = -10.0;

	public static double FUNCTION_SUP_LIMIT = 10.0;

	// Constantes da MLP

	public static int NUMERO_NEURONIOS_CAMADA_ENTRADA = 5;

	public static int NUMERO_NEURONIOS_CAMADA_SAIDA = 4;

	public static int NUMERO_NEURONIOS_CAMADA_ESCONDIDA = 5;

	// Fim das constantes da MLP

	// Constantes do PSO
	
	public static int MAX_EPOCHS = 1000;
	
	public static int NUMBER_OF_PARTICLES = 50;
	
	public static double PROB_DEATH = 0.005;

	public static double ERRO_PARADA = 0.003;

	// Fim das constantes do PSO

	// Constantes do Fish

	public static double STEP_IND_INCIAL = 10.0; // Testaram com 10, 1 e 0.1 %
													// do espaco de busca

	public static double STEP_COLECTIVE_INCIAL = 0.1; // Testaram com 0.01,
														// 0.001 e 0.0001 % do
														// espaco de busca

	public static double STEP_IND_FINAL = 1.0;// Testaram com 10, 1 e 0.1 %
													// do espaco de busca

	public static double STEP_COLECTIVE_FINAL = 1.0; // Testaram com 0.01,
															// 0.001 e 0.0001 %
															// do espaco de
															// busca

	public static double W_SCALE = 2500; // Peso maximo dos peixes; Metade do
											// numero de iteracoes usadas no
											// artigo original

	public static double W_MINIMUM = 100; // Peso minimo dos peixes

	public static int FISH_QUANTITY = 50; // Mesmo numero usado no artigo

	public static double NUMBER_OF_ITERATIONS = 2000; // Tambem retirado do artigo
													// original

	// Fim das constantes do Fish

	public static final int QUANTIDADE_PESOS = (Util.NUMERO_NEURONIOS_CAMADA_ENTRADA * Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA)
			+ (Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA * Util.NUMERO_NEURONIOS_CAMADA_SAIDA)
			+ Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA
			+ Util.NUMERO_NEURONIOS_CAMADA_SAIDA;

	/**
	 * Metodo para realizar a leitura do arquivo csv
	 * 
	 * @param caminhoArquivo
	 *            caminho do arquivo
	 * @param padrao
	 *            recebe o padrao para realizar a quebra do csv (",") ou (";")
	 */
	public static ArrayList<String[]> leituraCSV(String caminhoArquivo,
			String padrao) throws IOException {

		ArrayList<String[]> arquivoRetorno = new ArrayList<String[]>();

		BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));

		String line = "";

		String[] row = null;

		while ((line = br.readLine()) != null) {
			arquivoRetorno.add(line.split(padrao));
		}

		return arquivoRetorno;

	}

	/**
	 * Metodo para normalizar os dados
	 * 
	 * @param caminhoArquivo
	 *            caminho do arquivo
	 * @param padrao
	 *            recebe o padrao para realizar a quebra do csv (",") ou (";")
	 */
	public static double[] normaliza(double[] vec, double lower, double upper) {
		double[] normalized = new double[vec.length];

		double max = max(vec);
		double min = min(vec);
		for (int i = 0; i < normalized.length; i++) {
			normalized[i] = (vec[i] - min) * (upper - lower) / (max - min)
					+ lower;
		}

		return normalized;
	}

	/**
	 * Calculates the minimum value of <code>array</code>
	 * 
	 * @param array
	 *            the array of values
	 * @return the minimum value of <code>array</code>
	 */
	public static double min(double[] signal) {
		double min = Double.MAX_VALUE;

		for (int i = 0; i < signal.length; i++)
			if (signal[i] < min)
				min = signal[i];

		return min;
	}

	/**
	 * Calculates the maximum value of <code>array</code>
	 * 
	 * @param array
	 *            the array of values
	 * @return the maximum value of <code>array</code>
	 */
	public static double max(double[] signal) {
		double max = -Double.MAX_VALUE;

		for (int i = 0; i < signal.length; i++)
			if (signal[i] > max)
				max = signal[i];

		return max;
	}

	/**
	 * Calculates the mean value of <code>array</code>
	 * 
	 * @param array
	 *            the array of values
	 * @return the mean value of <code>array</code>
	 */
	public static double mean(double[] array) {
		double sum = 0.0;

		for (int i = 0; i < array.length; i++)
			sum += array[i];

		return (sum / array.length);
	}

	/**
	 * Calculates the standar deviation of values in <code>array</code>
	 * 
	 * @param array
	 *            the array of values
	 * @return the standard deviation
	 */
	public static double stdev(double[] serie) {
		double sd = 0.0;
		double mean = mean(serie);

		for (int i = 0; i < serie.length; i++)
			sd += (serie[i] - mean) * (serie[i] - mean);

		return Math.sqrt(sd / serie.length);
	}

	public static int getMinPos(double[] list) {
		int pos = 0;
		double minValue = list[0];

		for (int i = 0; i < list.length; i++) {
			if (list[i] < minValue) {
				pos = i;
				minValue = list[i];
			}
		}

		return pos;
	}

	public static int maxIndex(double[] array) {
		int maxIndex = 0;
		for (int i = 1; i < array.length; i++) {
			double newnumber = array[i];
			if ((newnumber > array[maxIndex])) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	public static int maxIndex(int[] array) {
		int maxIndex = 0;
		for (int i = 1; i < array.length; i++) {
			int newnumber = array[i];
			if ((newnumber > array[maxIndex])) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}

}
