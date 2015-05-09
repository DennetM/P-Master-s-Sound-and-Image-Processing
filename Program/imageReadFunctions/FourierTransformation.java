package imageReadFunctions;

//Note: For basic FFT, we're using Apache's deployment of the function.
//Because really, IT'S JUST SIMPLE MATH. Take a formula and apply it. Too many chances to bug out if I do it by myself.
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;


//Fourtier Transformation class extending ImageReading.
//In practice this is no different from ImageReading, and exists purely NOT to clog up space in the main class.
//...which is already bloated if you ask me.
public class FourierTransformation extends ImageReading {
	
	//Variables
	//2D tables that represent the flat colourspace we'll be filling out during our transformation.
	protected double[][] r;
	protected double[][] g;
	protected double[][] b;
	
	int baseWidth; // Base width of the image, we need to remember it.
	int baseHeight; // Base height of the image, we need to remember it as well.
	
	int newWidth; // Same thing, but after the FFT padding.
	int newHeight; // As above.
	
	//2D tables that represent the specific channels after being passed through a 2D FFT.
	//Com is the sum-total Complex Number table, which will be our main operating procedure.
	//Two double tables are in order: Amplitude (Power) and Phase (Frequency) of the image. They're the ones we push to visualize.
	//NOTE: THEY JUST REPRESENT THE FOURIER. ACTUAL FOURIER MATH IS DONE ON rCOM AND NOT ELSEWHERE.
	protected Complex[][] rCom; //red Complex.
	public double[][] rVizPow; // Red Power. (dassoracist)
	public double[][] rVizFrq; // Red Frequency.
	
	protected Complex[][] gCom; //green Complex.
	public double[][] gVizPow; // Green Power.
	public double[][] gVizFrq; // Green Frequency.
	
	protected Complex[][] bCom; //blue Complex.
	public double[][] bVizPow; // Blue power.
	public double[][] bVizFrq; // Blue frequency.

	FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD); // Our FFT. Using the Standard formula.
																					// Again, don't ask. Better left alone.
	
	//=============================
	//=============================
	
	//Constructor
	//...which is essentially our ImageReading constructor.
	//Except we enrich it to plaster the R G B values from the image into the tables.
	public FourierTransformation(String x){
		super(x); //We-do what we did in the original.
		
		this.baseWidth = this.img.getWidth();
		this.baseHeight = this.img.getHeight();
		
		//We initialize the sizes of our tables.
		this.r = new double[baseWidth][baseHeight];
		this.g = new double[baseWidth][baseHeight];
		this.b = new double[baseWidth][baseHeight];
		
		//...and now we copy the respective data into the right section of the image.
		for(int i = 0; i<baseWidth; i++){
			for(int j = 0; j<baseHeight; j++){
				Color tempCol = new Color (this.img.getRGB(i,j));
				r[i][j] = tempCol.getRed();
				g[i][j] = tempCol.getGreen();
				b[i][j] = tempCol.getBlue();
			}
		}
		//We now have three 2D tables filled with their respective colours. Wowzers.
	}
	

	//=============================
	//=============================
	
	//Functions, also known as "where things get interesting" section.
	
	//Padding function - this function checks the width and height of the image we've grabbed and pads it to the nearest power of 2.
	//This is necessary to perform an FFT later on, so we'll have to bear with it.
	private void pad(){
		//We'll pad the array to a rectangle for clarity, so let's first check which is greater - width or height.
		int padding;
		//Copy the array to a safecopy.
		double[][] tempArrayR = new double[baseWidth][baseHeight];
		double[][] tempArrayG = new double[baseWidth][baseHeight];
		double[][] tempArrayB = new double[baseWidth][baseHeight];
		
		for(int i=0;i<baseWidth;i++){
			for(int j=0;j<baseHeight;j++){
				tempArrayR[i][j] = this.r[i][j];
				tempArrayG[i][j] = this.g[i][j];
				tempArrayB[i][j] = this.b[i][j];
			}
		}
		if(baseWidth > baseHeight) padding = baseWidth;
		else if (baseHeight > baseWidth) padding = baseHeight;
		else padding = baseWidth; // Doesn't matter in this scenario, since both are equal.
		
		//Now we introduce the powerIndex, also known as the current power of two.
		int powerIndex = 1;
		//First, by width.
		while(powerIndex < padding && powerIndex < 8388608){
			powerIndex *=2;
		}
		//Resize the array.
		this.r = new double[powerIndex][powerIndex];
		this.g = new double[powerIndex][powerIndex];
		this.b = new double[powerIndex][powerIndex];
		for(int i=0; i<powerIndex; i++){
			for(int j=0; j<powerIndex;j++){
				//If you go over the base values, instead input zeroes.
				if(i>=baseWidth || j>=baseHeight){
					this.r[i][j] = 0;
					this.g[i][j] = 0;
					this.b[i][j] = 0;
				}
				else{
					this.r[i][j] = tempArrayR[i][j];
					this.g[i][j] = tempArrayG[i][j];
					this.b[i][j] = tempArrayB[i][j];
				}
			}
		}
		
		this.newHeight = powerIndex;
		this.newWidth = powerIndex;
		//Our table is now padded and should work for the FFT transform.
	}
	
	//Updates the separate tables - powers and frequencies.
	//This is invoked each time we want to push a visualization of our spectrums unto the screen.
	//Essentially just calculates the abs() (power) and argument (freq) from the Complex table.
	private void updateSeparates(){
		this.rVizPow = new double [newWidth][newHeight];
		this.rVizFrq = new double [newWidth][newHeight];
		
		this.gVizPow = new double [newWidth][newHeight];
		this.gVizFrq = new double [newWidth][newHeight];
		
		this.bVizPow = new double [newWidth][newHeight];
		this.bVizFrq = new double [newWidth][newHeight];
		
		//Now iterate the table and count the stuff.
		for (int i=0; i<newWidth; i++){
			for(int j=0; j<newHeight; j++){
				//Power:
				this.rVizPow[i][j] = rCom[i][j].abs();
				this.gVizPow[i][j] = gCom[i][j].abs();
				this.bVizPow[i][j] = bCom[i][j].abs();
				
				//Freq:
				this.rVizFrq[i][j] = rCom[i][j].getArgument();
				this.gVizFrq[i][j] = gCom[i][j].getArgument();
				this.bVizFrq[i][j] = bCom[i][j].getArgument();
			}
		}
	}
	
	//The main forward/simple/standard. FFT function.
	public void FFTstandard(){
		//First step - pad out the tables so that they can work. We can also initialize our result tables with proper values.
		pad();
		
		this.rCom = new Complex[newWidth][newHeight];
		this.gCom = new Complex[newWidth][newHeight];
		this.bCom = new Complex[newWidth][newHeight];
			
		//2D Fourier Transform is simply a 1D fouriter transform performed twice. Once by width, once by height.
		//This is going to be a very lengthy algorithm, so even with a simple FFT (the fastest FT out there), it may take a while.
		
		//First pass - by columns.
		//There's as many columns as the WIDTH of the table.
		for(int clmn = 0; clmn < newWidth; clmn++){
			//First thing - we create a temporary table that's a single column, then fill it out.
			double tempSingleR[] = new double[newHeight];
			double tempSingleG[] = new double[newHeight];
			double tempSingleB[] = new double[newHeight];
			for (int i = 0; i<newHeight; i++){
				//System.out.println("Clmn: "+clmn+" i: "+i);
				tempSingleR[i] = r[clmn][i];
				tempSingleG[i] = g[clmn][i];
				tempSingleB[i] = b[clmn][i];
			}
			//Note: we are still in a single pass of the main loop.
			//We now have three tables, each of them is a single column from each of the spectrums we're Fourier-ing.
			//Enter three supplementary Complex tables wherein each of the previous tables is Fourier'd.
			Complex[] rTemp = fft.transform(tempSingleR, TransformType.FORWARD);
			Complex[] gTemp = fft.transform(tempSingleG, TransformType.FORWARD);
			Complex[] bTemp = fft.transform(tempSingleB, TransformType.FORWARD);
			//Now we reverse the previous. We cram each of these tables into its corresponding final table.
			for (int i = 0; i<tempSingleR.length; i++){
				rCom[clmn][i] = rTemp[i];
				gCom[clmn][i] = gTemp[i];
				bCom[clmn][i] = bTemp[i];
			}
		}
		System.out.println("Finished FFT by columns.");
		//We have a full rCom table that's been partially transformed. Now we repeat the entire algorithm by rows.
		//Second pass - by rows.
		for(int row = 0; row < newHeight; row++){
			//Notice - we make three temporary rows as before, but since we've already passed the Fourier, they're COMPLEX now!
			Complex[] tempSingleR = new Complex[newWidth];
			Complex[] tempSingleG = new Complex[newWidth];
			Complex[] tempSingleB = new Complex[newWidth];
			for (int i=0; i<newWidth; i++){
				tempSingleR[i] = rCom[i][row];
				tempSingleG[i] = gCom[i][row];
				tempSingleB[i] = bCom[i][row];
			}
			//And since they're complex, we can 'abuse' them for this task.
			tempSingleR = fft.transform(tempSingleR, TransformType.FORWARD);
			tempSingleG = fft.transform(tempSingleG, TransformType.FORWARD);
			tempSingleB = fft.transform(tempSingleB, TransformType.FORWARD);
			//Finally, we cram them in once more.
			for(int i =0; i<tempSingleR.length; i++){
				rCom[i][row] = tempSingleR[i];
				gCom[i][row] = tempSingleG[i];
				bCom[i][row] = tempSingleB[i];
			}
		}
		System.out.println("Finished FFT by rows. Finished FFT.");
	}
	
	//The main inverse FFT function.
	//Warning - using this function overwrites the rgbCom tables with inverted FFT. In essence this means imaginaries are gone and reals
	// are our image. We can't do FFT algos until we revert the image through a forward FFT once again.
	public void FFTinverse(){
		//The entire algorithm works just like its forward cousin, instead operating solely on Complex[]es.
		
		//Step one - inverse FFT by columns.
		for(int clmn = 0; clmn < newWidth; clmn++){
			Complex[] tempSingleR = new Complex[newHeight];
			Complex[] tempSingleG = new Complex[newHeight];
			Complex[] tempSingleB = new Complex[newHeight];
			for (int i=0; i<newHeight; i++){
				tempSingleR[i] = rCom[clmn][i];
				tempSingleG[i] = gCom[clmn][i];
				tempSingleB[i] = bCom[clmn][i];
			}
			tempSingleR = fft.transform(tempSingleR, TransformType.INVERSE);
			tempSingleG = fft.transform(tempSingleG, TransformType.INVERSE);
			tempSingleB = fft.transform(tempSingleB, TransformType.INVERSE);
			for (int i=0; i<newHeight; i++){
				rCom[clmn][i] = tempSingleR[i];
				gCom[clmn][i] = tempSingleG[i];
				bCom[clmn][i] = tempSingleB[i];
			}
		}
		System.out.println("Finished inversion-FFT by colmns.");
		
		//Step two - inverse by rows.
		for(int row = 0; row < newHeight; row++){
			Complex[] tempSingleR = new Complex[newWidth];
			Complex[] tempSingleG = new Complex[newWidth];
			Complex[] tempSingleB = new Complex[newWidth];
			for (int i =0; i<newWidth; i++){
				tempSingleR[i] = rCom[i][row];
				tempSingleG[i] = gCom[i][row];
				tempSingleB[i] = bCom[i][row];
			}
			tempSingleR = fft.transform(tempSingleR, TransformType.INVERSE);
			tempSingleG = fft.transform(tempSingleG, TransformType.INVERSE);
			tempSingleB = fft.transform(tempSingleB, TransformType.INVERSE);
			for (int i=0; i<newWidth; i++){
				rCom[i][row] = tempSingleR[i];
				gCom[i][row] = tempSingleG[i];
				bCom[i][row] = tempSingleB[i];
			}
		}
		System.out.println("Finsihed inversion-FFT by rows. Finished inverted FFT.");
	}
	
	//Flip function.
	//This function takes the rCom and flips it - replaces the quarters 1 with 4 and 2 with 3 to visualize the center-alignment.
	public void exec_FLIP(){
		//Helper variables. Half width, half height...
		int halfWidth = newWidth/2;
		int halfHeight = newHeight/2;
		//...and a temporary copy of our table so we get a place to dump stuff into.
		Complex[][] temp = new Complex[newWidth][newHeight];
		
		//Debug:
		//System.out.println("newWidth / newHeight: "+newWidth+" "+newHeight);
		//System.out.println("halfwidth: "+halfWidth);
		//System.out.println("halfheight: "+halfHeight);
		
		for(int i=0; i<newWidth; i++){
			for (int j=0; j<newHeight; j++){
				//First slice - i<= half and j<=half.
				//Swap the elements of that slice with the 4th slice.
				if(i<halfWidth && j<halfHeight){
					temp[i][j] = rCom[i][j];
					rCom[i][j] = rCom[halfWidth+i][halfHeight+j];
					rCom[halfWidth+i][halfHeight+j] = temp[i][j];
					temp[i][j] = Complex.ZERO; //Purge it just to be sure.
					
					temp[i][j] = gCom[i][j];
					gCom[i][j] = gCom[halfWidth+i][halfHeight+j];
					gCom[halfWidth+i][halfHeight+j] = temp[i][j];
					temp[i][j] = Complex.ZERO; //Purge it just to be sure.
					
					temp[i][j] = bCom[i][j];
					bCom[i][j] = bCom[halfWidth+i][halfHeight+j];
					bCom[halfWidth+i][halfHeight+j] = temp[i][j];
					temp[i][j] = Complex.ZERO; //Purge it just to be sure.
				}
				//Second slice - i>half and j<=half.
				//Swap these with the 3rd slice - i<=half and j>half.
				if(i>halfWidth && j<halfHeight){
					temp[i][j] = rCom[i][j];
					rCom[i][j] = rCom[i-halfWidth][j+halfHeight];
					rCom[i-halfHeight][halfHeight+j] = temp[i][j];
					temp[i][j] = Complex.ZERO;
					
					temp[i][j] = gCom[i][j];
					gCom[i][j] = gCom[i-halfWidth][j+halfHeight];
					gCom[i-halfHeight][halfHeight+j] = temp[i][j];
					temp[i][j] = Complex.ZERO;
					
					temp[i][j] = bCom[i][j];
					bCom[i][j] = bCom[i-halfWidth][j+halfHeight];
					bCom[i-halfHeight][halfHeight+j] = temp[i][j];
					temp[i][j] = Complex.ZERO;
				}
			}
		}
		updateSeparates();
	}
	
	//Logarithmic normalization.
	//A twist on our normalization function, instead logs each value first and then normalizes to fit 0-255 range.
	private void exec_NORM(){
		//First, min and max values for all three colours and all two variables.
		double rPowMax = rVizPow[0][0];
		double rFrqMax = rVizFrq[0][0];
		
		double gPowMax = gVizPow[0][0];
		double gFrqMax = gVizFrq[0][0];

		double bPowMax = bVizPow[0][0];
		double bFrqMax = bVizFrq[0][0];
		
		//First loop - gather maximums and minimums of the log'd values.
		for(int i=0; i<newWidth; i++){
			for(int j=0; j<newHeight; j++){
				//DISREGARD THE PREVIOUS MATH, IT WAS PURE ASINE BULLSHIT.
				// THIS IS YOUR ONE TRUE GOD: http://homepages.inf.ed.ac.uk/rbf/HIPR2/pixlog.htm
				
				//Reds:
				if(this.rVizPow[i][j] > rPowMax) rPowMax = this.rVizPow[i][j];
				if(this.rVizFrq[i][j] > rFrqMax) rFrqMax = this.rVizFrq[i][j];
				
				//Greens:
				if(this.gVizPow[i][j] > gPowMax) gPowMax = this.gVizPow[i][j];
				if(this.gVizFrq[i][j] > gFrqMax) gFrqMax = this.gVizFrq[i][j];
				
				//Blues:
				if(this.bVizPow[i][j] > bPowMax) bPowMax = this.bVizPow[i][j];
				if(this.bVizFrq[i][j] > bFrqMax) bFrqMax = this.bVizFrq[i][j];
			}
		}
		//Debug:
		//System.out.println("Power Values:");
		//System.out.println("rPowMax: "+rPowMax);
		//System.out.println("rFrqMax: "+rFrqMax);

		//System.out.println("gPowMax: "+rPowMax);
		//System.out.println("gFrqMax: "+rFrqMax);
		
		//System.out.println("bPowMax: "+rPowMax);
		//System.out.println("bFrqMax: "+rFrqMax);
		
		// Constant values used for the normalization formula (refer to article above):
		double rCP = 255 / Math.log(1 + Math.abs(rPowMax));
		double gCP = 255 / Math.log(1 + Math.abs(gPowMax));
		double bCP = 255 / Math.log(1 + Math.abs(bPowMax));
		
		double rCF = 255 / Math.log(1 + Math.abs(rFrqMax));
		double gCF = 255 / Math.log(1 + Math.abs(gFrqMax));
		double bCF = 255 / Math.log(1 + Math.abs(bFrqMax));
		
		//Second pass: Each value is shifted according to a simple formula.
		for (int i=0; i<newWidth; i++){
			for(int j=0; j<newHeight; j++){
				
				//Normalizing Powers:
				this.rVizPow[i][j] = rCP * Math.log(1 + Math.abs(this.rVizPow[i][j]));
				this.gVizPow[i][j] = gCP * Math.log(1 + Math.abs(this.gVizPow[i][j]));
				this.bVizPow[i][j] = bCP * Math.log(1 + Math.abs(this.bVizPow[i][j]));
				
				//Normalizing Frequencies:
				this.rVizFrq[i][j] = rCF * Math.log(1 + Math.abs(this.rVizFrq[i][j]));
				this.gVizFrq[i][j] = gCF * Math.log(1 + Math.abs(this.gVizFrq[i][j]));
				this.bVizFrq[i][j] = bCF * Math.log(1 + Math.abs(this.bVizFrq[i][j]));
			}
		}
		
	}
	
	//Visualization function (DEBUGMODE)
	//Turns the Magnitude (real) spectrum into an image that can be displayed.
	public void visualize(String mode){
		super.initializeAltImage();
		exec_NORM();
		
		for(int i = 0; i<newWidth; i++){
			for(int j=0; j<newHeight;j++){
				int r = 0; int g = 0; int b = 0;
				
				if(mode.equals("REAL")){
					r = (int) rCom[i][j].getReal();
					g = (int) gCom[i][j].getReal();
					b = (int) bCom[i][j].getReal();
					
					r = super.safetyCheck(r);
					g = super.safetyCheck(g);
					b = super.safetyCheck(b);
				}
				
				
				if(mode.equals("FOUR")){
					r = (int) rVizPow[i][j];
					g = (int) gVizPow[i][j];
					b = (int) bVizPow[i][j];
					
					//r = super.safetyCheck(r);
					//g = super.safetyCheck(g);
					//b = super.safetyCheck(b);
				}
				
				Color col = new Color(r,g,b);
				super.altimg.setRGB(i,j,col.getRGB());
			}
		}
	}
	
	
	//Filter functions.
	//IMPORTANT : DO NOT INVOKE UNLESS STANDARD FFT HAS BEEN INVOKED, OTHERWISE YOU'RE IN DEEP SHIT, BROSKI.
	
	//How (perfect) filtering works: essentially, we make a circle in the center of the frequency domain (aka the fourier transformed image).
	// Then, we either purge everything inside or outside the circle. Sometimes we make a band and purge stuff in and out of it.
	// It's important to note those are PERFECT filters. It means the circle/band is sharp, strict 0s and 1s. If we do that, the result
	// will 'suffer' from banding on the end image. On the OTHER hand, making non-perfect filters removes that effect, but we need to
	// fiddle with fuzzy/gradiented circles. These algos work on perfect filters, so take note.
	
	//A quickie function that calculates the range of a point from point half-and-half.
	private double calcRange(double x, double y){
		double mW = newWidth/2;
		double mH = newHeight/2;
		
		return Math.sqrt((mW-x)*(mW-x)+(mH-y)*(mH-y));
	}
	
	//High Pass, meaning everything higher than the limit / circle range gets a pass. Everything below is turned into the power of the Void.
	public void filterHighpass(double limit){
		//Run the table.
		for (int i=0; i<newWidth; i++){
			for (int j=0; j<newHeight; j++){
				//Check if the absolute value of the number is lower than the cutoff.
				// If so, zero the value.
				if (calcRange(i,j) < limit){
					this.rCom[i][j] = Complex.ZERO;
					this.gCom[i][j] = Complex.ZERO;
					this.bCom[i][j] = Complex.ZERO;
				}
			}
		}
		//Done? Update the visualized values.
		updateSeparates();
	}
	
	//The ur-lowpass. Ditto, just for above.
	public void filterLowpass(double limit){
		for (int i=0; i<newWidth; i++){
			for (int j=0; j<newHeight; j++){
				//Check if the absolute value of the number is lower than the cutoff.
				// If so, zero the value.
				if (calcRange(i,j) > limit){
					rCom[i][j] = Complex.ZERO;
					gCom[i][j] = Complex.ZERO;
					bCom[i][j] = Complex.ZERO;
				}
			}
		}
		//Done? Update the visualized values.
		updateSeparates();
	}	
	
	//The ur-bandpass filter. The user selects the lower border and the upper border and we purge (zero-ify) everything not between them.
	public void filterBandpass(double borderofLife, double borderofDeath){
		//Travel through the entire complex array.
		for (int i=0; i<newWidth; i++){
			for (int j=0; j<newHeight; j++){
				//Check if the REAL value is higher than borderofLife, and lower than borderofDeath (haha Perfect Cherry Blossom reference)
				if (calcRange(i,j) < borderofLife || calcRange(i,j) > borderofDeath){
					rCom[i][j] = Complex.ZERO;
					gCom[i][j] = Complex.ZERO;
					bCom[i][j] = Complex.ZERO;
				}
			}
		}
		updateSeparates();
	}
	
	//The ur-bandblock filter. Reverse of bandpass. If it's between, it's gone.
	public void filterBandblock(double borderofLife, double borderofDeath){
		for (int i=0; i<newWidth; i++){
			for (int j=0; j<newHeight; j++){
				if (calcRange(i,j) > borderofLife && calcRange(i,j) < borderofDeath){
					rCom[i][j] = Complex.ZERO;
					gCom[i][j] = Complex.ZERO;
					bCom[i][j] = Complex.ZERO;
				}
			}
		}
		updateSeparates();
	}
	
	//The edge-detect filter.
	//This is basically a bandblock filter, only limited to work in the 2nd and 3rd slice of the picture.
	public void filterEdge(double borderofLife, double borderofDeath){
		for (int i=0; i<newWidth; i++){
			for (int j=0; j<newHeight; j++){
				//2nd slice.
				if(i > newWidth/2 && j<newHeight/2){
					if (calcRange(i,j) > borderofLife && calcRange(i,j) < borderofDeath){
						rCom[i][j] = Complex.ZERO;
						gCom[i][j] = Complex.ZERO;
						bCom[i][j] = Complex.ZERO;
					}
				}
				if (i < newWidth/2 && j> newHeight/2){
					if (calcRange(i,j) > borderofLife && calcRange(i,j) < borderofDeath){
						rCom[i][j] = Complex.ZERO;
						gCom[i][j] = Complex.ZERO;
						bCom[i][j] = Complex.ZERO;
					}
				}
			}
		}
		updateSeparates();
	}
	
	//Displacement filter.
	//The formula given to us should move the picture in some manner, as in, scroll it around. Let's see what happens.
	public void filterDisplacement(double k, double l){
		for (int i=0; i<newWidth; i++){
			for (int j=0; j<newHeight; j++){
				double firstPart = (-i*k*2*Math.PI)/newWidth;
				double secondPart = (-j*l*2*Math.PI)/newHeight;
				rCom[i][j] = rCom[i][j].multiply(firstPart + secondPart + (i+j)*Math.PI);
				gCom[i][j] = gCom[i][j].multiply(firstPart + secondPart + (i+j)*Math.PI);
				bCom[i][j] = bCom[i][j].multiply(firstPart + secondPart + (i+j)*Math.PI);
			}
		}
	}
}
