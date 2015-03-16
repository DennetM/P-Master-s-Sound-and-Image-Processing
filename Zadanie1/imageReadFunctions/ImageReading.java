package imageReadFunctions;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImageReading {
	
	// Variables
	protected BufferedImage img = null; // The loaded image.
	protected BufferedImage altimg = null; // The altered, filtered image space.
	
	protected int x = 0; // Width
	protected int y = 0; // Height
	// ^ These will be OFTEN overwritten, so pay attention what you do with them!
	
	//=============================
	//=============================
	
	//Constructor
	public ImageReading(String x){
		accessImage(x);
	}
	
	//=============================
	//=============================
	
	// Set and Get Functions
	// Get Function for img -> pass image to another class.
		public BufferedImage getImage(){
			return img;
		}
		
		public BufferedImage getAlteredImage(){
			return altimg;
		}
		
	//=============================
	//=============================

	// Functions
	// Read the image using BufferedImage class.
	public void accessImage(String imagename){
		try {
			this.img = ImageIO.read(new File(imagename));
		} catch (IOException e){
			System.out.println("The 'img' variable is null.");
		}
	}
	
	
	// Initialize the altered image 
	private void initializeAltImage(){
		this.x = img.getWidth();
		this.y = img.getHeight();
		this.altimg = new BufferedImage(x, y, img.getType());
	}
	
	private int safetyCheck(int val){
		if (val > 255) val = 255;
		if (val < 0) val = 0;
		return val;
	}
	
	// Brightness altering function.
	// Bool 1 -> brighten, Bool 0 -> darken
	public void brightnessAdjust(Boolean mode, int altvalue){
		System.out.println("Initializing 2nd image.");
		initializeAltImage();
		System.out.println("2nd image initialized.");
		//Main loop that flies over the entire board.
		for (int i=0; i<this.x; i++){
			for (int j=0; j<this.y; j++){
				
				//Start by grabbing the colour (type ARGB) from the pixel we're in.
				Color col = new Color(this.img.getRGB(i, j));
				
				int r, g, b;
				if (mode == true){
					r = safetyCheck(col.getRed() + altvalue);
					g = safetyCheck(col.getGreen() + altvalue);
					b = safetyCheck(col.getBlue() + altvalue);
				}
				else{
					r = safetyCheck(col.getRed() - altvalue);
					g = safetyCheck(col.getGreen() - altvalue);
					b = safetyCheck(col.getBlue() - altvalue);
				}
				
				col = new Color(r,g,b); // Overwrite the colour.
				this.altimg.setRGB(i, j, col.getRGB());
				
			}
		}
	}
	
	// Contrast altering function.
	// Again, mode 1 -> increase contrast, 0 -> decrease contrast.
	public void contrastAdjust(int contrast){
		System.out.println("Initializing 2nd image.");
		initializeAltImage();
		System.out.println("2nd image initialized.");
		
		int factor = 0;
		
		factor = (259 * (contrast + 255)) / (255 * (259 - contrast));
		
		//Main loop that flies over the entire board.
		for (int i=0; i<this.x; i++){
			for (int j=0; j<this.y; j++){
				
				//Start by grabbing the colour (type ARGB) from the pixel we're in.
				Color col = new Color(this.img.getRGB(i, j));
				
				int r, g, b;
				
				r = safetyCheck(factor * (col.getRed() - 128) + 128);
				g = safetyCheck(factor * (col.getGreen() - 128) + 128);
				b = safetyCheck(factor * (col.getBlue() - 128) + 128);
				
				col = new Color(r,g,b); // Overwrite the colour.
				this.altimg.setRGB(i, j, col.getRGB());
				
			}
		}
	}
	
	// Inverts the color
	public void invertAdjust(){
		System.out.println("Initializing 2nd image.");
		initializeAltImage();
		System.out.println("2nd image initialized.");
		
		//Main loop that flies over the entire board.
		for (int i=0; i<this.x; i++){
			for (int j=0; j<this.y; j++){
				
				//Start by grabbing the colour (type ARGB) from the pixel we're in.
				Color col = new Color(this.img.getRGB(i, j));
				
				int r, g, b;
				
				r = safetyCheck(255 - col.getRed());
				g = safetyCheck(255 - col.getGreen());
				b = safetyCheck(255 - col.getBlue());
				
				col = new Color(r,g,b); // Overwrite the colour.
				this.altimg.setRGB(i, j, col.getRGB());
				
			}
		}
	}
	
	
	//Ur-Mean Filtering.
	public void meanFilter(){
		System.out.println("Initializing 2nd image.");
		initializeAltImage();
		System.out.println("2nd image initialized.");
		
		int kernelwidth = 3;
		int kernelheight = 3;
		
		int[] rMedian = new int [kernelwidth*kernelheight];
		int[] gMedian = new int [kernelwidth*kernelheight];
		int[] bMedian = new int [kernelwidth*kernelheight];
		
		int kerneliter = 0;
		
		// Walk the entire image but stop before you go out of bounds at the kernel boundraries.
		for (int i = 0; i<this.x-kernelwidth; i++){
			for (int j=0; j<this.y-kernelheight; j++){
				// Walk the kernel itself.
				for (int ki = 0; ki<kernelwidth; ki++){
					for(int kj = 0; kj<kernelheight; kj++){
						Color col = new Color(this.img.getRGB(i+ki, j+kj));
						rMedian[kerneliter] = col.getRed();
						gMedian[kerneliter] = col.getGreen();
						bMedian[kerneliter] = col.getBlue();
						kerneliter++;
					}
				}
				kerneliter = 0;
				for (int sumiter = 1; sumiter < rMedian.length; sumiter++){
					rMedian[0] += rMedian[sumiter];
					gMedian[0] += gMedian[sumiter];
					bMedian[0] += bMedian[sumiter];
				}
				
				Color colfinal = new Color((rMedian[0]/9), (gMedian[0]/9), (bMedian[0]/9));
				this.altimg.setRGB(i+1, j+1, colfinal.getRGB());
			}
		}
	}
	
	//Ur-Median Filtering.
		public void medianFilter(){
			System.out.println("Initializing 2nd image.");
			initializeAltImage();
			System.out.println("2nd image initialized.");
			
			int kernelwidth = 3;
			int kernelheight = 3;
			
			int[] rMedian = new int [kernelwidth*kernelheight];
			int[] gMedian = new int [kernelwidth*kernelheight];
			int[] bMedian = new int [kernelwidth*kernelheight];
			
			int kerneliter = 0;
			
			// Walk the entire image but stop before you go out of bounds at the kernel boundraries.
			for (int i = 0; i<this.x-kernelwidth; i++){
				for (int j=0; j<this.y-kernelheight; j++){
					// Walk the kernel itself.
					for (int ki = 0; ki<kernelwidth; ki++){
						for(int kj = 0; kj<kernelheight; kj++){
							Color col = new Color(this.img.getRGB(i+ki, j+kj));
							rMedian[kerneliter] = col.getRed();
							gMedian[kerneliter] = col.getGreen();
							bMedian[kerneliter] = col.getBlue();
							kerneliter++;
						}
					}
					kerneliter = 0;
					Arrays.sort(rMedian);
					Arrays.sort(gMedian);
					Arrays.sort(bMedian);
					Color colfinal = new Color(rMedian[4], gMedian[4], bMedian[4]);
					this.altimg.setRGB(i+1, j+1, colfinal.getRGB());
				}
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
	
}
	
