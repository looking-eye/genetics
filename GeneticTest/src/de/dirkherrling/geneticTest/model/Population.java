package de.dirkherrling.geneticTest.model;

import java.io.Serializable;
import java.util.Vector;

/**
 * 
 * @author dherrling
 *
 */
public class Population implements Serializable {

	private static final long serialVersionUID = -2737904288565296180L;
	private int populationSize;
	private Vector<Genome> genomes;
	private int genomeSize;
	
	public Population() {
		this.genomes = new Vector<>();
	}
	
	private void generateDNAs() {
		for (int i = 0; i < populationSize; i++) {
			this.genomes.add(new Genome(this, this.genomeSize));
		}
	}
	
	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public Vector<Genome> getGenomes() {
		return genomes;
	}

	public void setGenomes(Vector<Genome> genomes) {
		this.genomes = genomes;
	}

	public int getGenomeSize() {
		return genomeSize;
	}

	public void setGenomeSize(int size) {
		this.genomeSize = size;
		this.generateDNAs();
	}
	
}
