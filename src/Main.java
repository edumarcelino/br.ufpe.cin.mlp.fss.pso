import MLP.MLPHibrida;
import dataset.Dataset;
import util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    // Parametros
    private static int iteracoes = 1;

    private static double taxaAprendizagem;

    public static String tipoTreinamento;

    private static List<double[]> acumuladorRede = new ArrayList<>();
    private static List<double[]> acumuladorEsperada = new ArrayList<>();

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

            tipoTreinamento = mlp.TREINAMENTO_FISH_SCHOOL_SEARCH;

            // Treinamento para a rede neural
            double[] pesos = mlp.treinamento(dtTreino,
                    tipoTreinamento);

            if (!tipoTreinamento.equals(mlp.TREINAMENTO_BACK_PROPAGATION)) {
                mlp.setPesos(pesos);
            }

        }

        System.out.println("TIPO DE TREINAMENTO: " + tipoTreinamento);

//        System.out.println("########## DATASET TREINO ##########");

//        for (String[] dt : dtTreino) {
//
//            // Converte a linha do dataset para treinar a rede MLP
//            double[] padrao = new double[4];
//            padrao[0] = Double.parseDouble(dt[0]);
//            padrao[1] = Double.parseDouble(dt[1]);
//            padrao[2] = Double.parseDouble(dt[2]);
//            padrao[3] = Double.parseDouble(dt[3]);
//
//            // Converte a saida esperada para o treinamento
//            double[] saidaEsperada = new double[3];
//            saidaEsperada[0] = Double.parseDouble(dt[4]);
//            saidaEsperada[1] = Double.parseDouble(dt[5]);
//            saidaEsperada[2] = Double.parseDouble(dt[6]);
//
//
//            double[] saidaRede = mlp.apresentaPadrao(padrao);
//        }

        System.out.println("########## DATASET TESTE ##########");

        double[] saidaEsperada;
        double[] padrao;
        double[] saidaRede;
        for (String[] dt : dtTeste) {

            // Converte a linha do dataset para treinar a rede MLP
            padrao = new double[4];
            padrao[0] = Double.parseDouble(dt[0]);
            padrao[1] = Double.parseDouble(dt[1]);
            padrao[2] = Double.parseDouble(dt[2]);
            padrao[3] = Double.parseDouble(dt[3]);

            // Converte a saida esperada para o treinamento
            saidaEsperada = new double[3];
            saidaEsperada[0] = Double.parseDouble(dt[4]);
            saidaEsperada[1] = Double.parseDouble(dt[5]);
            saidaEsperada[2] = Double.parseDouble(dt[6]);
            acumuladorEsperada.add(saidaEsperada);


            saidaRede = mlp.apresentaPadrao(padrao);
            double [] saidaRedeTemp = new double[3];
            for(int i=0; i <saidaRede.length; i++){
                saidaRedeTemp[i] = saidaRede[i];
            }

            acumuladorRede.add(saidaRedeTemp);
        }
        System.out.print(accuracy(acumuladorEsperada, acumuladorRede));

    }

    private static int retornaQuantidadeClasses(
            ArrayList<String[]> datasetCarregado) {
        int quantidadeClasse = 0;
        ArrayList<String> listTextoTemp = new ArrayList<String>();

        for (Iterator iterator = datasetCarregado.iterator(); iterator
                .hasNext(); ) {
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
        System.out.println(numCorrect);
        System.out.println(numWrong);
        return (numCorrect) / (numCorrect + numWrong);
    }


}