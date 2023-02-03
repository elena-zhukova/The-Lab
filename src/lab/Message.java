//-----------------------------------------
// 
// CLASS		: Message.java
//
// REMARKS		: This class is prints all the text messages in the application
//
//-----------------------------------------

package lab;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Message 
{

	private String welcome;
	private String subhead;
	private String instructions;
	
	private String text;
	
	private Font general;
	
	public Message() 
	{
		welcome = "THE LAB";
		subhead = "Simulation of testing for blood type";
		instructions = "You can drag the pipette to get blood from the sample and the drag again to pour it into the well plate. "
				+ "\nOnce the well plate is full, another pipette will appear and you can drag it to three reagents and then to the well plate to test the reaction for the A, B and Rh antigens. "
				+ "\nWhen reactions are finished, you can click the cheatsheet to enlarge it and and drag well plate to it to compare the results.";
		general = new Font("Arial", Font.ITALIC|Font.BOLD, 14);
	}
	
	public Message(String str) 
	{
		text = str;
		general = new Font("Arial", Font.ITALIC|Font.BOLD, 14);
	}
	
	public void updateText(String str)
	{
		text = str;
	}
	
	public void updateFont(int size)
	{
		general = general.deriveFont(Font.ITALIC|Font.BOLD, size);
	}
	public void draw(Graphics2D g2)
	{
	    g2.setFont(general);
	    g2.setColor(Color.LIGHT_GRAY);
	    g2.drawString(text, 50, 700);
	}
	
	public void drawMessage(Graphics2D g2)
	{
		Font f = new Font("Arial", Font.BOLD, 64);
	    g2.setFont(f);
	    g2.setColor(Color.WHITE);
	    g2.drawString(welcome, 30, 500);
	    
	    Font f2 = new Font("Arial", Font.ITALIC|Font.BOLD, 36);
	    g2.setFont(f2);
	    g2.setColor(Color.LIGHT_GRAY);
	    g2.drawString(subhead, 30, 550);
	    
	    Font f3 = new Font("Arial", Font.PLAIN, 12);
	    g2.setFont(f3);
	    int counter = 0;
	    /*
	     * Source for breaking the String by using split 
	     * StackOverflow: https://stackoverflow.com/a/4413153
	     */
	    for (String line : instructions.split("\n"))
	    {
	    	g2.drawString(line, 30, 580 + counter);
	    	counter += 24;
	    }
	}

}
