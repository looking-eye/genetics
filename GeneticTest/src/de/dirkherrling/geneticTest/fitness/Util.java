/**
 * 
 */
package de.dirkherrling.geneticTest.fitness;

import java.awt.Color;
import java.util.Vector;

import de.dirkherrling.geneticTest.model.Genome;

/**
 * @author dherrling
 *
 */
public class Util {

	public static int[] rgbToYUV(int r, int g, int b) {
		int[] result = new int[3];
		
		result[0] = Math.max((int) Math.round(0.299*r + 0.587*g + 0.114*b), 0);
		result[1] = Math.max((int) Math.round(-0.147*r + (-0.289*g) + 0.463*b), 0);
		result[2] = Math.max((int) Math.round(0.615*r + (-0.515*g) + (-0.1*b)), 0);
		
		return result;
	}
	
	public static long colorDistance(long r1, long g1, long b1, long r2, long g2, long b2) {
		long error = Math.abs(r2-r1);
		error += Math.abs(g2-g1);
		error += Math.abs(b2-b1);
		
		return error;
	}
	
	public static long colorDistance(Color c1, Color c2) {
		return colorDistance(c1.getRed(), c1.getGreen(), c1.getBlue(), c2.getRed(), c2.getGreen(), c2.getBlue());
	}
	
	public static long colorDistance(int r1, int g1, int b1, Color c2) {
		return colorDistance(r1, g1, b1, c2.getRed(), c2.getGreen(), c2.getBlue());
	}
	
	public static long colorDistanceSquared(long r1, long g1, long b1, long r2, long g2, long b2) {
		long error = 0L;
		
		long rErrorSquared = (r2-r1)*(r2-r1);
		long gErrorSquared = (g2-g1)*(g2-g1);
		long bErrorSquared = (b2-b1)*(b2-b1);
		
		error = rErrorSquared + gErrorSquared + bErrorSquared;
		
		return error;
	}
	
	public static long colorDistanceSquared(Color c1, Color c2) {
		return colorDistanceSquared(c1.getRed(), c1.getGreen(), c1.getBlue(), c2.getRed(), c2.getGreen(), c2.getBlue());
	}
	
	public static long colorDistanceSquared(int r, int g, int b, Color c2) {
		return colorDistanceSquared(r, g, b, c2.getRed(), c2.getGreen(), c2.getBlue());
	}
	
	public static Vector<Genome> sortByFitness(Vector<Genome> genomes) {
		Vector<Genome> result = new Vector<>();
		
		while(!genomes.isEmpty()) {
			Genome winner = genomes.get(0);
			for(Genome g : genomes) {
				if (g.getFitness() < winner.getFitness()) {
					winner = g;
				}
			}
			result.add(winner);
			genomes.remove(winner);
//			System.out.println(winner.getFitness());
		}
		
		return result;
	}
	
}
