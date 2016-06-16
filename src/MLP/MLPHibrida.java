package MLP;

import FSS.FSS;
import dataset.Dataset;
import pso.PSO;
import pso.Particula;
import util.Util;

public class MLPHibrida {

	public static final String TREINAMENTO_BACK_PROPAGATION = "backpropagation";
	public static final String TREINAMENTO_PARTICLE_SWARM_OPTIMIZATION = "pso";
	public static final String TREINAMENTO_FISH_SCHOOL_SEARCH = "fss";

	// Numero de neuronios nas camadas
	private int numeroNeuroniosEntrada;
	private int numeroNeuroniosEscondida;
	private int numeroNeuroniosSaida;

	private int qtdePesos;

	// Camadas da rede
	private double[] camadaEntrada;
	private double[] camadaEscondida;
	private double[] camadaSaida;

	// Pesos entre as camadas
	private double[][] pesosCamadaEntradaEscondida;
	private double[][] pesosCamadaEscondidaSaida;

	// Taxa de aprendizagem
	private double TAXA_APRENDIZAGEM = 0.5;

	/**
	 * Cria uma nova instancia da rede SimpleMLP.
	 * 
	 * @param numeroNeuroniosEntrada
	 *            numero de neuronios da camada de entrada
	 * @param numeroNeuroniosEscondida
	 *            numero de neuronios da camada escondida
	 * @param numeroNeuroniosSaida
	 *            numero de neuronios da camada de saida
	 */
	public MLPHibrida(int numeroNeuroniosEntrada, int numeroNeuroniosEscondida,
			int numeroNeuroniosSaida) {

		setNumeroNeuroniosEntrada(numeroNeuroniosEntrada);
		setNumeroNeuroniosEscondida(numeroNeuroniosEscondida);
		setNumeroNeuroniosSaida(numeroNeuroniosSaida);

		setQtdePesos(qtdePesos);

		// Inicializa as camadas da rede MLP
		// CamadaEntrada[0] -> bias
		// CamadaEscondida[0] -> bias
		camadaEntrada = new double[numeroNeuroniosEntrada + 1];
		camadaEscondida = new double[numeroNeuroniosEscondida + 1];
		camadaSaida = new double[numeroNeuroniosSaida];

		pesosCamadaEntradaEscondida = new double[numeroNeuroniosEscondida][numeroNeuroniosEntrada+1];
		pesosCamadaEscondidaSaida = new double[numeroNeuroniosSaida][numeroNeuroniosEscondida + 1];

		geraRandomicamentePesos();
	}

	public static void visualizaPesosRede() {

		// System.out.println("NEURONIO " + "[" + i + "]" + "[" + j + "]");
	}

	/**
	 * Seta a taxa de aprendizagem
	 * 
	 * @param taxaAprendizagem
	 *            taxa de aprendizagem para a rede
	 */
	public void setTaxaAprendizagem(double taxaAprendizagem) {
		TAXA_APRENDIZAGEM = taxaAprendizagem;
	}

	/**
	 * Gera os pesos randomicamente entre os valores 0.5 e -0.5
	 */
	private void geraRandomicamentePesos() {

		for (int j = 1; j <= numeroNeuroniosEscondida; j++) {
			for (int i = 0; i <= numeroNeuroniosEntrada; i++) {
				pesosCamadaEntradaEscondida[j][i] = Math.random() - 0.5;
			}
		}

		for (int j = 1; j < numeroNeuroniosSaida; j++) {
			for (int i = 0; i <= numeroNeuroniosEscondida; i++) {
				pesosCamadaEscondidaSaida[j][i] = Math.random() - 0.5;
			}
		}

	}

