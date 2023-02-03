//-----------------------------------------
// 
// CLASS		: WellPlate.java
//
// REMARKS		: This class is used to create a well plate which is the object where all the reactions are happening.
//					It is subclass of shape used for factory
//
//-----------------------------------------
package equipment;

import processing.core.PApplet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import factory.Shape;
import lab.CheatSheet;
import util.*;

public class WellPlate extends Shape 
{

	private WellPlateState state; //states are implemented via enum
	
	public WellPlate() 
	{
		super(500,500,"assets/empty.png");
		state = WellPlateState.EMPTY;
	}
	
	public void drawPlate(Graphics2D g2) 
	{
		AffineTransform transform = g2.getTransform();
		
		g2.translate(xPos, yPos);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);
		
		//draw perlin noise after serums are add
		if (state == WellPlateState.ACHECKED)
	        addNoise(g2, -100);
		if (state == WellPlateState.BCHECKED)
		{
	        addNoise(g2, -100);
	        addNoise(g2, -10);
		}
		if (state == WellPlateState.ALLCHECKED)
		{
	        addNoise(g2, -100);
	        addNoise(g2, -10);
	        addNoise(g2, 80);
		}

		g2.setTransform(transform);
	}

	//loading image based on current state
	public void setImage() 
	{

	    if (state == WellPlateState.EMPTY)

	        img = ImageLoader.loadImage("assets/empty.png");

	    else if (state == WellPlateState.AFILLED)

	        img = ImageLoader.loadImage("assets/noB.png");

	    else if (state == WellPlateState.BFILLED)

	        img = ImageLoader.loadImage("assets/noRh.png");
	    
	    else if (state == WellPlateState.FULL)

	        img = ImageLoader.loadImage("assets/full.png");
	    
	    else if (state == WellPlateState.ACHECKED)
	    	//image is the same as for full state because there is no reaction for B-positive blood type
	        img = ImageLoader.loadImage("assets/full.png");
	    
	    else if (state == WellPlateState.BCHECKED)

	        img = ImageLoader.loadImage("assets/BChecked.png");
	    
	    else if (state == WellPlateState.ALLCHECKED)

	        img = ImageLoader.loadImage("assets/AllChecked.png");
	    

	}
	
	/*
	 * Source code: IAT 265 week 11 lab and lecture demo
	 */
	//draw Perlin noise at the time of reaction with serums
	public void addNoise(Graphics2D g2, int pos) 
	{
		
		float xStart = (float)Math.random()*5;
		float xSeed = xStart;
		float ySeed = (float)Math.random()*5;
		PApplet pa = new PApplet();
		
		float noiseFactor;
		
		AffineTransform at1 = g2.getTransform();
		g2.translate(pos, 0);
		
		for (int y = 0; y <= 20; y += 5) 
		{
			ySeed += 0.3;
			xSeed = xStart;
			
			for (int x = 0; x <= 20; x += 5) 
			{
				xSeed += 0.6;
				noiseFactor = pa.noise(xSeed, ySeed);
				
				AffineTransform at = g2.getTransform();
				
				g2.translate(x, y);
				g2.rotate(noiseFactor * 7*Math.PI);
				
				float diameter = noiseFactor * 30;
				
				int colorR = (int) (noiseFactor * 255);
				int colorB = (int) (noiseFactor * 255);
				
				//draw shapes according to noise
				g2.setColor(new Color(colorR, 70, colorB));
				g2.fill(new Ellipse2D.Float(-diameter/2, -diameter/4, diameter/2, diameter/3));
				g2.setColor(new Color(colorR, 150, colorB));
				g2.fill(new Ellipse2D.Float(-diameter/2, -diameter/4, diameter/2, diameter/3));
				g2.setColor(new Color(colorR, 20, colorB));
				g2.draw(new Line2D.Float(-diameter/2, -diameter/4, diameter/2, diameter/3));
				
				g2.setTransform(at);
			}
		}
		g2.setTransform(at1);
	}

	
	public void setState(WellPlateState s)
	{
		state = s;
	}
	
	public WellPlateState getState()
	{
		return state;
	}
	
	public double getY() 
	{
		return yPos;
	}

	public double getX() 
	{
		return xPos;
	}
	
	public double getWidth() 
	{
		return img.getWidth();
	}

	public double getHeight() 
	{
		return img.getHeight();
	}
	
	public void setPos(double mouseX, double mouseY) 
	{
		xPos = mouseX;
		yPos = mouseY;
		
	}
	
	//source code: IAT 265 lab 10
	public boolean clicked(double x, double y)
	{
		boolean clicked = false;
			
		if(x > (xPos - ((double) img.getWidth())/2) && x < (xPos + ((double) img.getWidth())/2) && y > (yPos - ((double) img.getHeight())/2) && y < (yPos + ((double) img.getHeight())/2)) 
			clicked = true;
			
		return clicked;
	}
		
	public boolean hit(CheatSheet cs)
	{
		boolean hit = false;
			
		if (Math.abs(cs.getX()-xPos) < cs.getWidth()/2 && Math.abs(cs.getY()-yPos) < cs.getHeight()/2)
		{
			hit = true;
		}
		return hit;
	}
}
