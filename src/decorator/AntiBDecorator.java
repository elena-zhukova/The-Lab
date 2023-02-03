//-----------------------------------------
// 
// CLASS		: AntiBDecorator.java
//
// REMARKS		: Decorator for second (additional) serum
//
//-----------------------------------------
package decorator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import equipment.LiquidContainer;
import equipment.Serum;

public class AntiBDecorator extends AntiADecorator 
{

	LiquidContainer antiB;
	
	public AntiBDecorator(SerumInt baseSerum) 
	{
		super(baseSerum);
		antiB = new Serum(275, 250, "assets/antiB.png");
	}
	
	public void showSerum(Graphics2D g2) 
	{
		super.showSerum(g2);
		antiB.draw(g2);
	}
}