	/**
	 * Inicia o treinamento com o envio de um padrão e uma saida desejada
	 * 
	 * @param numeroNeuroniosEntrada
	 *            numero de neuronios da camada de entrada
	 * @param numeroNeuroniosEscondida
	 *            numero de neuronios da camada escondida
	 * @param numeroNeuroniosSaida
	 *            numero de neuronios da camada de saída
	 */
	public double[] treinamento(double[] padrao, double[] saidaDesejada,
			String tipoTreinamento) {

		if (tipoTreinamento.equals(TREINAMENTO_BACK_PROPAGATION)) {

			double[] saidaRede = apresentaPadrao(padrao);
			backPropagation(saidaDesejada);

		} else if (tipoTreinamento.equals(TREINAMENTO_FISH_SCHOOL_SEARCH)) {

			double[] pesosFSS = fssTreinamento(saidaDesejada);
			this.setPesos(pesosFSS);

		} else if (tipoTreinamento
				.equals(TREINAMENTO_PARTICLE_SWARM_OPTIMIZATION)) {

			int tamanhoDataset = new Dataset().getDatasetTreino().size();
			PSO pso = new PSO();

			// Inicializa 15 particulas no enxame do PSO.
			pso.inicializaEnxame(20);

			double[] pesosPSO = psoTreinamento(saidaDesejada, padrao,
					tamanhoDataset, pso);

			this.setPesos(pesosPSO);

		}

		return camadaSaida;
	}

	private double[] fssTreinamento(double[] saidaDesejada) {

		FSS fss = new FSS();

		return fss.centralExecution();

	}

	private double[] psoTreinamento(double[] saidaDesejada, double[] padrao,
			int tamanhoDataset, PSO pso) {

//		if (pso.bestGlobalError < Util.ERRO_PARADA_PSO) {
//			return pso.getBestGlobalPosition();
//		}

		// System.out.println("NUMERO_ITERACOES_PSO: " + numIteracoes);

		for (Particula particula : pso.enxame) {

			double erro = 0;

			double[] novaVelocidade = pso.atualizaVelocidade(particula);

			particula.setVelocidade(novaVelocidade);

			double[] novaPosicao = pso.atualizaPosicao(particula,
					novaVelocidade);

			particula.setPosicao(novaPosicao);

			erro = pso.meanSquaredError(padrao, saidaDesejada,
					particula.posicao, tamanhoDataset);

			if (erro < pso.bestGlobalError) {
				System.out.println("ERRO ANTES (bestGlobalError): "
						+ pso.bestGlobalError);
				System.out.println("ERRO DEPOIS (erro): " + erro);

				pso.setBestGlobalError(erro);
				pso.setBestGlobalPosition(novaPosicao);
			}

			particula.setErro(erro);

		}

		System.out
				.println("PSO BEST GLOBAL ERROR: " + pso.getBestGlobalError());
		System.out.print("PSO BEST GLOBAL POSITION: ");
		
		for (int i = 0; i < pso.getBestGlobalPosition().length; i++) {
			System.out.print(pso.getBestGlobalPosition()[i] + " ");
		}
		System.out
				.println("---------------------------------------------------");

		return pso.getBestGlobalPosition();
	}

	/**
	 * Passa um padrao para a rede. Utiliza a funcao de ativacao a logistica
	 * 
	 * @param padrao
	 *            que sera passada pela rede
	 * @return a saida da rede para o padrao
	 */
	public double[] apresentaPadrao(double[] padrao) {

		// A camada de entrada recebe o padrao para propagar na rede
		for (int i = 0; i < numeroNeuroniosEntrada; i++) {
			camadaEntrada[i + 1] = padrao[i];
		}

		// Seta o bias
		camadaEntrada[0] = 1.0;
		camadaEscondida[0] = 1.0;

		// Itera em todos os neuronios da camada escondida para repassar pela
		// camada escondida
		for (int j = 1; j <= numeroNeuroniosEscondida; j++) {
			camadaEscondida[j] = 0.0;
			for (int i = 0; i <= numeroNeuroniosEntrada; i++) {
				camadaEscondida[j] += pesosCamadaEntradaEscondida[j][i]
						* camadaEntrada[i];
			}
			camadaEscondida[j] = 1.0 / (1.0 + Math.exp(-camadaEscondida[j]));
		}

		// Itera em todos os neuronios da camada de saida para repassar pela
		// camada de saida
		for (int j = 0; j < numeroNeuroniosSaida; j++) {
			camadaSaida[j] = 0.0;
			for (int i = 0; i <= numeroNeuroniosEscondida; i++) {

				camadaSaida[j] += pesosCamadaEscondidaSaida[j][i]
						* camadaEscondida[i];

			}
			camadaSaida[j] = 1.0 / (1.0 + Math.exp(-camadaSaida[j]));
		}

		// Retorna a saida atraves da rede
		return camadaSaida;
	}

