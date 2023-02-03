//-----------------------------------------
// 
// CLASS		: Serum.java
//
// REMARKS		: Subclass of LiquidContainer that can interact with SerumPipette only
//
//-----------------------------------------
package equipment;

import java.awt.Graphics2D;

import decorator.SerumInt;

public class Serum extends LiquidContainer implements SerumInt
{
	
	public Serum(int x, int y, String img) 
	{
		super(x,y,img);
	}

	@Override
	public void showSerum(Graphics2D g2) 
	{
		super.draw(g2);
		
	}

	
}
