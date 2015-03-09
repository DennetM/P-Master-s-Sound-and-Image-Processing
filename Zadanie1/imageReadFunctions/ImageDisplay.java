package imageReadFunctions;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

								
@SuppressWarnings("serial")		// This extension is necessary to throw the entire thing into a JPane inside a JFrame.
public class ImageDisplay extends JPanel{
	
	// Variables
	protected BufferedImage img = null; // Our image
	protected int x = 0; // Image width.
	protected int y = 0; // Image height.
	
	
	// Constructor
	public ImageDisplay(ImageReading x){
		this.img = x.getImage();
		this.x = this.img.getWidth();
		this.y = this.img.getHeight();
		
		//Checker:
		System.out.println("Image width: " + this.x + "px");
		System.out.println("Image height: " + this.y + "px");
	}
	
	// Functions
	// The painting function - draw the loaded image at 10x 10y from top.
								// Graphics is the universal drawing class. Doesn't need to be constructed to work.
	public void paintComponent(Graphics g){
		//Scale drawImage overwrite.
		//What image / Anchor at X and Y / Finish Anchor at X, Y / Start Copying from X and Y / Finish Copying from X and Y.
		g.drawImage(this.img, -20, 0, 770, 550, 0, 0, x, y, null);
	}
}
