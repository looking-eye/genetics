package de.dirkherrling.geneticTest;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.imageio.ImageIO;

import de.dirkherrling.geneticTest.evolution.Crossover;
import de.dirkherrling.geneticTest.evolution.Mutation;
import de.dirkherrling.geneticTest.fitness.Fitness;
import de.dirkherrling.geneticTest.fitness.Util;
import de.dirkherrling.geneticTest.model.Circle;
import de.dirkherrling.geneticTest.model.Genome;
import de.dirkherrling.geneticTest.model.Population;
import de.dirkherrling.geneticTest.renderEngine.DrawImage;
import de.dirkherrling.geneticTest.renderEngine.GUI;

public class Main {
	
	private Population population;
	private static BufferedImage prototypeImage;
	private static String pathToPrototypeImage = "/Users/dherrling/Pictures/IMG_1112_cropped_small.jpg";
	private static int width;
	private static int height;
	private static boolean headless = false;
	private static String pathToImageStore = "./" + System.currentTimeMillis() + "/";
	private static Properties properties;
	private static int runningThreadCount = 0;
	private static String pathToInitialPopulation = "";
	private static ObjectOutputStream oos;
//	private static Color[][] phenotypeColors;
	private static int[][] phenoRs;
	private static int[][] phenoGs;
	private static int[][] phenoBs;
	private static int imageNumber = 0;
	private static int generationOfLastImprovement = 1;
	private static int deltaG = 1;
	
