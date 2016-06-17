import java.util.ArrayList;
import java.util.Iterator;

import util.Util;
import MLP.MLPHibrida;
import dataset.Dataset;

public class Main {

	// Parametros
	private static int iteracoes = 2;

	private static double taxaAprendizagem;

	public static String tipoTreinamento;
	
	public static void main(String[] args) {

		System.out
				.println("-----------------REDE NEURAL - MLP-----------------");
		System.out.println("QUANTIDADE DE ITERACOES = " + iteracoes + "\n");

		MLPHibrida mlp = new MLPHibrida(Util.NUMERO_NEURONIOS_CAMADA_ENTRADA,
				Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA,
				Util.NUMERO_NEURONIOS_CAMADA_SAIDA);

		// Inicio do treinamento
		System.out.println("Training...");

		Dataset dataset = new Dataset();

		ArrayList<String[]> dtTeste = dataset.getDatasetTeste();
		ArrayList<String[]> dtTreino = dataset.getDatasetTreino();

		System.out.println("DATASET TREINO: " + dtTreino.size());
		System.out.println("DATASET TESTE: " + dtTeste.size());

		for (int i = 0; i < iteracoes; i++) {

			tipoTreinamento = mlp.TREINAMENTO_PARTICLE_SWARM_OPTIMIZATION;
			
			// Treinamento para a rede neural
			double[] pesos = mlp.treinamento(dtTreino,
					tipoTreinamento);

			if(!tipoTreinamento.equals(mlp.TREINAMENTO_BACK_PROPAGATION)){
				mlp.setPesos(pesos);
			}

		}

		
		for (String[] dt : dtTeste) {

			// Converte a linha do dataset para treinar a rede MLP
			double[] padrao = new double[4];
			padrao[0] = Double.parseDouble(dt[0]);
			padrao[1] = Double.parseDouble(dt[1]);
			padrao[2] = Double.parseDouble(dt[2]);
			padrao[3] = Double.parseDouble(dt[3]);

			// Converte a saida esperada para o treinamento
			double[] saidaEsperada = new double[3];
			saidaEsperada[0] = Double.parseDouble(dt[4]);
			saidaEsperada[1] = Double.parseDouble(dt[5]);
			saidaEsperada[2] = Double.parseDouble(dt[6]);

			
			
			double[] saidaRede = mlp.apresentaPadrao(padrao);

			System.out.println("PADRAO ENTRADA: " + padrao[0] + "," + padrao[1]
					+ "," + padrao[2] + "," + padrao[3]);
			System.out.println("SAIDA ESPERADA: " + saidaEsperada[0] + " - "
					+ saidaEsperada[1] + " - " + saidaEsperada[2]);

			double[] saidaRedeModificada = retornaRede(saidaRede);
			System.out.println("SAIDA DA MLP: " + saidaRedeModificada[0]
					+ " - " + saidaRedeModificada[1] + " - "
					+ saidaRedeModificada[2]);

			System.out.println("SAIDA DA MLP: " + saidaRede[0] + " - "
					+ saidaRede[1] + " - " + saidaRede[2]);
			
			System.out.println("");
		}

	}

	private static int retornaQuantidadeClasses(
			ArrayList<String[]> datasetCarregado) {
		int quantidadeClasse = 0;
		ArrayList<String> listTextoTemp = new ArrayList<String>();

		for (Iterator iterator = datasetCarregado.iterator(); iterator
				.hasNext();) {
			String[] datasetTemp = (String[]) iterator.next();

			if (!listTextoTemp.contains(datasetTemp[datasetTemp.length - 1])) {
				listTextoTemp.add(datasetTemp[datasetTemp.length - 1]);
			}
		}

		quantidadeClasse = listTextoTemp.size();

		return quantidadeClasse;
	}

	public static double[] retornaRede(double[] padrao) {
		double[] retorno = new double[3];
		if (padrao[0] > padrao[1] && padrao[0] > padrao[2]) {
			retorno[0] = 1;
			retorno[1] = 0;
			retorno[2] = 0;
		}

		else if (padrao[1] > padrao[2]) {
			retorno[0] = 0;
			retorno[1] = 1;
			retorno[2] = 0;
		}

		else {
			retorno[0] = 0;
			retorno[1] = 0;
			retorno[2] = 1;
		}

		return retorno;
	}

}