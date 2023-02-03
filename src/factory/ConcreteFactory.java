//-----------------------------------------
// 
// CLASS		: ConcreteFactory.java
//
// REMARKS		: This is subclass of AbstFactory for creating concrete shapes
//
//-----------------------------------------
package factory;

import equipment.WellPlate;
import lab.CheatSheet;
import lab.Restart;
import main.LabPanel;

public class ConcreteFactory extends AbstFactory 
{

	@Override
	public Shape createShapes(String type) 
	{
		Shape shape = null;
	
		if (type == "wellplate")
			shape = new WellPlate();
		else if (type == "cheatsheet")
			shape = new CheatSheet();
		else if (type == "start")
			shape = new Restart(LabPanel.WIDTH/2, 200, "assets/Start.png");
		else if (type == "restart")
			shape = new Restart(LabPanel.WIDTH/2, 200, "assets/Restart.png");
		
		return shape;
	}

}