	public Main() throws IOException {
		try {
			headless = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length == 0;
		} catch (HeadlessException hle) {
			headless = true;
		}
		if (!headless) {
			headless = Boolean.valueOf(properties.getProperty("headless"));
		}
		
		prototypeImage = ImageIO.read(new File(pathToPrototypeImage));
		Main.width = prototypeImage.getWidth();
		Main.height = prototypeImage.getHeight();
		
		if (Boolean.valueOf(Main.getProperties().getProperty("altFitnessAlgo"))) {
//			phenotypeColors = new Color[Main.width][Main.height];
			phenoRs = new int[Main.width][Main.height];
			phenoGs = new int[Main.width][Main.height];
			phenoBs = new int[Main.width][Main.height];
			for (int i = 0; i < Main.width; i++) {
				for (int j = 0; j < Main.height; j++) {
//					phenotypeColors[i][j] = new Color(prototypeImage.getRGB(i, j));
					phenoRs[i][j] = new Color(prototypeImage.getRGB(i, j)).getRed();
					phenoGs[i][j] = new Color(prototypeImage.getRGB(i, j)).getGreen();
					phenoBs[i][j] = new Color(prototypeImage.getRGB(i, j)).getBlue();
				}
			}
		}
		
		if (pathToInitialPopulation.equals("")) {
			this.population = new Population();
			this.population.setPopulationSize(Integer.valueOf(properties.getProperty("populationSize")));
			this.population.setGenomeSize(Main.width * Main.height / Integer.valueOf(properties.getProperty("fractionCircleCount")));
		} else {
			try {
				File f = new File(pathToInitialPopulation);
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				this.population = (Population) ois.readObject();
				ois.close();
			} catch (FileNotFoundException | ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
//		fitness = new Fitness(this);
		Genome winner = this.population.getGenomes().get(0);
		for (Genome g : this.population.getGenomes()) {
//			System.out.println("Threadcount: " + runningThreadCount);
			while (runningThreadCount >= Integer.valueOf(properties.getProperty("threadCount"))) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
			new DrawImage(width, height, g).start();
		}

		for (Genome g : this.population.getGenomes()) {
			Fitness.getFitnessValue(g);
			if (winner.getFitness() > g.getFitness()) {
				winner = g;
			}
		}
		GUI gui = null;
		if (!headless) {
			gui = new GUI(prototypeImage, this);
			if (!Boolean.valueOf(properties.getProperty("drawAllPhenotypes"))) {
				gui.setCandidateImage(winner.getPhenoType());
			}
		}
		new File(pathToImageStore).mkdirs();
		properties.store(new FileOutputStream(new File(pathToImageStore + "/config.properties")), "Properties stored for reproduction");
		
		long start = System.currentTimeMillis();
		long stop = System.currentTimeMillis();
		int i = 0;
		generationOfLastImprovement = 1;
		long lastFitness = winner.getFitness();
		while (i != Integer.valueOf(properties.getProperty("generationCount"))) {
			i++;
			//sort
			this.population.setGenomes(Util.sortByFitness(this.population.getGenomes()));
			BufferedImage allImages = null;
			if (Boolean.valueOf(properties.getProperty("drawAllPhenotypes"))
					|| Boolean.valueOf(properties.getProperty("outputAllPhenotypes"))) {
				allImages = gui.combineAllPhenotypes();
			}
			//display winner
			if (!headless) {
				if (!Boolean.valueOf(properties.getProperty("drawAllPhenotypes"))) {
					gui.setCandidateImage(this.population.getGenomes().get(0).getPhenoType());
				} else {
					gui.setPrototypeImage(allImages);
				}
			}
//			if (!new File(Main.getPathToImageStore() + String.valueOf(this.population.getGenomes().get(0).getFitness()) + ".png").exists()) {
			if (this.population.getGenomes().get(0).getFitness() < lastFitness) {
				String fileName = ("0000000000" + Main.imageNumber).substring(("" + Main.imageNumber).length());
				Main.imageNumber++;
				DrawImage.saveImage(this.population.getGenomes().get(0).getPhenoType(), 
						 fileName + ".png");
				if (Boolean.valueOf(properties.getProperty("outputAllPhenotypes"))) {
					DrawImage.saveImage(allImages, 
							 "combined_" + fileName + ".png");
				}
				DrawImage.saveImage(this.population.getGenomes().get(0).getPhenoType(), 
					 "winner.png");
				if (Boolean.valueOf(properties.getProperty("saveWinningGenome"))) {
					oos = new ObjectOutputStream(new FileOutputStream(new File(pathToImageStore + "/winner.ser")));
					oos.writeObject(population);
					oos.close();
				}
			}

			if (this.population.getGenomes().get(0).getFitness() < lastFitness) {
				System.out.println("Generation: " + i + " GPS: " + 1000.0/(stop-start) + 
						" \tWinner: " + this.population.getGenomes().get(0).getFitness() +
						" \tdelta G: " + (i - generationOfLastImprovement) +
						" \tFitness delta: " + (lastFitness - this.population.getGenomes().get(0).getFitness()) + 
						/*" \tThread count: " + runningThreadCount + */" \t" +
						new Date(System.currentTimeMillis()));
				lastFitness = this.population.getGenomes().get(0).getFitness();
				generationOfLastImprovement = i;
			}
			deltaG = Math.max(1, i - generationOfLastImprovement);
			start = System.currentTimeMillis();
			//duplicate best
			Vector<Genome> best = new Vector<>();
			best.add(this.population.getGenomes().get(0).duplicate());
			best.add(this.population.getGenomes().get(1).duplicate());
			best.add(this.population.getGenomes().get(2).duplicate());
			
			//mutate
			for(Genome g : this.population.getGenomes()) {
				Mutation.mutate(g, i);
			}
			
			//cross
			Vector<Genome> newGenomes = new Vector<>();
			for (Genome mother : this.population.getGenomes()) {
				//select father
				int index = Circle.getRand().nextInt(this.population.getGenomes().size());
				Genome father = this.population.getGenomes().get(index);
				newGenomes.add(Crossover.cross(mother, father));
			}
			this.population.setGenomes(newGenomes);
			
			//add best three, remove worst
			if (Boolean.valueOf(properties.getProperty("removeWorst"))) {
				this.population.getGenomes().remove(this.population.getGenomes().size()-1);
				this.population.getGenomes().remove(this.population.getGenomes().size()-1);
				this.population.getGenomes().remove(this.population.getGenomes().size()-1);
			} else {
				this.population.getGenomes().remove(this.population.getGenomes().size()/2);
				this.population.getGenomes().remove(this.population.getGenomes().size()/2);
				this.population.getGenomes().remove(this.population.getGenomes().size()/2);
			}

			this.population.getGenomes().add(0, best.get(2));
			this.population.getGenomes().add(0, best.get(1));
			this.population.getGenomes().add(0, best.get(0));
			
			//genotype => phenotype
			//fitness
			for (Genome g : this.population.getGenomes()) {
				while (runningThreadCount >= Integer.valueOf(properties.getProperty("threadCount"))) {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
				new DrawImage(width, height, g).start();
			}

			//wait for all Threads to finish
			while (runningThreadCount != 0) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
			stop = System.currentTimeMillis();
		}
	}

	public static void main(String[] args) {
		try {
			if (args.length >= 1 && args[0] != null && !args[0].equals("")) {
				File f = new File(args[0]);
				if (f.exists()) {
					Main.pathToPrototypeImage = args[0];
				}
			}
			if (args.length >= 2 && args[1] != null && !args[1].equals("")) {
				File f = new File(args[1]);
				if (f.exists()) {
					Main.pathToInitialPopulation = args[1];
				}
			}
			properties = new Properties();
			properties.load(new FileInputStream(new File("config.properties")));
			new Main();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static BufferedImage getPrototypeImage() {
		return prototypeImage;
	}

	public static void setPrototypeImage(BufferedImage prototypeImage) {
		Main.prototypeImage = prototypeImage;
	}

	public static Properties getProperties() {
		return properties;
	}

	public static String getPathToImageStore() {
		return pathToImageStore;
	}

	public static synchronized void increaseThreadCount() {
		runningThreadCount++;
	}
	
	public static synchronized void decreaseThreadCount() {
		runningThreadCount--;
	}

	public static int[][] getPhenoRs() {
		return phenoRs;
	}

	public static int[][] getPhenoGs() {
		return phenoGs;
	}

	public static int[][] getPhenoBs() {
		return phenoBs;
	}

	public Population getPopulation() {
		return population;
	}

	public static int getDeltaG() {
		return deltaG;
	}

//	public static Color[][] getPhenotypeColors() {
//		return phenotypeColors;
//	}
//
//	public static void setPhenotypeColors(Color[][] phenotypeColors) {
//		Main.phenotypeColors = phenotypeColors;
//	}

}
