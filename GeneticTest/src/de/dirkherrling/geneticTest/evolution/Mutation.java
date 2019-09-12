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
	private static int duplicateFeatureGenerationFraction = Integer.valueOf(Main.getProperties().getProperty("duplicateFeatureGenerationFraction"));
	private static boolean directedMutation = Boolean.valueOf(Main.getProperties().getProperty("directedMutation"));
	private static boolean dynamicMutationProbability = Boolean.valueOf(Main.getProperties().getProperty("dynamicMutationProbability"));
	private static boolean dynamicMutationSteps = Boolean.valueOf(Main.getProperties().getProperty("dynamicMutationSteps"));
	
	private static int maxChangeColorDefault = maxChangeColor;
	private static int maxChangePositionDefault = maxChangePosition;
	private static int maxChangeDiameterDefault = maxChangeDiameter;
	private static double mutationProbabilityDefault = mutationProbability;
	
	public static void mutate(Genome g, int generation) {
		for (Circle c : g.getCircles()) {
			if (directedMutation) {
				c = newCircleColor(c);
			} else {
				c.setR(newRGBint(c.getR()));
				c.setG(newRGBint(c.getG()));
				c.setB(newRGBint(c.getB()));
			}
			int newDiameter = c.getDiameter();
			if (dynamicMutationProbability) {
				mutationProbability = Math.max(1.0d / Double.valueOf(Main.getDeltaG()), mutationProbabilityDefault);
			}
			if (dynamicMutationSteps) {
				maxChangeDiameter = (int)Math.round(Math.max(1.0, Math.floor(Double.valueOf(maxChangeDiameterDefault) / Double.valueOf(Main.getDeltaG()))));
//				System.out.println(maxChangeDiameter);
				maxChangePosition = (int)Math.round(Math.max(1.0, Math.floor(Double.valueOf(maxChangePositionDefault) / Double.valueOf(Main.getDeltaG()))));
			}
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
				newY = Math.min(newY, Main.getHeight()-1);
				c.setY(newY);
//				c.setDiameter(newDiameter);
			}
		}
		if (duplicateFeatures && (generation%duplicateFeatureGenerationFraction == 0)) {
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
	
	private static Circle newCircleColor(Circle c) {
		if (Circle.getRand().nextDouble() < mutationProbability) {
//			Color centerColor = new Color();
			int r = 0;
			int g = 0;
			int b = 0;
			if (dynamicMutationSteps) {
				maxChangeColor = (int)Math.round(Math.max(1.0, Math.floor(Double.valueOf(maxChangeColorDefault) / Double.valueOf(Main.getDeltaG()))));
			}
			if (dynamicMutationSteps && Main.getDeltaG() > 100) {
				if (Circle.getRand().nextBoolean()) {
					r = Math.max(0, Math.min(c.getR() - Circle.getRand().nextInt(maxChangeColor), 255));
				} else {
					r = Math.max(0, Math.min(c.getR() + Circle.getRand().nextInt(maxChangeColor), 255));
				}
				if (Circle.getRand().nextBoolean()) {
					g = Math.max(0, Math.min(c.getG() - Circle.getRand().nextInt(maxChangeColor), 255));
				} else {
					g = Math.max(0, Math.min(c.getG() + Circle.getRand().nextInt(maxChangeColor), 255));
				}
				if (Circle.getRand().nextBoolean()) {
					b = Math.max(0, Math.min(c.getB() - Circle.getRand().nextInt(maxChangeColor), 255));
				} else {
					b = Math.max(0, Math.min(c.getB() + Circle.getRand().nextInt(maxChangeColor), 255));
				}
			} else {
				if (Main.getPhenoRs()[c.getX()][c.getY()] <= c.getR()) {
					r = Math.max(0, Math.min(c.getR() - Circle.getRand().nextInt(maxChangeColor), 255));
				} else {
					r = Math.max(0, Math.min(c.getR() + Circle.getRand().nextInt(maxChangeColor), 255));
				}
				if (Main.getPhenoGs()[c.getX()][c.getY()] <= c.getG()) {
					g = Math.max(0, Math.min(c.getG() - Circle.getRand().nextInt(maxChangeColor), 255));
				} else {
					g = Math.max(0, Math.min(c.getG() + Circle.getRand().nextInt(maxChangeColor), 255));
				}
				if (Main.getPhenoBs()[c.getX()][c.getY()] <= c.getB()) {
					b = Math.max(0, Math.min(c.getB() - Circle.getRand().nextInt(maxChangeColor), 255));
				} else {
					b = Math.max(0, Math.min(c.getB() + Circle.getRand().nextInt(maxChangeColor), 255));
				}
			}
			c.setR(r);
			c.setG(g);
			c.setB(b);
			return c;
		} else {
			return c;
		}
	}
	
	private static int newRGBint(int color) {
		if (dynamicMutationSteps) {
			maxChangeColor = (int)Math.round(Math.max(1.0, Math.floor(Double.valueOf(maxChangeColorDefault) / Double.valueOf(Main.getDeltaG()))));
		}
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
