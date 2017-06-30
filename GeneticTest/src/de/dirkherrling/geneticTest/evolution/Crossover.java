/**
 * 
 */
package de.dirkherrling.geneticTest.evolution;

import de.dirkherrling.geneticTest.model.Circle;
import de.dirkherrling.geneticTest.model.Genome;

/**
 * @author dherrling
 *
 */
public class Crossover {
	
	public static Genome cross(Genome mother, Genome father) {
//		System.out.println(mother.getCircles().size() + " " + father.getCircles().size());
		Genome result = new Genome(mother.getOwner(), 0);
		result.setFitnessValid(false);
		result.setPhenoTypeValid(false);
		
		for (int i = 0; i < mother.getCircles().size(); i++) {
			if (mother.getCircles().size() != father.getCircles().size()) {
				System.out.println("Mother and father had different genome sizes. I'm going to explode: " + mother.getCircles().size() + " " + father.getCircles().size());
			}
			if (Circle.getRand().nextBoolean()) {
				result.getCircles().add(mother.getCircles().get(i));
			} else {
//				System.out.println(father.getCircles().size());
				result.getCircles().add(father.getCircles().get(i));
			}
		}
		
		return result;
	}

}
