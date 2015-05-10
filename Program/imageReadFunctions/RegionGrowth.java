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
				int r = 255*reg.regionMap[i][j];
				int g = 255*reg.regionMap[i][j];
				int b = 255*reg.regionMap[i][j];
				
				Color col = new Color(r,g,b);
				super.altimg.setRGB(i, j, col.getRGB());
			}
		}
	}

}
