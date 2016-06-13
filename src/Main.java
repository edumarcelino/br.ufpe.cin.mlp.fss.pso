import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import MLP.MLPHibrida;

import util.Util;

public class Main {

	// Parametros
	private static int iteracoes = 1;

	private static double taxaAprendizagem;

	public static void main(String[] args) {

		ArrayList<String[]> datasetCarregado = new ArrayList<String[]>();

		// Realiza a leitura do csv para um array
		try {

			datasetCarregado = Util
					.leituraCSV(
							"C:/Users/c087956/workspace_luna/br.ufpe.cin.mlp.fss.pso/src/dataset/iris_mapped.csv",
							",");

		} catch (IOException e1) {
			System.out.println("Erro no carregamento do arquivo: "
					+ e1.toString());
		}

		System.out
				.println("-----------------REDE NEURAL - MLP-----------------");
		System.out.println("TAXA DE APRENDIZAGEM = " + taxaAprendizagem);
		System.out.println("QUANTIDADE DE ITERACOES = " + iteracoes + "\n");

		MLPHibrida mlp = new MLPHibrida(Util.NUMERO_NEURONIOS_CAMADA_ENTRADA,
				Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA,
				Util.NUMERO_NEURONIOS_CAMADA_SAIDA);

		// Inicio do treinamento
		System.out.println("Training...");
		System.out.println("TAMANHO DATASET: " + datasetCarregado.size());

		// Embaralhando o dataset
		Collections.shuffle(datasetCarregado);

		// Quantidade de linhas para treinar com 90% dos dados
		int qtdLinhasNoventaPorcento = (int) Math
				.round(datasetCarregado.size() * 0.1);

		ArrayList<String[]> datasetTreino = new ArrayList<String[]>();
		ArrayList<String[]> datasetTeste = new ArrayList<String[]>();

		// Inicializa o dataset de treino
		for (int i = 0; i < qtdLinhasNoventaPorcento; i++) {
			datasetTreino.add(datasetCarregado.get(i));
		}

		// Inicializa o dataset de teste
		for (int i = qtdLinhasNoventaPorcento; i < datasetCarregado.size(); i++) {
			datasetTeste.add(datasetCarregado.get(i));
		}

		int quantidadeClasses = retornaQuantidadeClasses(datasetCarregado);

		 
		for (Iterator iterator = datasetTreino.iterator(); iterator.hasNext();) {

			
			
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

		
		
		String[] linhaTempTeste = (String[]) datasetTreino.get(0);

		// TODO: Tentar transformar de forma generica

		// Converte a linha do dataset para treinar a rede MLP
		double[] padrao = new double[4];
		padrao[0] = Double.parseDouble(linhaTempTeste[0]);
		padrao[1] = Double.parseDouble(linhaTempTeste[1]);
		padrao[2] = Double.parseDouble(linhaTempTeste[2]);
		padrao[3] = Double.parseDouble(linhaTempTeste[3]);

		// Converte a saida esperada para o treinamento
		double[] saidaEsperada = new double[3];
		saidaEsperada[0] = Double.parseDouble(linhaTempTeste[4]);
		saidaEsperada[1] = Double.parseDouble(linhaTempTeste[5]);
		saidaEsperada[2] = Double.parseDouble(linhaTempTeste[6]);

		double[] saidaRede = mlp.apresentaPadrao(padrao);

		System.out.println("SAIDA ESPERADA: " + saidaEsperada[0] + " - "
				+ saidaEsperada[1] + " - " + saidaEsperada[2]);

		System.out.println("SAIDA DA MLP: " + saidaRede[1] + " - "
				+ saidaRede[2] + " - " + saidaRede[3]);

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