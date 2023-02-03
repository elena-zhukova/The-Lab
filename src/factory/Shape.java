//-----------------------------------------
// 
// CLASS		: Shape.java
//
// REMARKS		: Abstract superclass for well plate and cheat sheet needed for factory to create objects
//
//-----------------------------------------
package factory;

import java.awt.image.BufferedImage;

import util.ImageLoader;

public abstract class Shape 
{

	protected double xPos, yPos;
	protected BufferedImage img;
	
	public Shape(double x, double y, String str) 
	{
		xPos = x;
		yPos = y;
		img = ImageLoader.loadImage(str);
	}
	
	

}
