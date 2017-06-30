/**
 * 
 */
package de.dirkherrling.geneticTest.renderEngine;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author dherrling
 *
 */
public class GUI extends JFrame {
	
	private static final long serialVersionUID = 4517938937622247379L;
	
	private ImageIcon candidateImage;


	public GUI(BufferedImage prototype) {
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new JLabel(new ImageIcon(prototype)), BorderLayout.NORTH);
		candidateImage = new ImageIcon(prototype);
		this.getContentPane().add(new JLabel(candidateImage), BorderLayout.SOUTH);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void setCandidateImage(BufferedImage candidateImage) {
		if (candidateImage != null) {
			this.candidateImage.setImage(candidateImage);
			this.repaint();
		}
	}

}
