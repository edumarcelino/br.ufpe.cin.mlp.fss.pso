package util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Util {

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
		
		for(int i=0; i<list.length; i++) {
			if(list[i] < minValue) {
				pos = i;
				minValue = list[i];
			}
		}
		
		return pos;
	}
		
}
