/**
 * 
 */
package de.dirkherrling.geneticTest.fitness;

import java.awt.Color;

import de.dirkherrling.geneticTest.Main;
import de.dirkherrling.geneticTest.model.Genome;

/**
 * @author dherrling
 *
 */
public class Fitness extends Thread {
	
	private Genome genome;
	
	public Fitness(Genome genome) {
		this.genome = genome;
	}
	
	
	public void run() {
		Main.increaseThreadCount();
		getFitnessValue(this.genome);
		Main.decreaseThreadCount();
//		getFitnessValue(candidateGenome);
	}
	
	public static long getFitnessValue(Genome candidateGenome) {
		if (!candidateGenome.isPhenoTypeValid()) {
			return Long.MAX_VALUE;
		} else {
			long error = 0;
			for (int x = 0; x < Main.getWidth(); x++) {
				for (int y = 0; y < Main.getHeight(); y++) {
					if (Boolean.valueOf(Main.getProperties().getProperty("altFitnessAlgo"))) {
						Color candidateColor = new Color(candidateGenome.getPhenoType().getRGB(x, y));
//						Color originalColor = Main.getPhenotypeColors()[x][y];
						if (Boolean.valueOf(Main.getProperties().getProperty("squaredError"))) {
							error += Util.colorDistanceSquared(Main.getPhenoRs()[x][y], Main.getPhenoGs()[x][y], Main.getPhenoBs()[x][y], candidateColor);
						} else {
//							error += Util.colorDistance(originalColor, candidateColor);
							error += Util.colorDistance(Main.getPhenoRs()[x][y], Main.getPhenoGs()[x][y], Main.getPhenoBs()[x][y], candidateColor);
						}
					} else {
						Color originalColor = new Color(Main.getPrototypeImage().getRGB(x, y));
						Color candidateColor = new Color(candidateGenome.getPhenoType().getRGB(x, y));
						if (Boolean.valueOf(Main.getProperties().getProperty("squaredError"))) {
							error += Util.colorDistanceSquared(originalColor, candidateColor); 
//							error += Util.colorDistanceSquared(Main.getPhenoRs()[x][y], Main.getPhenoGs()[x][y], Main.getPhenoBs()[x][y], candidateColor);
						} else {
							error += Util.colorDistance(originalColor, candidateColor);
						}
					}
					
				}
			}
			candidateGenome.setFitness(error);
			return error;
		}
	}

}
