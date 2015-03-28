package mainDefault;

import imageReadFunctions.ImageDisplay;
import imageReadFunctions.ImageReading;

import javax.swing.JFrame;

import prettyDrawings.HistogramAction;



public class Main {
	
	public static void main (String[] args){
		
	// Basic variables.
	String fileToRead = null;
	String fileToSave = null;
	
	int frameX = 800;
	int frameY = 600;
	
	
	//============
	// Program starts here:
	// Load the image and hook it to the image display controller.
	//fileToRead = JOptionPane.showInputDialog("Choose the image to read.");
	//fileToRead = "CheerSmiles.png"; // Short version for debug purposes.
	fileToRead = "Lena.png";
	ImageReading ReadImage = new ImageReading(fileToRead);
	
	HistogramAction histAction = new HistogramAction();
	histAction.showTheThing(ReadImage);
		
		
	ImageDisplay PrimeImage = new ImageDisplay(ReadImage, false);
		

	// Alter the image and hook THAT up to the image display controller.
	//ReadImage.brightnessAdjust(true, 200);
	//ReadImage.contrastAdjust(100);
	//ReadImage.invertAdjust();
	//ReadImage.meanFilter();
	//ReadImage.medianFilter();
	//ReadImage.foregroundFilter(4);
	//ReadImage.Rosenfeld(2);
	ReadImage.transformRaleigh(25, 100, histAction);
	ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
	
	
	
	// Save the altered image
	//fileToSave = JOptionPane.showInputDialog("Choose the name for the saved image. \n"
	//		+ "									[REMEMBER TO INCLUDE THE FILE EXTENSION!!]");
	//ReadImage.saveImage(fileToSave);

	
	// Frames that make the world spin round.
	// First frame invoke.
	JFrame urFrame = new JFrame("Obraz Orginalny");
	urFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	urFrame.setSize(frameX,frameY);
	urFrame.setContentPane(PrimeImage);
	urFrame.setVisible(true);
	
	// Second frame invoke.
	JFrame isoFrame = new JFrame("Obraz Zmieniony");
	isoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	isoFrame.setSize(frameX,frameY);
	isoFrame.setLocation(frameX, 0);
	isoFrame.setContentPane(AlterImage);
	isoFrame.setVisible(true);
	
	
	}
}