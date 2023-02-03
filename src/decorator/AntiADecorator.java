//-----------------------------------------
// 
// CLASS		: AntiADecorator.java
//
// REMARKS		: Decorator for first (base) serum
//
//-----------------------------------------
package decorator;

import java.awt.Graphics2D;

import equipment.LiquidContainer;
import equipment.Serum;

public class AntiADecorator implements SerumInt  
{

	protected SerumInt baseSerum;
	
	LiquidContainer antiA;
	
	public AntiADecorator(SerumInt baseSerum) 
	{
		this.baseSerum = baseSerum;
		antiA = new Serum(100, 250, "assets/antiA.png");
	}

	@Override
	public void showSerum(Graphics2D g2) 
	{
		baseSerum.showSerum(g2);
		antiA.draw(g2);
	}

}
