package dataset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import util.Util;

public class Dataset {

	ArrayList<String[]> datasetTreino = new ArrayList<String[]>();
	ArrayList<String[]> datasetTeste = new ArrayList<String[]>();

	public Dataset() {
		
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
		// Embaralhando o dataset
		Collections.shuffle(datasetCarregado);

		// Quantidade de linhas para treinar com 90% dos dados
		int qtdLinhasNoventaPorcento = (int) Math
				.round(datasetCarregado.size() * 0.2);

		// Inicializa o dataset de treino
		for (int i = 0; i < qtdLinhasNoventaPorcento; i++) {
			datasetTreino.add(datasetCarregado.get(i));
		}

		// Inicializa o dataset de teste
		for (int i = qtdLinhasNoventaPorcento; i < datasetCarregado.size(); i++) {
			datasetTeste.add(datasetCarregado.get(i));
		}


	}

	public ArrayList<String[]> getDatasetTreino() {
		return datasetTreino;
	}

	public void setDatasetTreino(ArrayList<String[]> datasetTreino) {
		this.datasetTreino = datasetTreino;
	}

	public ArrayList<String[]> getDatasetTeste() {
		return datasetTeste;
	}

	public void setDatasetTeste(ArrayList<String[]> datasetTeste) {
		this.datasetTeste = datasetTeste;
	}

}
