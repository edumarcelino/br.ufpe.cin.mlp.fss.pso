package FSS;

/*
 * Copyright (c) 2011 Murilo Rebelo Pontes
 * murilo.pontes@gmail.com
 * 
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */


import java.util.concurrent.ThreadLocalRandom;

public class FSS_Solution {

	public double[] variables = null;
	public double fitness = Double.NaN;

	public FSS_Solution(int dims) {
		variables = new double[dims];
	}
	public static void copy(FSS_Solution source,FSS_Solution destination){

		destination.fitness = source.fitness;

		for(int i=0;i<source.variables.length;i++){
			destination.variables[i]=source.variables[i];
		}
	}
	public void randomize_variables(){
		for(int i=0;i<variables.length;i++){
			variables[i]= ThreadLocalRandom.current().nextDouble(0,1.1); // TODO Avaliar esse random de pesos
		}
	}
}
