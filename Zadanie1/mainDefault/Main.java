package mainDefault;

import imageReadFunctions.ImageDisplay;
import imageReadFunctions.ImageReading;

import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class Main {
	
	public static void main (String[] args){
		
	// Basic variables.
	String fileToRead = null;
	String fileToSave = null;
	
	
	//============
	// Program starts here:
	// Load the image and hook it to the image display controller.
	fileToRead = JOptionPane.showInputDialog("Choose the image to read.");
	ImageReading ReadImage = new ImageReading(fileToRead);
	ImageDisplay PrimeImage = new ImageDisplay(ReadImage);
	
	
	// Alter the image and hook THAT up to the image display controller.
	/*
	 * Placeholder code for filtering images goes here.
	 */
	ImageDisplay AlterImage = new ImageDisplay(ReadImage);
	
	
	// Save the altered image
	fileToSave = JOptionPane.showInputDialog("Choose the name for the saved image. \n"
			+ "									[REMEMBER TO INCLUDE THE FILE EXTENSION!!]");
	ReadImage.saveImage(fileToSave);

	
	// Frames that make the world spin round.
	// First frame invoke.
	JFrame urFrame = new JFrame("Obraz Orginalny");
	urFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	urFrame.setSize(800,600);
	urFrame.setContentPane(PrimeImage);
	urFrame.setVisible(true);
	
	// Second frame invoke.
	JFrame isoFrame = new JFrame("Obraz Zmieniony");
	isoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	isoFrame.setSize(800,600);
	isoFrame.setContentPane(AlterImage);
	isoFrame.setVisible(true);
	
	
	}
}