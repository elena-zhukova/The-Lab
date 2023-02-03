//-----------------------------------------
// 
// CLASS		: Lab.java
//
// REMARKS		: This class is creates the lab environment
//
//-----------------------------------------

package lab;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import util.WellPlateState;

public class Lab 
{

	//position of wall
	private final static int X_WALL = 0;
	private final static int Y_WALL = 0;
	
	//position of table
	private final static int X_TABLE = 0;
	private final static int Y_TABLE = 400;
	
	//dimensions of wall and table
	private final static int WIDTH = 1100;
	private final static int HEIGHT = 400;
	
	//dimensions of shelf
	private final static int WIDTH_SHELF = 600;
	private final static int HEIGHT_SHELF = 25;
	
	//colour of elements
	private final static Color COLOUR_WALL = new Color(250, 200, 200);
	private final static Color COLOUR_TABLE = new Color(100, 70, 60);
	private static Color colourWindow;
	
	private Rectangle2D.Double table;
	private Rectangle2D.Double wall;
	private Rectangle2D.Double shelf;
	
    //------------------------------------------------------
    // CONSTRUCTOR	:	Lab() 
    //
    // PURPOSE		:	Constructor of Lab which instantiates environment and initial colour of window
	//
    // PARAMETERS	:
    //     				none
    //
    //------------------------------------------------------
	public Lab() 
	{
		wall = new Rectangle2D.Double(X_WALL, Y_WALL, WIDTH, HEIGHT);
		table = new Rectangle2D.Double(X_TABLE, Y_TABLE, WIDTH, HEIGHT);
		shelf = new Rectangle2D.Double(X_TABLE, Y_TABLE-50, WIDTH_SHELF, HEIGHT_SHELF);
		colourWindow = new Color(144, 202, 249);
	}
	
	//------------------------------------------------------
    // METHOD		:	drawLab(Graphics2D g2) 
    //
    // PURPOSE		:	painting environment by transforming g2 and drawing images
	//					Then two calls to deawWindow are made fro recursive drawing
	//
    // PARAMETERS	:
    //     				Graphics2D g2
	//
	// RETURN VALUE	: 	
	//					no return value
    //
    //------------------------------------------------------
	public void drawLab(Graphics2D g2) 
	{
		g2.setColor(COLOUR_WALL);
		g2.fill(wall);
		
		
		g2.setColor(COLOUR_TABLE);
		g2.fill(table);
		g2.fill(shelf);
		
		drawWindow(g2, WIDTH/4, HEIGHT/2, 200);
		drawWindow(g2, 3*WIDTH/4, HEIGHT/2, 200);
	}
	
	//------------------------------------------------------
    // METHOD		:	setWindowColor(WellPlateState state)
    //
    // PURPOSE		:	setting time of day based on well plate state
	//
    // PARAMETERS	:
    //     				WellPlateState state
	//
	// RETURN VALUE	: 	
	//					no return value
    //
    //------------------------------------------------------
	public void setWindowColor(WellPlateState state)
	{
		if (state == WellPlateState.EMPTY)
			colourWindow = new Color(144, 202, 249);
		else if (state == WellPlateState.AFILLED)
			colourWindow = new Color(100, 181, 246);
		else if (state == WellPlateState.BFILLED)
			colourWindow = new Color(66, 165, 245);
		else if (state == WellPlateState.FULL)
			colourWindow = new Color(121, 134, 203);
		else if (state == WellPlateState.ACHECKED)
			colourWindow = new Color(92, 107, 192);
		else if (state == WellPlateState.BCHECKED)
			colourWindow = new Color(48, 63, 159);
		else if (state == WellPlateState.ALLCHECKED)
			colourWindow = new Color(26, 35, 126);
	}
	
	//------------------------------------------------------
    // METHOD		:	drawWindow(Graphics2D g2, float x, float y, float s)
    //
    // PURPOSE		:	drawing base of window and making calls to recursion part
	//
    // PARAMETERS	:
    //     				Graphics2D g2
	//					float x - x position
	//					float y - y position
	//					float s - dimension of side
	//
	// RETURN VALUE	: 	
	//					no return value
    //
    //------------------------------------------------------
	private void drawWindow(Graphics2D g2, float x, float y, float s)
	{
		AffineTransform at = g2.getTransform();
		g2.translate(x, y);
		g2.setColor(colourWindow);
		g2.fill(new Rectangle2D.Double(-s/2, -s/2, s, s));
		g2.setTransform(at);
		
		drawFractal (g2, x, y, s );
	}
	
	//------------------------------------------------------
    // METHOD		:	drawFractal(Graphics2D g2, float x, float y, float s)
    //
    // PURPOSE		:	recursive drawing border of window
	//
    // PARAMETERS	:
    //     				Graphics2D g2
	//					float x - x position
	//					float y - y position
	//					float s - dimension of side
	//
	// RETURN VALUE	: 	
	//					no return value
    //
    //------------------------------------------------------
	private void drawFractal(Graphics2D g2, float x, float y, float s)
	{
		//if side less then 20 recursion stops as base case hit
		if (s  > 20) {
			AffineTransform at = g2.getTransform();
			g2.translate(x, y);
			g2.setColor(Color.BLACK);
			
			g2.draw(new Rectangle2D.Double(-s/2, -s/2, s/2, s/2)); 
			g2.draw(new Rectangle2D.Double(0, -s/2, s/2, s/2)); 
			g2.draw(new Rectangle2D.Double(-s/2, 0, s/2, s/2)); 
			g2.draw(new Rectangle2D.Double(0, 0, s/2, s/2)); 
			
			s *= 0.5; //updating side
			
			g2.setTransform(at);
			//four recursive calls for different sides of window
			drawFractal (g2, x, y, s );
			drawFractal (g2, x+s/2, y, s );
			drawFractal (g2, x-s/2, y, s );
			drawFractal (g2, x, y + s/2, s );
			drawFractal (g2, x, y - s/2, s );
		}
	}
}
