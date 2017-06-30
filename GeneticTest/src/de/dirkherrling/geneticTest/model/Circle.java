package de.dirkherrling.geneticTest.model;

import java.io.Serializable;
import java.util.Random;

import de.dirkherrling.geneticTest.Main;

/**
 * 
 * @author Dirk Herrling
 *
 */
public class Circle implements Serializable {
	
	private static final long serialVersionUID = 812211413524205485L;
	private int x;
	private int y;
	private int diameter;
	private int r;
	private int g;
	private int b;
	private static Random rand = new Random(System.currentTimeMillis());
	
	public Circle() {
		
	}
	
	public Circle(int maxX, int maxY) {
//		Random r = 
		this.x = rand.nextInt(maxX);
		this.y = rand.nextInt(maxY);
//		this.diameter = rand.nextInt(Math.max(maxX, maxY) / 5);
		this.diameter = rand.nextInt(Integer.valueOf(Main.getProperties().getProperty("maxDiameter")));
		this.r = rand.nextInt(255);
		this.g = rand.nextInt(255);
		this.b = rand.nextInt(255);
//		System.out.println(x + " " + y + " " + diameter + " " + this.r + " " + g + " " + b);
	}
	
	public Circle(int x, int y, int diameter, int r, int g, int b) {
		this.x = x;
		this.y = y;
		this.diameter = diameter;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Circle duplicate() {
		Circle result = new Circle(x, y, diameter, r, g, b);
		
		return result;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public static Random getRand() {
		return rand;
	}
}
