import MLP.MLPHibrida;
import dataset.Dataset;
import util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

	private static double taxaAprendizagem;

	public static String tipoTreinamento;

	private static List<double[]> acumuladorRede = new ArrayList<>();
	private static List<double[]> acumuladorEsperada = new ArrayList<>();

	public static void main(String[] args) {

		long tempoInicio = System.currentTimeMillis();
		MLPHibrida mlp;

		// Inicio do treinamento
		System.out.println("Training...");

		Dataset dataset = new Dataset();

		ArrayList<String[]> datasetCarregadoTeste = new ArrayList<String[]>();
		ArrayList<String[]> datasetCarregadoTreino = new ArrayList<String[]>();

		for (int execucao = 1; execucao <= 30; execucao++) {

			//			long tempoInicio = System.currentTimeMillis();
			double acuraciaMediaPorExecucao = 0.0;
			
			for (int fold = 1; fold <= 10; fold++) {
				try {
					datasetCarregadoTeste = Util.leituraCSV(
							"C:/Users/Eduardo/workspace/br.ufpe.cin.mlp.fss.pso/src/dataset/USERKNOWLEDGE/execucao_"
									+ execucao + "_fold_" + fold + "_TESTE.csv",
							",");
					datasetCarregadoTreino = Util.leituraCSV(
							"C:/Users/Eduardo/workspace/br.ufpe.cin.mlp.fss.pso/src/dataset/USERKNOWLEDGE/execucao_"
									+ execucao + "_fold_" + fold + "_TREINO.csv",
							",");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mlp = new MLPHibrida(Util.NUMERO_NEURONIOS_CAMADA_ENTRADA, Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA,
						Util.NUMERO_NEURONIOS_CAMADA_SAIDA);

				dataset.setDatasetTeste(datasetCarregadoTeste);
				dataset.setDatasetTreino(datasetCarregadoTreino);

				tipoTreinamento = mlp.TREINAMENTO_BACK_PROPAGATION;

				for (int epoca = 0; epoca < 500; epoca++) {
					
					// Treinamento para a rede neural
					double[] pesos = mlp.treinamento(datasetCarregadoTreino, tipoTreinamento, dataset);

					if (!tipoTreinamento.equals(mlp.TREINAMENTO_BACK_PROPAGATION)) {
						mlp.setPesos(pesos);
					}
				}

				double[] saidaEsperada;
				double[] padrao;
				double[] saidaRede;
				for (String[] dt : datasetCarregadoTeste) {

					// Converte a linha do dataset para treinar a rede MLP
					// Converte a linha do dataset para treinar a rede MLP
					padrao = new double[5];
					padrao[0] = Double.parseDouble(dt[0]);
					padrao[1] = Double.parseDouble(dt[1]);
					padrao[2] = Double.parseDouble(dt[2]);
					padrao[3] = Double.parseDouble(dt[3]);
					padrao[4] = Double.parseDouble(dt[4]);


					// Converte a saida esperada para o treinamento
					saidaEsperada = new double[4];
					saidaEsperada[0] = Double.parseDouble(dt[5]);
					saidaEsperada[1] = Double.parseDouble(dt[6]);
					saidaEsperada[2] = Double.parseDouble(dt[7]);
					saidaEsperada[3] = Double.parseDouble(dt[8]);

					acumuladorEsperada.add(saidaEsperada);

					saidaRede = mlp.apresentaPadrao(padrao);
					double[] saidaRedeTemp = new double[4];
					for (int i = 0; i < saidaRede.length; i++) {
						
						saidaRedeTemp[i] = saidaRede[i];
					}

					acumuladorRede.add(saidaRedeTemp);
				}
				double acuracia = accuracy(acumuladorEsperada, acumuladorRede);
				acuraciaMediaPorExecucao = acuraciaMediaPorExecucao + acuracia;
				//System.out.println("Execucao: " + execucao + " Fold: " + fold + " Acurácia: " + acuracia);
			}
//			System.out.println("Acuracia média da execucao " + execucao + " -> " + acuraciaMediaPorExecucao / 10);
			System.out.println(acuraciaMediaPorExecucao / 10);
//			System.out.println("Tempo da execucao " + execucao + ": " + (System.currentTimeMillis() - tempoInicio)
//					+ " milisegundos");

		}

		// System.out.println("########## DATASET TREINO ##########");

		// for (String[] dt : dtTreino) {
		//
		// // Converte a linha do dataset para treinar a rede MLP
		// double[] padrao = new double[4];
		// padrao[0] = Double.parseDouble(dt[0]);
		// padrao[1] = Double.parseDouble(dt[1]);
		// padrao[2] = Double.parseDouble(dt[2]);
		// padrao[3] = Double.parseDouble(dt[3]);
		//
		// // Converte a saida esperada para o treinamento
		// double[] saidaEsperada = new double[3];
		// saidaEsperada[0] = Double.parseDouble(dt[4]);
		// saidaEsperada[1] = Double.parseDouble(dt[5]);
		// saidaEsperada[2] = Double.parseDouble(dt[6]);
		//
		//
		// double[] saidaRede = mlp.apresentaPadrao(padrao);
		// }
		System.out.println((System.currentTimeMillis() - tempoInicio));
	}

	private static int retornaQuantidadeClasses(ArrayList<String[]> datasetCarregado) {
		int quantidadeClasse = 0;
		ArrayList<String> listTextoTemp = new ArrayList<String>();

		for (Iterator iterator = datasetCarregado.iterator(); iterator.hasNext();) {
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
		} else if (padrao[1] > padrao[2]) {
			retorno[0] = 0;
			retorno[1] = 1;
			retorno[2] = 0;
		} else {
			retorno[0] = 0;
			retorno[1] = 0;
			retorno[2] = 1;
		}

		return retorno;
	}

	public static double accuracy(List<double[]> saidaEsperada, List<double[]> saidaRede) {
		double numCorrect = 0.0;
		double numWrong = 0.0;
		for (int k = 0; k < saidaEsperada.size(); k++) {

			int maxIndex = Util.maxIndex(saidaRede.get(k));

			if (saidaEsperada.get(k)[maxIndex] == 1.0) {
				++numCorrect;
			} else {
				++numWrong;
			}

		}
		return (numCorrect) / (numCorrect + numWrong);
	}

}