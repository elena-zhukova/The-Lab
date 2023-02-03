//-----------------------------------------
// 
// CLASS		: LiquidContainer.java
//
// REMARKS		: Abstract superclass for all the serums and blood sample
//
//-----------------------------------------
package equipment;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.ImageLoader;

public abstract class LiquidContainer {

	private double xPos, yPos;
	private BufferedImage img;
	
	public LiquidContainer(int x, int y, String str) {
		xPos = x;
		yPos = y;
		img = ImageLoader.loadImage(str);
	}
	
	public double getY() {
		return yPos;
	}

	public double getX() {
		return xPos;
	}

	public double getWidth() {
		return img.getWidth();
	}

	public double getHeight() {
		return img.getWidth();
	}
	
	public void draw(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		
		g2.translate(xPos, yPos);
		g2.drawImage(img, -img.getWidth()/2, -img.getHeight()/2, img.getWidth(), img.getHeight(), null);
		
		g2.setTransform(at);
	}

}
