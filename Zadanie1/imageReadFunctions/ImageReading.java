package imageReadFunctions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReading {
	
	// Variables
	protected BufferedImage img = null; // The loaded image.
	protected BufferedImage altimg = null; // The altered, filtered image space.
	
	
	
	//Constructor
	public ImageReading(String x){
		accessImage(x);
	}
	

	// Functions
	// Read the image using BufferedImage class.
	public void accessImage(String imagename){
		try {
			this.img = ImageIO.read(new File(imagename));
		} catch (IOException e){
			System.out.println("The 'img' variable is null.");
		}
	}
	
	//Save the image we have to a file [NATIVE VERSION].
	public void saveImage(String saveFile){
		File outputF = new File(saveFile);
		try{
			ImageIO.write(this.img, "png", outputF);
		} catch (IOException e){
			System.out.println("Something exploded when saving the file. Sorry!");
		}
	}
	
	//Save the image we have to a file [POST-FILTER VERSION].
	public void saveImage(String saveFile, BufferedImage image){
		File outputF = new File(saveFile);
		try{
			ImageIO.write(image, "png", outputF);
		} catch (IOException e){
			System.out.println("Something exploded when saving the file. Sorry!");
		}
	}
	
	// Get Function for img -> pass image to another class.
	BufferedImage getImage(){
		return img;
	}
}
	
