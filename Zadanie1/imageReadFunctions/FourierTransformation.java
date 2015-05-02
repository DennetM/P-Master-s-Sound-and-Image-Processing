package imageReadFunctions;

//Note: For basic FFT, we're using Apache's deployment of the function.
//Because really, IT'S JUST SIMPLE MATH. Take a formula and apply it. Too many chances to bug out if I do it by myself.
import java.awt.Color;
import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;


//Fourtier Transformation class extending ImageReading.
//In practice this is no different from ImageReading, and exists purely NOT to clog up space in the main class.
//...which is already bloated if you ask me.
public class FourierTransformation extends ImageReading {
	
	//Variables
	protected double[] r; //Flat red, what we'll be transforming.
	protected double[] g; //Flat green.
	protected double[] b; //Flat blue.
	
	protected Complex[] rCom; //red Complex.
	public double[] rRe; //red Real.
	public double[] rIm; //red Imaginary.
	
	protected Complex[] gCom; //green Complex.
	public double[] gRe; //green Real.
	public double[] gIm; //green Imaginary.
	
	protected Complex[] bCom; //blue Complex.
	public double[] bRe; //blue Real.
	public double[] bIm; //blue Imaginary.
	
	public static enum Type {FOWARD, INVERSE} //A type to control if we want to make it a standard FFT or inverse FFT.
												//This is required for the thing to work in the first place, so mind!
	
	FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD); // Our FFT. Using the Standard formula.
																					// Again, don't ask. Better left alone.
	
	//=============================
	//=============================
	
	//Constructor
	//...which is essentially our ImageReading constructor.
	//Except we enrich it to plaster the R G B values from the image into the table.
	public FourierTransformation(String x){
		super(x); //We-do what we did in the original.
		
		//Initialize the table values.
		this.r = new double[this.img.getWidth()*this.img.getHeight()];
		this.g = new double[this.img.getWidth()*this.img.getHeight()];
		this.b = new double[this.img.getWidth()*this.img.getHeight()];
		
		//Map the values one by one.
		for (int i = 0; i<this.img.getWidth(); i++){
			for(int j = 0; j<this.img.getHeight(); j++){
				Color tempCol = new Color(this.img.getRGB(i, j));
				r[this.img.getWidth()*i + j] = tempCol.getRed();
				g[this.img.getWidth()*i + j] = tempCol.getGreen();
				b[this.img.getWidth()*i + j] = tempCol.getBlue();
			}
		}
		//At this point we have our RGB tables filled by rows. We can now get the data going.
	}
	

	//=============================
	//=============================
	
	//Functions, also known as "where things get interesting" section.
	//pad() - checks the length of R, G and B tables and compares them to the nearest power of 2.
	// This is a requirement for FFT to work. If we don't fit, we jump higher and pad the tables with 0s.
	private void pad(){
		//This is the power index (aka the power value) we're looking for.
		int powerIndex = 1;
		//While the index is lesser than the number we're dealing with AND lower than 2^23 (because come on I make pics that big sometimes).
		while(powerIndex <(this.img.getWidth()*this.img.getHeight()) && powerIndex < 8388608){
			powerIndex *= 2;
		}
		
		//Pad it.
		this.r = Arrays.copyOf(r, powerIndex);
		this.g = Arrays.copyOf(g, powerIndex);
		this.b = Arrays.copyOf(b, powerIndex);
	}
	
	//The main standard FFT function.
	//Type denotes the necessary type - either FORWARD (simple) or INVERSE.
	public void standardFFT(TransformType type){
		//First thing's first - we need to pad stuff out.
		pad();
		//Now, we transform using the fft and its type. We get a single array of complex numbers aka EVIL.
		this.rCom = fft.transform(this.r, type);
		this.gCom = fft.transform(this.g, type);
		this.bCom = fft.transform(this.b, type);
		//Finally, we separate the reals from acid trips (imaginaries) and paste them to the right tables.
		
		//Initialize them.
		//(Fun fact - all lengths are the same, I just put them differently for flavour purposes.)
		rRe = new double[rCom.length];
		rIm = new double[rCom.length];
		gRe = new double[gCom.length];
		gIm = new double[gCom.length];
		bRe = new double[bCom.length];
		bIm = new double[bCom.length];
		//And fill them.
		for(int i=0; i<rCom.length; i++){
			rRe[i] = rCom[i].getReal();
			rIm[i] = rCom[i].getImaginary();
			
			gRe[i] = gCom[i].getReal();
			gIm[i] = gCom[i].getImaginary();
			
			bRe[i] = bCom[i].getReal();
			bIm[i] = bCom[i].getImaginary();
		}
		//TO-DO Paula: Wyœwietlane widma mocy (rgbRe) i czêstotliwoœci (rgbIm) dla obrazu.
		
	}
}
