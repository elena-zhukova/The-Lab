//-----------------------------------------
// 
// CLASS		: CheatSheet.java
//
// REMARKS		: This class is creates cheat sheet which can be enlarged to compare results of the test
//
//-----------------------------------------

package lab;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import factory.Shape;

public class CheatSheet extends Shape
{

	//fsm to chech if the cheat sheet is enlarged
	private static final boolean SMALL = false;
	private static final boolean LARGE = true;
	
	private boolean state; //current state of cheat sheet
	
	private double scale; //scale to enlarge cheat sheet
	
    //------------------------------------------------------
    // CONSTRUCTOR	:	CheatSheet() 
    //
    // PURPOSE		:	Constructor of CheatSheet which instantiates state of the object, its scale and sends position and image to superclass
	//
    // PARAMETERS	:
    //     				none
    //
    //------------------------------------------------------
	public CheatSheet() 
	{
		super(825, 200, "assets/CheatSheet.png");
		state = SMALL; //cheat sheet is created shrinked
		scale = 0.5;
	}//CheatSheet

	//------------------------------------------------------
    // METHOD		:	drawCheatSheet(Graphics2D g2)
    //
    // PURPOSE		:	painting cheatsheet by transforming g2 and drawing image
	//
    // PARAMETERS	:
    //     				Graphics2D g2
	//
	// RETURN VALUE	: 	
	//					no return value
    //
    //------------------------------------------------------
	public void drawCheatSheet(Graphics2D g2) 
	{
		AffineTransform transform = g2.getTransform();
		
		g2.translate(xPos, yPos);
		g2.scale(scale, scale);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
	
	//------------------------------------------------------
    // METHOD		:	setState(boolean s)
    //
    // PURPOSE		:	updating state of cheat sheet
	//
    // PARAMETERS	:
    //     				boolean s - new state
	//
	// RETURN VALUE	: 	
	//					no return value
    //
    //------------------------------------------------------
	public void setState(boolean s)
	{
		state = s;
		setScale(state);
	}
	
	//------------------------------------------------------
    // METHOD		:	getState()
    //
    // PURPOSE		:	checking state of cheat sheet
	//
    // PARAMETERS	:
    //     				no parameters
	//
	// RETURN VALUE	: 	
	//					boolean state - returns current state
    //
    //------------------------------------------------------
	public boolean getState()
	{
		return state;
	}
	
	//Source: IAT 265 lab 10
	//------------------------------------------------------
    // METHOD		:	clicked(double x, double y)
    //
    // PURPOSE		:	checking is cheat sheet was clicked
	//
    // PARAMETERS	:
    //     				double x - horizontal position of mouse
	//					double y - vertical position of mouse
	//
	// RETURN VALUE	: 	
	//					boolean clicked - returns if cheat sheet was clicked
    //
    //------------------------------------------------------
	public boolean clicked(double x, double y)
	{
		boolean clicked = false;
		
		if(x > (xPos - ((double) img.getWidth())/2*scale) && x < (xPos + ((double) img.getWidth())/2*scale) 
				&& y > (yPos - ((double) img.getHeight())/2*scale) && y < (yPos + ((double) img.getHeight())/2*scale)) 
			clicked = true;
		
		return clicked;
	}

	//------------------------------------------------------
    // METHOD		:	setScale(boolean s)
    //
    // PURPOSE		:	updating scale of cheat sheet based on current state
	//
    // PARAMETERS	:
    //     				boolean s - new state
	//
	// RETURN VALUE	: 	
	//					no return value
    //
    //------------------------------------------------------
	public void setScale(boolean s) 
	{
		if (s == SMALL)
			scale = 0.5;
		else
			scale = 1;
		
	}
	
	//------------------------------------------------------
    // METHOD		:	getY()
    //
    // PURPOSE		:	checking y position of cheat sheet
	//
    // PARAMETERS	:
    //     				no parameters
	//
	// RETURN VALUE	: 	
	//					double yPos - vertical position
    //
    //------------------------------------------------------
	public double getY() 
	{
		return yPos;
	}

	//------------------------------------------------------
    // METHOD		:	getX()
    //
    // PURPOSE		:	checking x position of cheat sheet
	//
    // PARAMETERS	:
    //     				no parameters
	//
	// RETURN VALUE	: 	
	//					double xPos - horizontal position
    //
    //------------------------------------------------------
	public double getX() 
	{
		return xPos;
	}
	
	//------------------------------------------------------
    // METHOD		:	getWidth() 
    //
    // PURPOSE		:	checking width of cheat sheet
	//
    // PARAMETERS	:
    //     				no parameters
	//
	// RETURN VALUE	: 	
	//					double img.getWidth() - width of the image
    //
    //------------------------------------------------------
	public double getWidth() 
	{
		return img.getWidth();
	}

	//------------------------------------------------------
    // METHOD		:	getHeight()
    //
    // PURPOSE		:	checking height of cheat sheet
	//
    // PARAMETERS	:
    //     				no parameters
	//
	// RETURN VALUE	: 	
	//					double img.getHeight() - height of the image
    //
    //------------------------------------------------------
	public double getHeight() 
	{
		return img.getHeight();
	}
}
