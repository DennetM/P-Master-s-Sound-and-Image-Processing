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
	fileToRead = JOptionPane.showInputDialog("Choose the image to read.");
	ImageReading ReadImage = new ImageReading(fileToRead);
	
	fileToSave = JOptionPane.showInputDialog("Choose the name for the saved image. \n"
			+ "									[REMEMBER TO INCLUDE THE FILE EXTENSION!!]");
	ReadImage.saveImage(fileToSave);
	
	ImageDisplay DisplayController = new ImageDisplay(ReadImage);
	
	// Standard frame invoke.
	JFrame frame = new JFrame("Obraz");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(800,600);
	frame.setContentPane(DisplayController);
	frame.setVisible(true);
	
	}
}