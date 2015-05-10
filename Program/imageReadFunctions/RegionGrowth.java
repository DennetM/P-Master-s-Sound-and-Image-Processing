package imageReadFunctions;

import java.awt.Color;

//This is a separate class that handles image segmentation and region growth.
//Extends the basic ImageReading to have all its properties, again, not to clutter the main class.
public class RegionGrowth extends ImageReading {
	
	public RegionGrowth(String x){
		super(x);
	}
	
	//Debug:
	public void visualize(double val, int k, int l){
		Region reg = new Region(this.img, val, k, l);
		
		super.initializeAltImage();
		for(int i=0; i<this.img.getWidth(); i++){
			for(int j=0; j<this.img.getHeight(); j++){
				Color colstart = new Color(this.img.getRGB(i, j));
				int r, g, b;
				
				if (reg.regionMap[i][j] == 0){
					r = (int) (colstart.getRed() * 0.2);
					g = (int) (colstart.getGreen() * 0.2);
					b = (int) (colstart.getBlue() * 0.2);
				}
				else{
					r = colstart.getRed();
					g = colstart.getGreen();
					b = colstart.getBlue();
				}
				
				Color col = new Color(r,g,b);
				super.altimg.setRGB(i, j, col.getRGB());
			}
		}
	}

}
