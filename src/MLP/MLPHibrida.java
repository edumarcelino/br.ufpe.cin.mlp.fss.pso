package MLP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import FSS.FSS;
import dataset.Dataset;
import pso.PSO;
import pso.Particle;
import pso.Particula;
import util.Util;

public class MLPHibrida {

	public static final String TREINAMENTO_BACK_PROPAGATION = "BACK PROPAGATION";
	public static final String TREINAMENTO_PARTICLE_SWARM_OPTIMIZATION = "PARTICLE SWARM OPTIMIZATION";
	public static final String TREINAMENTO_FISH_SCHOOL_SEARCH = "FISH SCHOOL SEARCH";

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

		pesosCamadaEntradaEscondida = new double[numeroNeuroniosEscondida + 1][numeroNeuroniosEntrada + 1];
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

		for (int j = 0; j < numeroNeuroniosSaida; j++) {
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
	public double[] treinamento(ArrayList<String[]> dtTreino,
			String tipoTreinamento) {

		
		if (tipoTreinamento.equals(TREINAMENTO_BACK_PROPAGATION)) {

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

				// Apresenta o padr�o para a rede neural
				double[] saidaRede = apresentaPadrao(padrao);

				// Executa o backpropagation
				backPropagation(saidaEsperada);
			}

		} else if (tipoTreinamento.equals(TREINAMENTO_FISH_SCHOOL_SEARCH)) {

			double[] pesosFSS = new double[Util.QUANTIDADE_PESOS];
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

				// Apresenta o padr�o para a rede neural
				//double[] saidaRede = apresentaPadrao(padrao);

				pesosFSS = fssTreinamento(saidaEsperada);
				this.setPesos(pesosFSS);
				
				
			}
			return pesosFSS;

		} else if (tipoTreinamento
				.equals(TREINAMENTO_PARTICLE_SWARM_OPTIMIZATION)) {

			int numParticles = 12;
			int maxEpochs = 700;
			double exitError = 0.060;
			double probDeath = 0.005;

			double[] bestWeights = treino(numParticles, maxEpochs, exitError,
					probDeath);

			return bestWeights;

		}
		return camadaEntrada;


	}

	private double[] fssTreinamento(double[] saidaDesejada) {

		FSS fss = new FSS();

		return fss.centralExecution();

	}

	private double[] psoTreinamento(double[] saidaEsperada, double[] saidaRede,
			int tamanhoDatasetTreino, PSO pso) {

		int i = 0;
		for (Particula particula : pso.enxame) {

			double erro = 0;

			double[] novaVelocidade = pso.atualizaVelocidade(particula);

			particula.setVelocidade(novaVelocidade);

			double[] novaPosicao = pso.atualizaPosicao(particula,
					novaVelocidade);

			particula.setPosicao(novaPosicao);

			erro = pso.meanSquaredError(particula.getPosicao());

			particula.setErro(erro);

			System.out.println("IDENTIFICADOR PARTICULA: "
					+ particula.getIdentificadorParticula() + " ERRO: " + erro);

			if (particula.getErro() < pso.getBestGlobalError()) {

				System.out.println("#########################################");
				System.out.println("-----------------------------------------");
				System.out.println("bestGlobalError "
						+ pso.getBestGlobalError() + " erro " + erro);

				pso.setBestGlobalError(erro);

				pso.setBestGlobalPosition(novaPosicao);

			} else {

			}

		}

		// System.out
		// .println("PSO BEST GLOBAL ERROR: " + pso.getBestGlobalError());
		// System.out.print("PSO BEST GLOBAL POSITION: ");
		//
		// for (int i = 0; i < pso.getBestGlobalPosition().length; i++) {
		// System.out.print(pso.getBestGlobalPosition()[i] + " ");
		// }
		// System.out
		// .println("---------------------------------------------------");

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
			for (int i = 0; i <= numeroNeuroniosEscondida; i++) {
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

	public double[] treino(int numParticles, int maxEpochs, double exitError,
			double probDeath) {
		// PSO version training. best weights stored into NN and returned
		// particle position == NN weights

		Random rnd = new Random(16); // 16 just gives nice demo

		int numWeights = (Util.NUMERO_NEURONIOS_CAMADA_ENTRADA * Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA)
				+ (Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA * Util.NUMERO_NEURONIOS_CAMADA_SAIDA)
				+ Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA
				+ Util.NUMERO_NEURONIOS_CAMADA_SAIDA;

		// use PSO to seek best weights
		int epoch = 0;
		double minX = -10.0; // for each weight. assumes data has been
								// normalized about 0
		double maxX = 10.0;
		double w = 0.729; // inertia weight
		double c1 = 1.49445; // cognitive/local weight
		double c2 = 1.49445; // social/global weight
		double r1, r2; // cognitive and social randomizations

		Particle[] swarm = new Particle[numParticles];

		double[] bestGlobalPosition = new double[numWeights];
		double bestGlobalError = Double.MAX_VALUE; // smaller values better

		for (int i = 0; i < swarm.length; ++i) {
			double[] randomPosition = new double[numWeights];
			for (int j = 0; j < randomPosition.length; ++j) {
				randomPosition[j] = (maxX - minX) * rnd.nextDouble() + minX;
			}

			double error = meanSquaredError(randomPosition);

			double[] randomVelocity = new double[numWeights];

			for (int j = 0; j < randomVelocity.length; ++j) {
				// double lo = -1.0 * Math.Abs(maxX - minX);
				// double hi = Math.Abs(maxX - minX);
				// randomVelocity[j] = (hi - lo) * rnd.NextDouble() + lo;
				double lo = 0.1 * minX;
				double hi = 0.1 * maxX;
				randomVelocity[j] = (hi - lo) * rnd.nextDouble() + lo;

			}
			swarm[i] = new Particle(randomPosition, error, randomVelocity,
					randomPosition, error); // last two are best-position and
											// best-error

			// does current Particle have global best position/solution?
			if (swarm[i].error < bestGlobalError) {
				bestGlobalError = swarm[i].error;

				for (int sw = 0; sw < swarm[i].position.length; sw++) {
					bestGlobalPosition[sw] = swarm[i].position[sw];
				}

			}
		}
		// initialization

		// Console.WriteLine("Entering main PSO weight estimation processing loop");

		// main PSO algorithm

		int[] sequence = new int[numParticles]; // process particles in random
												// order
		for (int i = 0; i < sequence.length; ++i) {
			sequence[i] = i;
		}

		while (epoch < maxEpochs) {
			if (bestGlobalError < exitError) {
				break;
			}

			double[] newVelocity = new double[numWeights]; // step 1
			double[] newPosition = new double[numWeights]; // step 2
			double newError; // step 3

			shuffle(sequence, rnd); // move particles in random sequence

			for (int pi = 0; pi < swarm.length; ++pi) // each Particle (index)
			{
				int i = sequence[pi];
				Particle currP = swarm[i]; // for coding convenience

				// 1. compute new velocity
				for (int j = 0; j < currP.velocity.length; ++j) // each x value
																// of the
																// velocity
				{
					r1 = rnd.nextDouble();
					r2 = rnd.nextDouble();

					// velocity depends on old velocity, best position of
					// parrticle, and
					// best position of any particle
					newVelocity[j] = (w * currP.velocity[j])
							+ (c1 * r1 * (currP.bestPosition[j] - currP.position[j]))
							+ (c2 * r2 * (bestGlobalPosition[j] - currP.position[j]));
				}

				for (int nv = 0; nv < newVelocity.length; nv++) {
					currP.velocity[nv] = newVelocity[nv];
				}

				// 2. use new velocity to compute new position
				for (int j = 0; j < currP.position.length; ++j) {
					newPosition[j] = currP.position[j] + newVelocity[j]; // compute
																			// new
																			// position
					if (newPosition[j] < minX) // keep in range
						newPosition[j] = minX;
					else if (newPosition[j] > maxX)
						newPosition[j] = maxX;
				}

				for (int np = 0; np < newPosition.length; np++) {
					currP.position[np] = newPosition[np];
				}

				// 2b. optional: apply weight decay (large weights tend to
				// overfit)

				// 3. use new position to compute new error
				// newError = MeanCrossEntropy(trainData, newPosition); // makes
				// next check a bit cleaner
				newError = meanSquaredError(newPosition);
				currP.error = newError;

				if (newError < currP.bestError) // new particle best?
				{

					for (int npb = 0; npb < newPosition.length; npb++) {
						currP.bestPosition[npb] = newPosition[npb];
					}
					currP.bestError = newError;
				}

				if (newError < bestGlobalError) // new global best?
				{
					for (int bgp = 0; bgp < newPosition.length; bgp++) {
						bestGlobalPosition[bgp] = newPosition[bgp];
					}

					bestGlobalError = newError;
				}

				// 4. optional: does curr particle die?
				double die = rnd.nextDouble();
				if (die < probDeath) {
					// new position, leave velocity, update error
					for (int j = 0; j < currP.position.length; ++j)
						currP.position[j] = (maxX - minX) * rnd.nextDouble()
								+ minX;
					currP.error = meanSquaredError(currP.position);

					for (int z = 0; z < currP.position.length; z++) {
						currP.bestPosition[z] = currP.position[z];
					}

					currP.bestError = currP.error;

					if (currP.error < bestGlobalError) // global best by chance?
					{
						bestGlobalError = currP.error;
						for (int g = 0; g < bestGlobalPosition.length; g++) {
							bestGlobalPosition[g] = currP.position[g];
						}

					}
				}

			} // each Particle

			++epoch;

		} // while

		this.setPesos(bestGlobalPosition); // best position is a set of weights
		double[] retResult = new double[numWeights];

		for (int rt = 0; rt < bestGlobalPosition.length; rt++) {
			retResult[rt] = bestGlobalPosition[rt];
		}

		return retResult;

	} // Train

	public static double meanSquaredError(double[] pesos) {

		MLPHibrida mlpHibrida = new MLPHibrida(
				Util.NUMERO_NEURONIOS_CAMADA_ENTRADA,
				Util.NUMERO_NEURONIOS_CAMADA_ESCONDIDA,
				Util.NUMERO_NEURONIOS_CAMADA_SAIDA);

		Dataset dataset = new Dataset();

		ArrayList<String[]> dtTreino = dataset.getDatasetTreino();

		int tamanhoBaseTreinamento = dtTreino.size();

		double sumSquaredError = 0.0;

		mlpHibrida.setPesos(pesos);

		for (Iterator iterator = dtTreino.iterator(); iterator.hasNext();) {

			String[] linha = (String[]) iterator.next();

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

			double[] saidaRede = mlpHibrida.apresentaPadrao(padrao);

			for (int i = 0; i < tamanhoBaseTreinamento; ++i) {

				for (int j = 0; j < saidaEsperada.length; ++j) {

					sumSquaredError += ((saidaRede[j] - saidaEsperada[j]) * (saidaRede[j] - saidaEsperada[j]));

				}
			}
		}
		return sumSquaredError / tamanhoBaseTreinamento;
	}

	private static void shuffle(int[] sequence, Random rnd) {
		for (int i = 0; i < sequence.length; ++i) {
			int r = next(i, sequence.length);
			int tmp = sequence[r];
			sequence[r] = sequence[i];
			sequence[i] = tmp;
		}
	}

	public static int next(int min, int max) {
		Random random = new Random();
		int randomNumber = random.nextInt(max - min) + min;
		return randomNumber;
	}
}