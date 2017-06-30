package de.dirkherrling.geneticTest.model;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Vector;

import de.dirkherrling.geneticTest.Main;

/**
 * 
 * @author dherrling
 *
 */
public class Genome implements Serializable {
	
	private static final long serialVersionUID = 5340999326405557209L;
	private Vector<Circle> circles;
	private Population owner;
	private int genomeSize;
	private transient BufferedImage phenoType;
	private boolean phenoTypeValid = false;
	private long fitness = Long.MAX_VALUE;
	private boolean fitnessValid = false;
//	private int fitnes
	
	
	public Genome(Population owner, int size) {
		this.owner = owner;
		this.circles = new Vector<>();
		this.genomeSize = size;
		this.generateRandomCircles();
	}
	
	public Genome duplicate() {
		Genome result = new Genome(owner, 0);
		//circles
		for (Circle c : this.circles) {
			result.circles.add(c.duplicate());
		}
		result.genomeSize = this.genomeSize;
		result.phenoType = this.phenoType;
		result.phenoTypeValid = this.phenoTypeValid;
		result.fitness = this.fitness;
		result.fitnessValid = this.fitnessValid;
		return result;
	}
	
	public void generateRandomCircles() {
		for (int i = 0; i < genomeSize; i++) {
			this.circles.add(new Circle(Main.getWidth(), Main.getHeight()));
		}
	}
	

	public Vector<Circle> getCircles() {
		return circles;
	}

	public void setCircles(Vector<Circle> population) {
		this.circles = population;
	}

	public Population getOwner() {
		return owner;
	}

	public void setOwner(Population owner) {
		this.owner = owner;
	}

	public int getGenomeSize() {
		return genomeSize;
	}

	public void setGenomeSize(int genomeSize) {
		this.genomeSize = genomeSize;
	}

	public BufferedImage getPhenoType() {
		return phenoType;
	}

	public void setPhenoType(BufferedImage phenoType) {
		this.phenoType = phenoType;
	}

	public boolean isPhenoTypeValid() {
		return phenoTypeValid;
	}

	public void setPhenoTypeValid(boolean phoneTypeValid) {
		this.phenoTypeValid = phoneTypeValid;
	}

	public long getFitness() {
		return fitness;
	}

	public void setFitness(long fitness) {
		this.fitness = fitness;
		this.setFitnessValid(true);
	}

	public boolean isFitnessValid() {
		return fitnessValid;
	}

	public void setFitnessValid(boolean fitnessValid) {
		this.fitnessValid = fitnessValid;
	}
}