	/**
	 * Metodo para ajustar os pesos utilizando o algoritmo backPropagation. A
	 * saida é comparada com a saida da rede e os pesos sao ajustado de acordo
	 * com a taxa de aprendizagem.
	 * 
	 * @param saidaDesejada
	 *            saida desejada de acordo com o padrao apresentado
	 */
	private void backPropagation(double[] saidaDesejada) {

		double[] erroL2 = new double[numeroNeuroniosSaida];
		double[] erroL1 = new double[numeroNeuroniosEscondida + 1];

		double eSum = 0.0;

		for (int i = 0; i < numeroNeuroniosSaida; i++) {

			erroL2[i] = camadaSaida[i] * (1.0 - camadaSaida[i])
					* (saidaDesejada[i] - camadaSaida[i]);
		}

		for (int i = 0; i <= numeroNeuroniosEscondida; i++) {
			// Layer 1 error gradient

			for (int j = 0; j < numeroNeuroniosSaida; j++) {
				eSum += pesosCamadaEscondidaSaida[j][i] * erroL2[j];
			}

			erroL1[i] = camadaEscondida[i] * (1.0 - camadaEscondida[i]) * eSum;
			eSum = 0.0;
		}

		for (int j = 0; j < numeroNeuroniosSaida; j++) {
			for (int i = 0; i <= numeroNeuroniosEscondida; i++) {
				pesosCamadaEscondidaSaida[j][i] += TAXA_APRENDIZAGEM
						* erroL2[j] * camadaEscondida[i];
			}
		}

		for (int j = 1; j <= numeroNeuroniosEscondida; j++) {
			for (int i = 0; i <= numeroNeuroniosEntrada; i++) {
				pesosCamadaEntradaEscondida[j][i] += TAXA_APRENDIZAGEM
						* erroL1[j] * camadaEntrada[i];
			}
		}
	}

	public void setPesos(double[] pesos) {
		int k = 0;
		for (int j = 1; j <= numeroNeuroniosEscondida; j++) {
			for (int i = 0; i <= numeroNeuroniosEntrada; i++) {
				pesosCamadaEntradaEscondida[j][i] = pesos[k++];
			}
		}

		for (int j = 0; j < numeroNeuroniosSaida; j++) {
			for (int i = 1; i <= numeroNeuroniosEscondida; i++) {
				pesosCamadaEscondidaSaida[j][i] = pesos[k++];
			}
		}

	}

	public int getNumeroNeuroniosEntrada() {
		return numeroNeuroniosEntrada;
	}

	public void setNumeroNeuroniosEntrada(int numeroNeuroniosEntrada) {
		this.numeroNeuroniosEntrada = numeroNeuroniosEntrada;
	}

	public int getNumeroNeuroniosEscondida() {
		return numeroNeuroniosEscondida;
	}

	public void setNumeroNeuroniosEscondida(int numeroNeuroniosEscondida) {
		this.numeroNeuroniosEscondida = numeroNeuroniosEscondida;
	}

	public int getNumeroNeuroniosSaida() {
		return numeroNeuroniosSaida;
	}

	public void setNumeroNeuroniosSaida(int numeroNeuroniosSaida) {
		this.numeroNeuroniosSaida = numeroNeuroniosSaida;
	}

	public double[][] getPesosCamadaEntradaEscondida() {
		return pesosCamadaEntradaEscondida;
	}

	public void setPesosCamadaEntradaEscondida(
			double[][] pesosCamadaEntradaEscondida) {
		this.pesosCamadaEntradaEscondida = pesosCamadaEntradaEscondida;
	}

	public double[][] getPesosCamadaEscondidaSaida() {
		return pesosCamadaEscondidaSaida;
	}

	public void setPesosCamadaEscondidaSaida(
			double[][] pesosCamadaEscondidaSaida) {
		this.pesosCamadaEscondidaSaida = pesosCamadaEscondidaSaida;
	}

	public int getQtdePesos() {
		return qtdePesos;
	}

	public void setQtdePesos(int qtdePesos) {
		this.qtdePesos = qtdePesos;
	}

}