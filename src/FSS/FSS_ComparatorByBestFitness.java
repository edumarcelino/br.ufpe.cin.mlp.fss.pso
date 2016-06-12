package FSS;

import java.util.Comparator;

/*
 * Copyright (c) 2011 Murilo Rebelo Pontes
 * murilo.pontes@gmail.com
 * 
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */

public class FSS_ComparatorByBestFitness implements Comparator<Fish>{

	@Override
	public int compare(Fish o1, Fish o2) {
		if (o1.best.fitness<o2.best.fitness) return -1;
		if (o1.best.fitness>o2.best.fitness) return 1;
		return 0;
	}

}
