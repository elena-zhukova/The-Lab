//-----------------------------------------
// 
// CLASS		: Restart.java
//
// REMARKS		: This is class for creating start and restart buttons
//
//-----------------------------------------

package lab;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import factory.Shape;

public class Restart extends Shape
{

	
	public Restart(double x, double y, String img) 
	{
		super(x,y,img);
	}
	
	public void drawButton(Graphics2D g2) 
	{
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
	
	//Source code: IAT 265 lab 11
	public boolean clicked(double x, double y)
	{
		boolean clicked = false;
		
		if(x > (xPos - ((double) img.getWidth())/2) && x < (xPos + ((double) img.getWidth())/2) && y > (yPos - ((double) img.getHeight())/2) && y < (yPos + ((double) img.getHeight())/2)) 
			clicked = true;
		
		return clicked;
	}

}