//-----------------------------------------
// NAME			: Elena Zhukova
//
// CLASS		: LabApp.java
//
// REMARKS		: This is the program that creates the simulation of the lab environment for testing for blood type.
// 					This class includes the main method for executing the program.
//
//-----------------------------------------


package main;

import javax.swing.JFrame;

//Source code: IAT 265 Lab 10
public class LabApp extends JFrame
{

	public LabApp(String title) 
	{
		super(title);
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setLocation(0, 0);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LabPanel bpnl = new LabPanel(this);
		this.add(bpnl); 
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}//LabApp
	
	public static void main (String[] args)
	{
		new LabApp("LabApp");
		
	}//main

}//LabApp
