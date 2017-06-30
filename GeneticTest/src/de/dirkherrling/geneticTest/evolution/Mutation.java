package de.dirkherrling.geneticTest.evolution;

import de.dirkherrling.geneticTest.Main;
import de.dirkherrling.geneticTest.model.Circle;
import de.dirkherrling.geneticTest.model.Genome;

public class Mutation {

	private static double mutationProbability = Double.valueOf(Main.getProperties().getProperty("mutationProbability"));		//originally: 0.01
	private static int maxChangeColor = Integer.valueOf(Main.getProperties().getProperty("maxChangeColor"));
	private static int maxChangeDiameter = Integer.valueOf(Main.getProperties().getProperty("maxChangeDiameter"));
	private static int maxChangePosition = Integer.valueOf(Main.getProperties().getProperty("maxChangePosition"));
	
	public static void mutate(Genome g) {
		for (Circle c : g.getCircles()) {
			c.setR(newRGBint(c.getR()));
			c.setG(newRGBint(c.getG()));
			c.setB(newRGBint(c.getB()));
			int newDiameter = c.getDiameter();
			if (Circle.getRand().nextDouble() < mutationProbability) {
				newDiameter = (int) Math.floor(newDiameter+(maxChangeDiameter * (Circle.getRand().nextDouble()-0.5)*2.0 ));
				newDiameter = Math.max(newDiameter, 1);
				newDiameter = Math.min(newDiameter, Integer.valueOf(Main.getProperties().getProperty("maxDiameter")));
				c.setDiameter(newDiameter);
			}
			int newX = c.getX();
			if (Circle.getRand().nextDouble() < mutationProbability) {
				newX = (int) Math.floor(newX+(maxChangePosition * (Circle.getRand().nextDouble()-0.5)*2.0 ));
				newX = Math.max(newX, 0);
				newX = Math.min(newX, Main.getWidth()-1);
				c.setX(newX);
			}
			int newY = c.getY();
			if (Circle.getRand().nextDouble() < mutationProbability) {
				newY = (int) Math.floor(newY+(maxChangePosition * (Circle.getRand().nextDouble()-0.5)*2.0 ));
				newY = Math.max(newY, 0);
				newY = Math.min(newY, Main.getWidth()-1);
				c.setY(newY);
//				c.setDiameter(newDiameter);
			}
		}
	}
	
	private static int newRGBint(int color) {
		if (Circle.getRand().nextDouble() < mutationProbability) {
			int newRGBint = (int) Math.floor(color+(maxChangeColor * (Circle.getRand().nextDouble()-0.5)*2.0 ));
			newRGBint = Math.max(newRGBint, 0);
			newRGBint = Math.min(newRGBint, 255);
			return newRGBint;
		} else {
			return color;
		}
	}
	
}
