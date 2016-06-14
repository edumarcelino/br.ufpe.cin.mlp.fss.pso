package neuralnetwork;

import java.util.Arrays;

public class NeuralNetwork {
	// private static Random rnd; // for BP to initialize wts, in PSO
	private int numInput;
	private int numHidden;
	private int numOutput;
	private double[] inputs;
	private double[][] ihWeights; // input-hidden
	private double[] hBiases;
	private double[] hOutputs;
	private double[][] hoWeights; // hidden-output
	private double[] oBiases;
	private double[] outputs;

	public NeuralNetwork(int numInput, int numHidden, int numOutput) {

		this.numInput = numInput;

		this.numHidden = numHidden;

		this.numOutput = numOutput;

		this.inputs = new double[numInput];

		this.ihWeights = makeMatrix(numInput, numHidden);

		this.hBiases = new double[numHidden];

		this.hOutputs = new double[numHidden];

		this.hoWeights = makeMatrix(numHidden, numOutput);

		this.oBiases = new double[numOutput];

		this.outputs = new double[numOutput];
	}

	private static double[][] makeMatrix(int rows, int cols) // helper for ctor
	{
		double[][] result = new double[rows][];
		for (int r = 0; r < result.length; ++r)
			result[r] = new double[cols];
		return result;
	}

	public void setWeights(double[] weights) {

		// copy weights and biases in weights[] array to i-h weights, i-h
		// biases, h-o weights, h-o biases

		int numWeights = (numInput * numHidden) + (numHidden * numOutput)
				+ numHidden + numOutput;

		if (weights.length != numWeights) {
			new Exception("Bad weights array length: ");
		}

		int k = 0; // points into weights param

		for (int i = 0; i < numInput; ++i) {
			for (int j = 0; j < numHidden; ++j) {
				ihWeights[i][j] = weights[k++];
			}
		}

		for (int i = 0; i < numHidden; ++i) {
			hBiases[i] = weights[k++];
		}

		for (int i = 0; i < numHidden; ++i) {
			for (int j = 0; j < numOutput; ++j) {
				hoWeights[i][j] = weights[k++];
			}
		}

		for (int i = 0; i < numOutput; ++i) {
			oBiases[i] = weights[k++];
		}
	}

	public double[] getWeights() {
		// returns the current set of wweights, presumably after training
		int numWeights = (numInput * numHidden) + (numHidden * numOutput)
				+ numHidden + numOutput;

		double[] result = new double[numWeights];

		int k = 0;

		for (int i = 0; i < ihWeights.length; ++i) {
			for (int j = 0; j < ihWeights[0].length; ++j) {
				result[k++] = ihWeights[i][j];
			}

		}

		for (int i = 0; i < hBiases.length; ++i) {
			result[k++] = hBiases[i];
		}

		for (int i = 0; i < hoWeights.length; ++i) {
			for (int j = 0; j < hoWeights[0].length; ++j) {
				result[k++] = hoWeights[i][j];
			}

		}

		for (int i = 0; i < oBiases.length; ++i) {
			result[k++] = oBiases[i];
		}

		return result;
	}

	public double[] ComputeOutputs(double[] xValues) {
		if (xValues.length != numInput)
			new Exception("Bad xValues array length");

		double[] hSums = new double[numHidden]; // hidden nodes sums scratch
												// array
		double[] oSums = new double[numOutput]; // output nodes sums

		for (int i = 0; i < xValues.length; ++i)
			// copy x-values to inputs
			this.inputs[i] = xValues[i];

		for (int j = 0; j < numHidden; ++j)
			// compute i-h sum of weights * inputs
			for (int i = 0; i < numInput; ++i)
				hSums[j] += this.inputs[i] * this.ihWeights[i][j]; // note +=

		for (int i = 0; i < numHidden; ++i)
			// add biases to input-to-hidden sums
			hSums[i] += this.hBiases[i];

		for (int i = 0; i < numHidden; ++i)
			// apply activation
			this.hOutputs[i] = hyperTanFunction(hSums[i]); // hard-coded

		for (int j = 0; j < numOutput; ++j)
			// compute h-o sum of weights * hOutputs
			for (int i = 0; i < numHidden; ++i)
				oSums[j] += hOutputs[i] * hoWeights[i][j];

		for (int i = 0; i < numOutput; ++i)
			// add biases to input-to-hidden sums
			oSums[i] += oBiases[i];

		double[] softOut = softmax(oSums); // softmax activation does all
											// outputs at once for efficiency
		
		for(int i = 0; i < softOut.length; i++){
			outputs[i] = softOut[i];
		}
		
		//Array.Copy(softOut, outputs, softOut.length);

		double[] retResult = new double[numOutput]; // could define a GetOutputs
													// method instead
		
		for(int i = 0; i < softOut.length; i++){
			outputs[i] = softOut[i];
		}
		
		// TODO: Corrigir 
		//Array.Copy(this.outputs, retResult, retResult.length);

		return retResult;
	} // ComputeOutputs

	private static double hyperTanFunction(double x) {
		if (x < -20.0) {
			return -1.0; // approximation is correct to 30 decimals
		}

		else if (x > 20.0) {
			return 1.0;
		}

		else {
			return Math.tanh(x);
		}
	}

	private static double[] softmax(double[] oSums) {
		// does all output nodes at once so scale doesn't have to be re-computed
		// each time
		// determine max output sum
		double max = oSums[0];
		for (int i = 0; i < oSums.length; ++i)
			if (oSums[i] > max)
				max = oSums[i];

		// determine scaling factor -- sum of exp(each val - max)
		double scale = 0.0;
		for (int i = 0; i < oSums.length; ++i)
			scale += Math.exp(oSums[i] - max);

		double[] result = new double[oSums.length];
		for (int i = 0; i < oSums.length; ++i)
			result[i] = Math.exp(oSums[i] - max) / scale;

		return result; // now scaled so that xi sum to 1.0
	}

}