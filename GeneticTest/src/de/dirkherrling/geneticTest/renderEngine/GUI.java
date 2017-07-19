/**
 * 
 */
package de.dirkherrling.geneticTest.renderEngine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.dirkherrling.geneticTest.Main;

/**
 * @author dherrling
 *
 */
public class GUI extends JFrame {
	
	private static final long serialVersionUID = 4517938937622247379L;
	
	private ImageIcon candidateImage;
	private ImageIcon prototypeImage;
	private Main owner;

	public GUI(BufferedImage prototype, Main owner) {
		this.getContentPane().setLayout(new BorderLayout());
		this.prototypeImage = new ImageIcon(prototype);
		this.getContentPane().add(new JLabel(prototypeImage), BorderLayout.NORTH);
		this.owner = owner;
		if (!Boolean.valueOf(Main.getProperties().getProperty("drawAllPhenotypes"))) {
			this.candidateImage = new ImageIcon(prototype);
			this.getContentPane().add(new JLabel(candidateImage), BorderLayout.SOUTH);
		}
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void setPrototypeImage(BufferedImage image) {
		if (image != null) {
			this.prototypeImage.setImage(image);
			this.pack();
			this.repaint();
		}
	}
	
	public void setCandidateImage(BufferedImage candidateImage) {
		if (candidateImage != null) {
			this.candidateImage.setImage(candidateImage);
			this.repaint();
		}
	}
	
	public BufferedImage combineAllPhenotypes() {
		int sideLength = (int) Math.ceil(Math.sqrt(Integer.valueOf(Main.getProperties().getProperty("populationSize"))+1));
		int totalWidth = sideLength * Main.getWidth();
		int totalHeight = sideLength * Main.getHeight();
		BufferedImage result = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = result.createGraphics();
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, totalWidth, totalHeight);
		g2d.drawImage(Main.getPrototypeImage(), 0, 0, null);
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				if (i != 0 || j != 0) {					//pass 0, 0, as the prototype sits there.
					g2d.drawImage(this.owner.getPopulation().getGenomes().get(j*5+i-1).getPhenoType(), i*Main.getWidth(), j*Main.getHeight(), null);
					if (j*5+i-1 >= Integer.valueOf(Main.getProperties().getProperty("populationSize"))) {
						break;
					}
				}
			}
		}
		g2d.dispose();
		return result;
	}
}
