//-----------------------------------------
// 
// CLASS		: Pipette.java
//
// REMARKS		: Abstract superclass for pipettes for samples and serums
//
//-----------------------------------------
package equipment;

import static util.ImageLoader.loadImage;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.ImageLoader;
import util.PipetteState;

public abstract class Pipette 
{

	protected double xPos;
	protected double yPos;
	protected BufferedImage img;
	
	//default position of pipettes
	private double xDefault;
	private double yDefault;
	
	private PipetteState state;
	
	public Pipette(double x, double y, String str) 
	{
		xPos = x;
		xDefault = xPos;
		yPos = y;
		yDefault = yPos;
		img = loadImage(str);
		state = PipetteState.EMPTY;
	}

	public void drawPipette(Graphics2D g2) 
	{
		AffineTransform transform = g2.getTransform();
		
		g2.translate(xPos, yPos);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
	
	public void setImage() 
	{

	    if (state == PipetteState.EMPTY)
	    {
	    	if (this instanceof SamplePipette)
	    		img = ImageLoader.loadImage("assets/SamplePipette.png");
	    	else if (this instanceof SerumPipette)
	    		img = ImageLoader.loadImage("assets/SerumPipette.png"); 
	    }
	    
	    else if (state == PipetteState.FULL)
	    {
	    	if (this instanceof SamplePipette)
	    		img = ImageLoader.loadImage("assets/SamplePipetteFull.png");
	    	else if (this instanceof SerumPipette)
	    		img = ImageLoader.loadImage("assets/SerumPipetteFull.png");
	    }

	}
	
	public void setState(PipetteState s) 
	{
		state = s;
	}
	
	public PipetteState getState()
	{
		return state;
	}
	
	public void setPos(double mouseX, double mouseY) 
	{
		xPos = mouseX;
		yPos = mouseY;
		
	}
	
	//------------------------------------------------------
    // METHOD		:	hit(WellPlate wp)
    //
    // PURPOSE		:	checking for collision with well plate
	//
    // PARAMETERS	:
    //     				WellPlate wp
	//
	// RETURN VALUE	: 	
	//					boolean hit - if well plate was hit
    //
    //------------------------------------------------------
	public boolean hit(WellPlate wp)
	{
		boolean hit = false;
		
		if (Math.abs(wp.getX()-xPos) < wp.getWidth()/2 && Math.abs(wp.getY()-yPos) < wp.getHeight()/2)
		{
			hit = true;
		}
		return hit;
	}
	
	//------------------------------------------------------
    // METHOD		:	hit(LiquidContainer lc)
    //
    // PURPOSE		:	depending on subclass of both pipette and liquid container the collision between pipette and sample/serum determined
	//
    // PARAMETERS	:
    //     				LiquidContainer lc - can be serum or sample
	//
	// RETURN VALUE	: 	
	//					boolean hit - if liquid container was hit
    //
    //------------------------------------------------------
	public boolean hit(LiquidContainer lc)
	{
		boolean hit = false;
		
		//serumPipette only collides with serum and SamplePipette only with Sample
		if ((this instanceof SamplePipette && lc instanceof Sample) ||
				(this instanceof SerumPipette && lc instanceof Serum))
		{
			if (Math.abs(lc.getX()-xPos) < lc.getWidth()/2 && Math.abs(lc.getY()-yPos) < lc.getHeight()/2)
			{
				hit = true;
			}
		}
		return hit;
	}
	
	//source code: IAT 265 lab 10
	public boolean clicked(double x, double y)
	{
		boolean clicked = false;
		
		if(x > (xPos - ((double) img.getWidth())/2) && x < (xPos + ((double) img.getWidth())/2) && y > (yPos - ((double) img.getHeight())/2) && y < (yPos + ((double) img.getHeight())/2)) 
			clicked = true;
		
		return clicked;
	}
	
	public void revertDefaultPos() {
		xPos = xDefault;
		yPos = yDefault;
		
	}
	
}
