import java.util.ArrayList;
import java.util.Iterator;

import util.Util;
import MLP.MLPHibrida;
import dataset.Dataset;

public class Main {

	// Parametros
	private static int iteracoes = 1;

	private static double taxaAprendizagem;

	public static void main(String[] args) {

		System.out
				.println("-----------------REDE NEURAL - MLP-----------------");
		System.out.println("TAXA DE APRENDIZAGEM = " + taxaAprendizagem);
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

		for (int i = 0; i < 200; i++) {
			for (Iterator iterator = dtTreino.iterator(); iterator.hasNext();) {

				String[] linha = (String[]) iterator.next();

				// TODO: Tentar transformar de forma generica

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

				
				// Treinamento para a rede neural
				mlp.treinamento(padrao, saidaEsperada,
						mlp.TREINAMENTO_PARTICLE_SWARM_OPTIMIZATION);

			}
		}

		for (String[] dt : dtTeste) {

			// TODO: Tentar transformar de forma generica

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

			System.out.println("");
			System.out.println("SAIDA ESPERADA: " + saidaEsperada[0] + " - "
					+ saidaEsperada[1] + " - " + saidaEsperada[2]);

			System.out.println("SAIDA DA MLP: " + saidaRede[1] + " - "
					+ saidaRede[2] + " - " + saidaRede[3]);
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

}