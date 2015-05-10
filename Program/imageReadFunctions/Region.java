package imageReadFunctions;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Region {

	//Variables
	public int[][] regionMap; //This map is -essentially- our region. 0s do not belog to it, 1s do.
	private int[][] memoryMap; //This is our memory map. Every position is initialized as 0, meaning it's not checked.
								//When we walk the table, each time we perform a check on a square we'll mark it as visited.
								//This is necessary so we don't loop endlessly.
	
	public double thresh; //This is the value we'll use to compare the possibility of adding pixels to the region.
							//Threshold is the Euclidean Distance between the pixels. Essentially SQRT of the sum of ^2 of differences
							//between R, G and B values.
	
	//Ur-standard variables.
	//The image in three forms, for easier viewing.
	int[][] r;
	int[][] g;
	int[][] b;
	int mapWidth;
	int mapHeight;
	
	//=============================
	//Constructor:
	public Region(BufferedImage img, double val, int seedX, int seedY){
		//Initialize the values.
		this.mapWidth = img.getWidth();
		this.mapHeight = img.getHeight();
		
		this.regionMap = new int[mapWidth][mapHeight];
		this.memoryMap = new int[mapWidth][mapHeight];
		this.r = new int[mapWidth][mapHeight];
		this.g = new int[mapWidth][mapHeight];
		this.b = new int[mapWidth][mapHeight];
		
		this.thresh = val;
		
		//Single loop that goes over all three tables, fixing them.
		for(int i=0; i<mapWidth; i++){
			for(int j=0; j<mapHeight; j++){
				//Step one - zero our regionMap.
				regionMap[i][j] = 0;
				//Step two - zero our memoryMap.
				memoryMap[i][j] = 0;
				//Step three - take the image and separate it into R, G and B values.
				Color col = new Color(img.getRGB(i, j));
				r[i][j] = col.getRed();
				g[i][j] = col.getGreen();
				b[i][j] = col.getBlue();
			}
		}
		System.out.println("Region values initialized.");
		//We done.
		System.out.println("Beginning region growth with seed values.");
		
		//Mark that we've started in the memory.
		this.memoryMap[seedX][seedY] = 1;
		grow(seedX,seedY);
	}
	
	//=============================
	//Functions:
	
	//grow function - this is a recursive function that enters a pixel, marks it in the region map and checks for other pixels to enter.
	//					Each time it checks a pixel, it marks it on the memory map. The function finishes its life when there are no more.
	//					pixels to scurry around for.
	private void grow(int i, int j){
		//First step - mark that we've entered a pixel.
		this.regionMap[i][j] = 1;
		System.out.println("Pixel ["+i+"],["+j+"] added to region.");
		
		double distance; //The distance value between pixels we'll be comparing.
		
		//The function checks pixels in the following order: Left, Top, Right, Bottom.
		//The following criteria must be met for the pixel to qualify for checkup:
			//	> The pixel must be within the image (i,j cannot be negatives, i,j < width and height)
			//  > The value in the memoryMap for this pixel must be 0.
		//Following that, we need to check if the threshold is within norm.
		
		//Check the left pixel. (i-1, j)
		if((i-1)>=0 && (j>=0) && (i-1)<this.mapWidth && j<this.mapHeight && this.memoryMap[i-1][j] == 0){
			//It's qualified, mark it.
			this.memoryMap[i-1][j] = 1;
			//Calculate the value.
			distance = Math.sqrt(Math.pow((r[i][j]-r[i-1][j]), 2) + Math.pow((g[i][j]-g[i-1][j]), 2) + Math.pow((b[i][j]-b[i-1][j]), 2));
			//Check if it qualifies.
			if(distance<=this.thresh){
				//If it does, do the timewarp again.
				grow(i-1,j);
			}
		}
		
		//Debug
		
		//Check the top pixel (i, j+1)
		if(i>=0 && ((j+1)>=0) && i<this.mapWidth && (j+1)<this.mapHeight && this.memoryMap[i][j+1] == 0){
			//It's qualified, mark it.
			this.memoryMap[i][j+1] = 1;
			//Calculate the value.
			distance = Math.sqrt(Math.pow((r[i][j]-r[i][j+1]), 2) + Math.pow((g[i][j]-g[i][j+1]), 2) + Math.pow((b[i][j]-b[i][j+1]), 2));
			//Check if it qualifies.
			if(distance<=this.thresh){
				//If it does, do the timewarp again.
				grow(i,j+1);
			}
		}
		
		//Check the right pixel (i+1, j)
		if((i+1)>=0 && (j>=0) && (i+1)<this.mapWidth && j<this.mapHeight && this.memoryMap[i+1][j] == 0){
			//It's qualified, mark it.
			this.memoryMap[i+1][j] = 1;
			//Calculate the value.
			distance = Math.sqrt(Math.pow((r[i][j]-r[i+1][j]), 2) + Math.pow((g[i][j]-g[i+1][j]), 2) + Math.pow((b[i][j]-b[i+1][j]), 2));
			//Check if it qualifies.
			if(distance<=this.thresh){
				//If it does, do the timewarp again.
				grow(i+1,j);
			}
		}
		
		//Check the bottom pixel (i, j-1)
		if(i>=0 && ((j-1)>=0) && i<this.mapWidth && (j-1)<this.mapHeight && this.memoryMap[i][j-1] == 0){
			//It's qualified, mark it.
			this.memoryMap[i][j-1] = 1;
			//Calculate the value.
			distance = Math.sqrt(Math.pow((r[i][j]-r[i][j-1]), 2) + Math.pow((g[i][j]-g[i][j-1]), 2) + Math.pow((b[i][j]-b[i][j-1]), 2));
			//Check if it qualifies.
			if(distance<=this.thresh){
				//If it does, do the timewarp again.
				grow(i,j-1);
			}
		}
		/*
	*/
	//There is no specific stopw watchdog. If we run out of places to check, we'll simply stop. MemoryMap makes sure we don't check a place twice.
		//We don't need to 'wait' for this function since the RegionMap is updated each time we ENTER. When the algorithm stops, we can
		//simply retrieve our regionMap and be done with it.
	}
}
