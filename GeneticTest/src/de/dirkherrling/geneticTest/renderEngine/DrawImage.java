/**
 * 
 */
package de.dirkherrling.geneticTest.renderEngine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.dirkherrling.geneticTest.Main;
import de.dirkherrling.geneticTest.fitness.Fitness;
import de.dirkherrling.geneticTest.model.Circle;
import de.dirkherrling.geneticTest.model.Genome;

/**
 * @author dherrling
 *
 */
public class DrawImage extends Thread {

//	private int width;
//	private int height;
	private BufferedImage bi;
	private Genome g;
	private int width;
	private int height;
	
	public DrawImage(int width, int height, Genome g) {
		this.g = g;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void run() {
		Main.increaseThreadCount();
		bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
//		this.width = width;
//		this.height = height;
		Graphics2D ig2 = bi.createGraphics();
//		ig2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//		System.out.println(g.getCircles().size());
//		System.out.println(Main.getProperties().getProperty("drawLargeCirclesFirst"));
		if (!Boolean.parseBoolean(Main.getProperties().getProperty("drawLargeCirclesFirst"))) {
//			System.out.println("not drawlargecirclesfirst");
			for(Circle c : g.getCircles()) {
				ig2.setColor(new Color(c.getR(), c.getG(), c.getB()));
				ig2.fillOval(c.getX()-(c.getDiameter()/2), c.getY()-(c.getDiameter()/2), c.getDiameter(), c.getDiameter());
			}
		} else {
			//sort by diameter
//			System.out.println("drawlargecirclesfirst");
			for (int i = Integer.valueOf(Main.getProperties().getProperty("maxDiameter"))+1; i >= 0; i--) {
				for (Circle c : g.getCircles()) {
					if (c.getDiameter() == i) {
						ig2.setColor(new Color(c.getR(), c.getG(), c.getB()));
						ig2.fillOval(c.getX()-(c.getDiameter()/2), c.getY()-(c.getDiameter()/2), c.getDiameter(), c.getDiameter());
					}
				}
			}
		}
		
		g.setPhenoType(bi);
		g.setPhenoTypeValid(true);
		new Fitness(g).start();
		Main.decreaseThreadCount();
	}
	
	public static void saveImage(BufferedImage bi, String fileName) {
		if (bi != null) {
			try {
				ImageIO.write(bi, "PNG", new File(Main.getPathToImageStore() + fileName));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		}
	}

	public BufferedImage getBi() {
		return bi;
	}

	public void setBi(BufferedImage bi) {
		this.bi = bi;
	}

}
