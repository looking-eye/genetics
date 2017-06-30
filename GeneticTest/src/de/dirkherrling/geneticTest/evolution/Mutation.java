package de.dirkherrling.geneticTest.evolution;

import java.util.Vector;

import de.dirkherrling.geneticTest.Main;
import de.dirkherrling.geneticTest.model.Circle;
import de.dirkherrling.geneticTest.model.Genome;

public class Mutation {

	private static double mutationProbability = Double.valueOf(Main.getProperties().getProperty("mutationProbability"));		//originally: 0.01
	private static int maxChangeColor = Integer.valueOf(Main.getProperties().getProperty("maxChangeColor"));
	private static int maxChangeDiameter = Integer.valueOf(Main.getProperties().getProperty("maxChangeDiameter"));
	private static int maxChangePosition = Integer.valueOf(Main.getProperties().getProperty("maxChangePosition"));
	private static boolean duplicateFeatures = Boolean.valueOf(Main.getProperties().getProperty("duplicateFeatures"));
	private static double duplicateFeatureProbability = Double.valueOf(Main.getProperties().getProperty("duplicateFeatureProbability"));
	private static double duplicateFeatureFraction = Double.valueOf(Main.getProperties().getProperty("duplicateFeatureFraction"));
	private static int maxDiameter = Integer.valueOf(Main.getProperties().getProperty("maxDiameter"));
	
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
		if (duplicateFeatures) {
			int numberOfFeaturesToConsiderForDuplication = (int) Math.floor((double)(g.getCircles().size()) * duplicateFeatureFraction);
			int circleCount = 0;
			int removeCircleCount = 0;
			Vector<Circle> sortedCircles = new Vector<Circle>();
			Vector<Circle> circlesToAdd = new Vector<>();
			for (int i = 0; i <= maxDiameter; i++) {
				for (Circle c : g.getCircles()) {
					if (c.getDiameter() == i) {
						sortedCircles.add(c);
						circleCount++;
						if (Circle.getRand().nextDouble() <= duplicateFeatureProbability && circleCount <= numberOfFeaturesToConsiderForDuplication) {
							Circle newCircle = new Circle(c.getX(), c.getY(), c.getDiameter(), c.getR(), c.getG(), c.getB());
							circlesToAdd.add(newCircle);
							removeCircleCount++;
						}
					}
				}
			}
			
			Vector<Circle> circlesToRemove = new Vector<>();
			for (int i = sortedCircles.size()-1; i > sortedCircles.size()-removeCircleCount-1; i--) {
				circlesToRemove.add(sortedCircles.get(i));
			}
			for (Circle c : circlesToRemove) {
				g.getCircles().remove(c);
			}
			for (Circle c: circlesToAdd) {
				g.getCircles().add(c);
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
