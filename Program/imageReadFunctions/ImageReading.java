package imageReadFunctions;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import prettyDrawings.HistogramAction;

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
	protected void initializeAltImage(){
		this.x = img.getWidth();
		this.y = img.getHeight();
		this.altimg = new BufferedImage(x, y, img.getType());
		for (int i=0; i<this.x; i++){
			for (int j=0; j<this.y; j++){
				this.altimg.setRGB(i, j, this.img.getRGB(i,j));
			}
		}
	}
	
	protected int safetyCheck(int val){
		if (val > 255) val = 255;
		if (val < 0) val = 0;
		return val;
	}
	
	//Normalize the picture.
	// Simply put, this algorithm detects the pixel colour range in the picture and then stretches it to fit 0-255.
	// Works on all channels.
	private void stretchContrast(){
		//Color variable to work with, initialized at top-left pixel.
		Color col = new Color(this.img.getRGB(0,0));
		// Streched range for all channels:
		int rNewMin = 0;
		int rNewMax = 255;
		int gNewMin = 0;
		int gNewMax = 255;
		int bNewMin = 0;
		int bNewMax = 255;
		
		// Placeholders for the old range, all channels again:
		// Initialized value: top-most pixel, a placeholder so we start SOMEWHERE.
		int rOldMin = col.getRed();
		int rOldMax = col.getRed();
		int gOldMin = col.getGreen();
		int gOldMax = col.getGreen();
		int bOldMin = col.getBlue();
		int bOldMax = col.getBlue();
		
		// Scan the picture once to find the proper values.
		for (int i = 0; i<this.x; i++){
			for (int j = 0; j<this.y; j++){
				// Overwrite our color and let's start having some fun.
				col = new Color(this.altimg.getRGB(i, j));
				
				if(col.getRed() < rOldMin) rOldMin = col.getRed(); // If smaller, reapply.
				if(col.getRed() > rOldMax) rOldMax = col.getRed(); // If bigger, reapply.
				if(col.getGreen() < gOldMin) gOldMin = col.getGreen();
				if(col.getGreen() > gOldMax) gOldMax = col.getGreen();
				if(col.getBlue() < bOldMin) bOldMin = col.getBlue();
				if(col.getBlue() > bOldMax) bOldMax= col.getBlue();
			}
		}
		
		// And now we run the entire thing AGAIN to do the proper math.
		// The function is simple: http://homepages.inf.ed.ac.uk/rbf/HIPR2/eqns/eqnstr1.gif	
		for (int i = 0; i<this.x; i++){
			for (int j = 0; j<this.y; j++){
				col = new Color(this.altimg.getRGB(i, j));
				
				int r, g, b;
				//Warning: here there be math-dragons.
				r = (col.getRed() - rOldMin) * ((rNewMax - rNewMin) / (rOldMax - rOldMin)) + rNewMin;
				g = (col.getGreen() - gOldMin) * ((gNewMax - gNewMin) / (gOldMax - gOldMin)) + gNewMin;
				b = (col.getBlue() - bOldMin) * ((bNewMax - bNewMin) / (bOldMax - bOldMin)) + bNewMin;
				
				col = new Color(r,g,b);
				this.altimg.setRGB(i, j, col.getRGB());
			}
		}
		
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
	
	// Raleigh Denisty Transform
	// Flies through the entire image and applies the same formula to each pixel in each channel.
	// gmin is the minimum colour density (aka low-end of the colour bracket), gmax is the opposite the user sets and I
	// have no idea how the heck is it even supposed to work, so take it with a grain of salt.
	public void transformRaleigh(int gmin, int gmax, HistogramAction hist){
		System.out.println("Initializing 2nd image.");
		initializeAltImage();
		System.out.println("2nd image initialized.");
		
		double N = x*y; // N is the total amount of pixels in our image, so width&height.
		
		//And now we walk the image...
		for (int i = 0; i<this.x; i++){
			for (int j=0; j<this.y; j++){
				//Grab the colour.
				Color col = new Color(this.img.getRGB(i, j));
				
				//Each function requires a sum to be performed. We repeat the sum for each channel.
				// Yes... it'll take a while.
				
				int redSum = 0; int greenSum = 0; int blueSum = 0;
				for (int fun = 0; fun<=col.getRed(); fun++){
					redSum += hist.getHistValue("red", fun);
				}
				for (int fun = 0; fun<=col.getGreen(); fun++){
					greenSum += hist.getHistValue("green", fun);
				}
				for (int fun = 0; fun<=col.getBlue(); fun++){
					blueSum += hist.getHistValue("blue", fun);
				}			
				
				//Now we can do the proper math, which is...
				int r, g, b;
				
				//r - red section, bit by bit.
				double alphSquareRed = (Math.pow((gmax-gmin),2)) / (-2 * Math.log((hist.getHistValue("red", 0)/N)));
				double sumR = redSum/N;
				double logSumR = Math.pow(Math.log(sumR),1);
				double expandSumR = -2 * alphSquareRed * logSumR;
				double expandsqrtR = Math.sqrt(expandSumR);
				if (i<2&&j<2) System.out.println("expandedsqrtR: "+expandsqrtR);
				int finalR = (int) expandsqrtR + gmin;
				r = finalR;
				
				//g - green section
				double alphSquareGreen = (Math.pow((gmax-gmin),2)) / (-2 * Math.log((hist.getHistValue("green", 0)/N)));
				double sumG = greenSum/N;
				double logSumG = Math.pow(Math.log(sumG),1);
				double expandSumG = -2 * alphSquareGreen * logSumG;
				double expandsqrtG = Math.sqrt(expandSumG);
				int finalG = (int) expandsqrtG + gmin;
				g = finalG;
				
				//b - blue section
				double alphSquareBlue = (Math.pow((gmax-gmin),2)) / (-2 * Math.log((hist.getHistValue("blue", 0)/N)));
				double sumB = blueSum/N;
				double logSumB = Math.pow(Math.log(sumB),1);
				double expandSumB = -2 * alphSquareBlue * logSumB;
				double expandsqrtB = Math.sqrt(expandSumB);
				int finalB = (int) expandsqrtB + gmin;
				b = finalB;
				
				
				//Final safety check and sum clearout.
				r = safetyCheck(r);
				g = safetyCheck(g);
				b = safetyCheck(b);
				
				//And finalize.
				col = new Color(r,g,b);
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
		
		System.out.println("\n\nThis may take a while...\n");
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
		stretchContrast();
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
			
			System.out.println("\n\nThis may take a while...\n");
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
			stretchContrast();
		}
		

		//Normalize the picture, THREE TABLES EDITION!
		// Because sometimes life likes throwing logs under your feet.
		// Works the same as the above algorithm, instead operates on three int tables and not Colors.
		protected void stretchContrastTableEdition(int[][] r, int[][] g, int[][] b){
			// Streched range for all channels:
			int rNewMin = 0;
			int rNewMax = 255;
			int gNewMin = 0;
			int gNewMax = 255;
			int bNewMin = 0;
			int bNewMax = 255;
			
			// Placeholders for the old range, all channels again:
			// Initialized value: top-most pixel, a placeholder so we start SOMEWHERE.
			int rOldMin = r[0][0];
			int rOldMax = r[0][0];
			int gOldMin = g[0][0];
			int gOldMax = g[0][0];
			int bOldMin = b[0][0];
			int bOldMax = b[0][0];
			
			// Scan the picture once to find the proper values.
			for (int i = 0; i<this.x; i++){
				for (int j = 0; j<this.y; j++){
					
					if(r[i][j] < rOldMin) rOldMin = r[i][j]; // If smaller, reapply.
					if(r[i][j] > rOldMax) rOldMax = r[i][j]; // If bigger, reapply.
					if(g[i][j] < gOldMin) gOldMin = g[i][j];
					if(g[i][j] > gOldMax) gOldMax = g[i][j];
					if(b[i][j] < bOldMin) bOldMin = b[i][j];
					if(b[i][j] > bOldMax) bOldMax = b[i][j];
				}
			}
			
			// And now we run the entire thing AGAIN to do the proper math.
			// The function is simple: http://homepages.inf.ed.ac.uk/rbf/HIPR2/eqns/eqnstr1.gif	
			for (int i = 0; i<this.x; i++){
				for (int j = 0; j<this.y; j++){
					
					int rnew, gnew, bnew;
					//Warning: here there be math-dragons.
					rnew = (r[i][j] - rOldMin) * ((rNewMax - rNewMin) / (rOldMax - rOldMin)) + rNewMin;
					gnew = (g[i][j] - gOldMin) * ((gNewMax - gNewMin) / (gOldMax - gOldMin)) + gNewMin;
					bnew = (b[i][j] - bOldMin) * ((bNewMax - bNewMin) / (bOldMax - bOldMin)) + bNewMin;
					
					Color col = new Color(rnew, gnew, bnew);
					this.altimg.setRGB(i, j, col.getRGB());
					
				}
			}
			
		}
		
	//Foreground Filtering, in four variants.
	// The main determinant is the INT value, from 1 to 4 (as inputted by the user). Variants are named in the code.
	public void foregroundFilter(int variant){
		System.out.println("Initializing 2nd image.");
		initializeAltImage();
		System.out.println("2nd image initialized.");
		
		int kernelwidth = 3;
		int kernelheight = 3;
		
		int[] rMedian = new int [kernelwidth*kernelheight];
		int[] gMedian = new int [kernelwidth*kernelheight];
		int[] bMedian = new int [kernelwidth*kernelheight];
		
		int kerneliter = 0;
		
		//Our filters as tables, initialized top-down, left-to-right in that order.
		int[] south = {-1, 1, 1, -1, -2, 1, -1, 1, 1};
		int[] southwest = {1, 1, 1, -1, -2, 1, -1, -1, 1};
		int[] west = {1, 1, 1, 1, -2, 1, -1, -1, -1};
		int[] northwest = {1, 1, 1, 1, -2, -1, 1, -1, -1};
		
		//And since this is a bullshit filter, we're going to need some backup.
		int[][] rChannel = new int [this.x][this.y];
		int[][] gChannel = new int [this.x][this.y];
		int[][] bChannel = new int [this.x][this.y];
		//...yes, why I did just make 3 pictures out of one. SUE ME.
		
		
		System.out.println("\n\nThis may take a while...\n");
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
				int r = 0;
				int g = 0;
				int b = 0;
				//Math time.
				// REMINDER: WE FOLLOW THE (Y) VALUE, DESCENDING.
				for (int itr = 0; itr<rMedian.length; itr++){
					if (variant == 1){
						r += rMedian[itr]*south[itr];
						g += gMedian[itr]*south[itr];
						b += bMedian[itr]*south[itr];
					}
					else if (variant == 2){
						r += rMedian[itr]*southwest[itr];
						g += gMedian[itr]*southwest[itr];
						b += bMedian[itr]*southwest[itr];
					}
					else if (variant == 3){
						r += rMedian[itr]*west[itr];
						g += gMedian[itr]*west[itr];
						b += bMedian[itr]*west[itr];
					}
					else if (variant == 4){
						r += rMedian[itr]*northwest[itr];
						g += gMedian[itr]*northwest[itr];
						b += bMedian[itr]*northwest[itr];
					}
				}
				rChannel[i][j] = safetyCheck(r);
				gChannel[i][j] = safetyCheck(g);
				bChannel[i][j] = safetyCheck(b);
			}
		}
		// THIS is where we do the proper thingmabob.
		stretchContrastTableEdition(rChannel, gChannel, bChannel);
	}
	
	//Rosenfeld non-linear Filter.
	// This is nothing but math.
	public void Rosenfeld(int R){
		System.out.println("Initializing 2nd image.");
		initializeAltImage();
		System.out.println("2nd image initialized.");
		
		int r,g,b;
		int r1 = 0, g1 = 0, b1 = 0;
		int r2 = 0, g2 = 0, b2 = 0;
		Color col = null;
		
		
		//Main loop that flies over the entire board.
		for (int i=R; i<this.x-R; i++){
			for (int j=0; j<this.y; j++){				
				// The function rolls itself over each channel.
				// g(x,y) = 1/R * ( sum(i=0, R, f(x+i-1, y) - sum(i=0, R, f(x-i,y))
				
				//Sum calculation.
				for (int kern = 1; kern<=R; kern++){
					//System.out.println("First color cords: ("+(i+kern-1)+","+j);
					col = new Color(this.img.getRGB(i+kern-1, j));
					r1 += col.getRed();
					g1 += col.getGreen();
					b1 += col.getBlue();
					
					//System.out.println("First color cords: ("+(i-kern)+","+j);
					col = new Color(this.img.getRGB(i-kern, j));
					r2 += col.getRed();
					g2 += col.getGreen();
					b2 += col.getBlue();
				}
				
				// Final calculation
				// IMPORANT: IF YOU EVERY WANT TO MULTIPLY SOMETHING BY 1/X, JUST DIVIDE THE DAMN THING BY X. OTHERWISE JAVA
				// WILL THROW A FUCKING HISSY FIT GOD DAMMIT YOU COCKY WHINING SCAMP.
				r = safetyCheck((r1 - r2)/R);
				g = safetyCheck((g1 - g2)/R);
				b = safetyCheck((b1 - b2)/R);
				
				
				r1 = 0; g1 = 0; b1 = 0;
				r2 = 0; g2 = 0; b2 = 0;
				
				col = new Color(r,g,b);
				this.altimg.setRGB(i, j, col.getRGB());
			}
		}
	}
	
	//Calculates the Mean Square Error value, aka MSE
	private double calcMSE(String color){
		double crossX = Math.pow(x,-1);
		double crossY = Math.pow(y,-1);
		double totalSum = 0;
		
		for(int i = 0;i<x;i++){
			for(int j = 0;j<y;j++){
				Color colprim = new Color(this.img.getRGB(i, j));
				Color colchng = new Color(this.altimg.getRGB(i,j));
				
				if (color.equals("red")) totalSum += Math.pow(Math.abs(colchng.getRed() - colprim.getRed()),2);
				if (color.equals("green")) totalSum += Math.pow(Math.abs(colchng.getGreen() - colprim.getGreen()),2);
				if (color.equals("blue")) totalSum += Math.pow(Math.abs(colchng.getGreen() - colprim.getGreen()),2);
			}
		}
		System.out.println("MSE Sum: "+totalSum);
		System.out.println("MSE: "+(totalSum*crossX*crossY));
		return (totalSum*crossX*crossY);
	}
	
	//Calculates the Peak Signal-to-Noise Ratiom, aka PSNR.
	public void calcPSNR(){
		double rPSNR = 10 * Math.log(Math.pow(255,2)/calcMSE("red"));
		double gPSNR = 10 * Math.log(Math.pow(255,2)/calcMSE("green"));
		double bPSNR = 10* Math.log(Math.pow(255,2)/calcMSE("blue"));
		System.out.println("PSNR Values:\n"
						+" rPSNR: "+rPSNR+"\n"
						+" gPSNR: "+gPSNR+"\n"
						+" bPSNR: "+bPSNR);
	}

	
	//Save the image we have to a file [NATIVE VERSION].
	public void saveImage(String saveFile){
		File outputF = new File(saveFile);
		try{
			ImageIO.write(this.altimg, "png", outputF);
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
