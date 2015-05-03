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
	//Re and Im tables are tables that separate the imaginary and real values of the complex number, for visualization.
	protected Complex[][] rCom; //red Complex.
	public double[][] rRe; //red Real.
	public double[][] rIm; //red Imaginary.
	
	protected Complex[][] gCom; //green Complex.
	public double[][] gRe; //green Real.
	public double[][] gIm; //green Imaginary.
	
	protected Complex[][] bCom; //blue Complex.
	public double[][] bRe; //blue Real.
	public double[][] bIm; //blue Imaginary.
	
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
	
	//Updates the separate tables (the ones that split Real and Acid Trips for purposes of display and clarity).
	//This is invoked a lot. A LOT.
	private void updateSeparates(){
		this.rRe = new double[newWidth][newHeight];
		this.rIm = new double[newWidth][newHeight];
		this.gRe = new double[newWidth][newHeight];
		this.gIm = new double[newWidth][newHeight];
		this.bRe = new double[newWidth][newHeight];
		this.bIm = new double[newWidth][newHeight];
		
		for (int i=0; i<newWidth; i++){
			for(int j=0; j<newHeight; j++){
				this.rRe[i][j] = this.rCom[i][j].getReal();
				this.rIm[i][j] = this.rCom[i][j].getImaginary();
				this.gRe[i][j] = this.gCom[i][j].getReal();
				this.gIm[i][j] = this.gCom[i][j].getImaginary();
				this.bRe[i][j] = this.bCom[i][j].getReal();
				this.bIm[i][j] = this.bCom[i][j].getImaginary();
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
		
		//With this done we split the real and imaginary parts for easier viewing.
		updateSeparates();
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
		updateSeparates();
	}
	
	//Visualization function (DEBUGMODE)
	//Turns the Magnitude (real) spectrum into an image that can be displayed.
	public void visualize(){
		super.initializeAltImage();
		for(int i = 0; i<newWidth; i++){
			for(int j=0; j<newHeight;j++){
				int r, g, b;
				r = (int) rCom[i][j].getReal();
				g = (int) rCom[i][j].getReal();
				b = (int) rCom[i][j].getReal();
				
				r = super.safetyCheck(r);
				g = super.safetyCheck(g);
				b = super.safetyCheck(b);
				
				Color col = new Color(r,g,b);
				super.altimg.setRGB(i,j,col.getRGB());
			}
		}
	}
	
	
	//Filter functions.
	//IMPORTANT : DO NOT INVOKE UNLESS STANDARD FFT HAS BEEN INVOKED, OTHERWISE YOU'RE IN DEEP SHIT, BROSKI.
	
	//The ur-highpass. Everything below the limit is PURGED.
	public void filterHighpass(double limit){
		/*
		//Travel through the entire complex array.
				for (int i=0; i<rCom.length; i++){
					//Check if the REAL value is higher than borderofLife, and lower than borderofDeath (haha Perfect Cherry Blossom reference)
					if(rCom[i].getReal() <= limit){
						rCom[i] = Complex.ZERO; // Zerofiy it with a complex number.
					}
					if(gCom[i].getReal() <= limit){
						gCom[i] = Complex.ZERO; // Zerofiy it with a complex number.
					}
					if(bCom[i].getReal() <= limit){
						bCom[i] = Complex.ZERO; // Zerofiy it with a complex number.
					}
				}
		updateSeparates();
		*/
	}
	
	//The ur-lowpass. Ditto, just for above.
	public void filterLowpass(double limit){
		/*
		//Travel through the entire complex array.
		for (int i=0; i<rCom.length; i++){
			//Check if the REAL value is higher than borderofLife, and lower than borderofDeath (haha Perfect Cherry Blossom reference)
			if(rCom[i].getReal() >= limit){
				rCom[i] = Complex.ZERO; // Zerofiy it with a complex number.
			}
			if(gCom[i].getReal() >= limit){
				gCom[i] = Complex.ZERO; // Zerofiy it with a complex number.
			}
			if(bCom[i].getReal() >= limit){
				bCom[i] = Complex.ZERO; // Zerofiy it with a complex number.
			}
		}
		updateSeparates();
		*/
	}	
	
	//The ur-bandpass filter. The user selects the lower border and the upper border and we purge (zero-ify) everything not between them.
	public void filterBandpass(double borderofLife, double borderofDeath){
		/*
		//Travel through the entire complex array.
		for (int i=0; i<rCom.length; i++){
			//Check if the REAL value is higher than borderofLife, and lower than borderofDeath (haha Perfect Cherry Blossom reference)
			if(rCom[i].getReal() <= borderofLife && rCom[i].getReal() >= borderofDeath){
				rCom[i] = Complex.ZERO; // Zerofiy it with a complex number.
			}
			if(gCom[i].getReal() <= borderofLife && gCom[i].getReal() >= borderofDeath){
				gCom[i] = Complex.ZERO; // Zerofiy it with a complex number.
			}
			if(bCom[i].getReal() <= borderofLife && bCom[i].getReal() >= borderofDeath){
				bCom[i] = Complex.ZERO; // Zerofiy it with a complex number.
			}
		}
		updateSeparates();
		*/
	}
}
