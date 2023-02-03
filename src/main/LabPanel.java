//-----------------------------------------
// NAME				: Elena Zhukova
//
// CLASS			: LabPanel.java
//
// REMARKS			: This is the program that creates the simulation of the lab environment for testing for blood type.
// 						This class is created to execute the main flow of the program
//
// ECO POINTS		: 
//					- All images are created by myself
//					- Time of day shifting depends on state of well plate (the experiment is taking entire day)
//					- Four finite state machine implemented (General, CheatSheet, Pipette and WellPlate), one of them (WellPlate)
//						switches between seven states.Three of fsm are implemented with use of ENUM
//					- most of control is done via mouse dragging/clicking
//					- creating Serum objects happens via JButtons
//					- recursive call to draw the window handles multiple recursive calls inside it
//					- Perlin noise is implemented as a response to chemical reaction in all three wells of well plate
//
//
//-----------------------------------------

package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import decorator.SerumInt;
import equipment.LiquidContainer;
import equipment.Pipette;
import equipment.Sample;
import equipment.SamplePipette;
import equipment.Serum;
import equipment.SerumPipette;
import equipment.WellPlate;
import lab.CheatSheet;
import lab.Lab;
import lab.Restart;
import lab.Message;
import factory.AbstFactory;
import factory.ConcreteFactory;
import factory.Shape;
import util.GeneralState;
import util.MinimHelper;
import util.PipetteState;
import util.WellPlateState;


public class LabPanel extends JPanel implements ActionListener 
{

	private JFrame jframe;
	
	public static int WIDTH = 1100; 					// width of the panel
	public static int HEIGHT = 800; 					// height of the panel
	
	SerumInt serum;
	
	//factory
	private ArrayList<Shape> shapes;					// ArrayList of objects create via factory
	private AbstFactory shapeMaker;						// factory class needed to create objects
	
	//environment and messages
	private Lab lab; 									// variable for general environment
	private Message welc;								// welcome message
	private Message instr;								// object to print instructions during the major part of the program
	private Message result;								// final message
	
	private LiquidContainer  antiRh, antiA, antiB;		// reagents for determining blood type (stored in super class)
	private LiquidContainer blood; 						// blood sample (stored in super class)

	private ArrayList<Pipette> sList;					// ArrayList of Pipettes storing objects of subclasses
	
	private Timer timer;								// Timer
	
	private double mouseX;								// horizontal position of the mouse
	private double mouseY;								// vertical position of the mouse
	
	private Minim minim;								// object for loading sounds
	private AudioPlayer background, drip, drawIn, paper;// sounds
	
	private GeneralState state;							// current state which identifies which of the screens to show
	
	
    //------------------------------------------------------
    // CONSTRUCTOR	:	LabPanel(JFrame frame)
    //
    // PURPOSE		:	Constructor of LabPanel which instantiates most of the objects needed for execution of the program
	//
    // PARAMETERS	:
    //     				@JFrame frame - JFrame where program will be shown
    //
    //------------------------------------------------------
	public LabPanel(JFrame frame) 
	{
		jframe = frame;
		
		this.setBackground(Color.white);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		setLayout(new FlowLayout());
		JButton serumA = new JButton("Serum AntiA");
		serumA.setBackground(Color.LIGHT_GRAY);
		add(serumA);
		serumA.addActionListener(this);

		JButton serumB = new JButton("Serum AntiB");
		serumB.setBackground(Color.LIGHT_GRAY);
		add(serumB);
		serumB.addActionListener(this);
		
		JButton serumRh = new JButton("Serum AntiRh");
		serumRh.setBackground(Color.LIGHT_GRAY);
		add(serumRh);
		serumRh.addActionListener(this); 
		
		//factory
		shapeMaker = new ConcreteFactory();
		shapes  = new ArrayList<Shape>();
		shapes.add(shapeMaker.createShapes("wellplate"));
		shapes.add(shapeMaker.createShapes("cheatsheet"));
		shapes.add(shapeMaker.createShapes("start"));
		shapes.add(shapeMaker.createShapes("restart"));
		
		//sound
		minim = new Minim(new MinimHelper());
		background = minim.loadFile("labmachine.wav");
		drip = minim.loadFile("drip1.wav");
		drawIn = minim.loadFile("slurp.mp3");
		paper = minim.loadFile("tearingpaper08.wav");
		
		lab = new Lab();
		welc = new Message();
		instr = new Message("Move red pipette towards blood sample");
		result = new Message("Congrarulations! You identified B+ blood type!");
		
		blood = new Sample();
		
		
		sList = new ArrayList<Pipette>();
		sList.add(new SamplePipette());
		sList.add(new SerumPipette());
			
		
		MyMouseListener ml = new MyMouseListener();
		addMouseListener(ml);
		
		MyMotionListener m2 = new MyMotionListener();
		addMouseMotionListener(m2);

		timer = new Timer(30, this);
		timer.start();
		
		state = GeneralState.WELCOME;
	}//LabPanel
	
	//------------------------------------------------------
    // METHOD		:	paintComponent(Graphics g)
    //
    // PURPOSE		:	painting objects in the panel depending on the current state of the program
	//
    // PARAMETERS	:
    //     				@Graphics g
	//
	// RETURN VALUE	: 	
	//					no return value
    //
    //------------------------------------------------------
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//drawing welcome screen
		if (state == GeneralState.WELCOME)
		{
			lab.drawLab(g2); 	//draw environment
			((Restart)shapes.get(2)).drawButton(g2); 	//draw start button
			welc.drawMessage(g2); 	//draw welcome message
		}//if
		//drawing main part of application
		else if (state == GeneralState.LAB)
		{
			lab.drawLab(g2); 	//draw environment
			instr.draw(g2);		//draw instructions
			
			//draw serums if objects are created via buttons
			if(antiA != null)
				antiA.draw(g2);
			if(antiB != null)
				antiB.draw(g2);
			if(antiRh != null)
				antiRh.draw(g2);
			
			blood.draw(g2);		//draw blood sample
			((WellPlate)shapes.get(0)).drawPlate(g2);		//draw well plate
			//draw ArrayList of pipettes
			for (Pipette s: sList)
				s.drawPipette(g2);
			
			((CheatSheet)shapes.get(1)).drawCheatSheet(g2);	
		}//else if
		//drawing finishing screen with restart button
		else if (state == GeneralState.RESTART)
		{
			background.pause(); //pause background music
			lab.drawLab(g2); //draw environment
			((Restart)shapes.get(3)).drawButton(g2);	//draw restart button
			result.updateFont(45); //update font for final message
			result.draw(g2);	//draw final message
		}//else if
	}//paintComponent
	
	
	//------------------------------------------------------
    // METHOD		:	actionPerformed(ActionEvent e) 
    //
    // PURPOSE		:	checking the button state depending on which serums will be drawn and repainting
	//
    // PARAMETERS	:
    //     				@ActionEvent e
	//
	// RETURN VALUE	: 	
	//					no return value
    //
    //------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// if button "Serum AntiA" is pressed create serum AntiA, play sound effect and update instructions
		if (e.getActionCommand() == "Serum AntiA") 
		{
			antiA = new Serum(100, 250, "assets/antiA.png"); //creating serum if button is pressed
			instr.updateText("Drag yellow pipette towards AntiA serum");//update instructions
			paper.play(0);  //play sound effect
		}//if
		// if button "Serum AntiB" is pressed create serum AntiB, play sound effect and update instructions
		if (e.getActionCommand() == "Serum AntiB") 
		{
			antiB = new Serum(275, 250, "assets/antiB.png"); //creating serum if button is pressed
			instr.updateText("Drag yellow pipette towards AntiB serum"); //update instructions
			paper.play(0); //play sound effect
		}//if
		// if button "Serum AntiRh" is pressed create serum AntiRh, play sound effect and update instructions
		if (e.getActionCommand() == "Serum AntiRh") 
		{
			antiRh = new Serum(450, 250, "assets/antiRh.png"); //creating serum if button is pressed
			instr.updateText("Drag yellow pipette towards AntiRh serum"); //update instructions
			paper.play(0); //play sound effect
		}//if
		
		repaint();
	}//actionPerformed

	//-----------------------------------------
	//
	// CLASS		: MyMouseListener
	//
	// REMARKS		: This is the class which allows handling mouse clicking events for cheat sheet and buttons
	//
	//-----------------------------------------
	public class MyMouseListener extends MouseAdapter 
	{

		//------------------------------------------------------
	    // METHOD		:	mouseClicked(MouseEvent e)
	    //
	    // PURPOSE		:	checking if mouse is clicked and performing instructions if mouse is clicked in the area of specific objects
		//
	    // PARAMETERS	:
	    //     				@MouseEvent e
		//
		// RETURN VALUE	: 	
		//					no return value
	    //
	    //------------------------------------------------------
		public void mouseClicked(MouseEvent e) 
		{
			//get position of the mouse
			mouseX = e.getX();
			mouseY = e.getY();
			//checking if start button is pressed,switch to the execution of the main program
			if (((Restart)shapes.get(2)).clicked(mouseX, mouseY) && state == GeneralState.WELCOME) 
			{
				paper.play(0); //play sound effect
				state = GeneralState.LAB; //move to the main screen of application
				background.loop(); //play background music
				instr.updateText("Drag red pipette towards blood sample"); //update instructions
			}//if
			//if restart button is pressed dispose frame and create new one to restart
			if (((Restart)shapes.get(3)).clicked(mouseX, mouseY) && state == GeneralState.RESTART) 
			{
				paper.play(0); //play sound effect
				jframe.dispose();
				jframe = new LabApp("LabApp");
			}//if
			//if cheat sheet is clicked enlarge./shrink it depending on current state of cheat sheet
			if (((CheatSheet)shapes.get(1)).clicked(mouseX, mouseY)) 
			{
				//if cheat sheet is small, enlarge it
				if (((CheatSheet)shapes.get(1)).getState() == false)
				{
					((CheatSheet)shapes.get(1)).setState(true); //enlarging cheat sheet by updating state
					paper.play(0); //play sound effect
					instr.updateText("Drag well plate to compare results"); //update instructions
				}//if
				//otherwise shrink cheat sheet
				else
				{
					paper.play(0); //play sound effect
					((CheatSheet)shapes.get(1)).setState(false); //shrinking cheat sheet by updating state
				}//else
			}//if
		}//mouseClicked
	}//MyMouseListener
	
	//-----------------------------------------
	//
	// CLASS		: MyMotionListener
	//
	// REMARKS		: This is the class which allows handling mouse dragging events for pipettes and well plate
	//
	//-----------------------------------------
	public class MyMotionListener extends MouseMotionAdapter
	{
		//------------------------------------------------------
	    // METHOD		:	mouseDragged(MouseEvent e)
	    //
	    // PURPOSE		:	checking if mouse is dragged and performing instructions if mouse is dragging in the area of specific objects
		//
	    // PARAMETERS	:
	    //     				@MouseEvent e
		//
		// RETURN VALUE	: 	
		//					no return value
	    //
	    //------------------------------------------------------
		public void mouseDragged(MouseEvent e)
		{
			//get mouse location
			mouseX = e.getX();
			mouseY = e.getY();
			
			//dragging sample (red) pipette if well plate is needed to be filled with blood sample
			if (sList.get(0).clicked(mouseX, mouseY) &&  (((WellPlate)shapes.get(0)).getState() == WellPlateState.EMPTY 
					|| ((WellPlate)shapes.get(0)).getState() == WellPlateState.AFILLED 
					|| ((WellPlate)shapes.get(0)).getState() == WellPlateState.BFILLED))
			{
				sList.get(0).setPos(mouseX, mouseY); //drag pipette
				//if pipette hit well plate, fill the well plate and move pipette to default state
				if (((SamplePipette)sList.get(0)).hit(blood))
				{
					drawIn.play(0); //play sound effect
					sList.get(0).revertDefaultPos(); //moving pipette to default state
					sList.get(0).setState(PipetteState.FULL); //filling the pipette by changing it state
					sList.get(0).setImage(); //setting image of pipette depending on new state
					instr.updateText("Drag red pipette towards well plate"); //update instructions
				}//if
			}//if
			
			//dragging serum (yellow) pipette if well plate is needed to be filled with antiA  serum
			if (sList.get(1).clicked(mouseX, mouseY) && ((WellPlate)shapes.get(0)).getState() == WellPlateState.FULL)
			{
				sList.get(1).setPos(mouseX, mouseY); //drag pipette
				//if pipette hit well plate, fill the well plate and move pipette to default state
				if (((SerumPipette)sList.get(1)).hit(antiA))
				{
					drawIn.play(0); //play sound effect
					sList.get(1).revertDefaultPos(); //moving pipette to default state
					sList.get(1).setState(PipetteState.FULL); //filling the pipette by changing it state
					sList.get(1).setImage(); //setting image of pipette depending on new state
					instr.updateText("Drag yellow pipette towards well plate"); //update instructions
				}//if
			}//if
			//dragging serum (yellow) pipette if well plate is needed to be filled with antiB  serum
			else if (sList.get(1).clicked(mouseX, mouseY) && ((WellPlate)shapes.get(0)).getState() == WellPlateState.ACHECKED)
			{
				sList.get(1).setPos(mouseX, mouseY); //drag pipette
				if (((SerumPipette)sList.get(1)).hit(antiB))
				{
					drawIn.play(0); //play sound effect
					sList.get(1).revertDefaultPos(); //moving pipette to default state
					sList.get(1).setState(PipetteState.FULL); //filling the pipette by changing it state
					sList.get(1).setImage(); //setting image of pipette depending on new state
					instr.updateText("Drag yellow pipette towards well plate"); //update instructions
				}//if
			}//else if
			//dragging serum (yellow) pipette if well plate is needed to be filled with antiRh  serum
			else if (sList.get(1).clicked(mouseX, mouseY) && ((WellPlate)shapes.get(0)).getState() == WellPlateState.BCHECKED)
			{ 
				sList.get(1).setPos(mouseX, mouseY); //drag pipette
				if (((SerumPipette)sList.get(1)).hit(antiRh))
				{
					drawIn.play(0); //play sound effect
					sList.get(1).revertDefaultPos(); //moving pipette to default state 
					sList.get(1).setState(PipetteState.FULL); //filling the pipette by changing it state
					sList.get(1).setImage(); //setting image of pipette depending on new state
					instr.updateText("Drag yellow pipette towards well plate"); //update instructions
				}//if
			}//else if
			
			//dragging pipette if first well of well plate is need to be filled with blood
			if (((WellPlate)shapes.get(0)).getState() == WellPlateState.EMPTY && sList.get(0).clicked(mouseX, mouseY))
			{
				sList.get(0).setPos(mouseX, mouseY); //drag pipette
				//if pipette is full and well plate need to be filled
				if (sList.get(0).hit(((WellPlate)shapes.get(0))) && sList.get(0).getState() == PipetteState.FULL)
				{
					drip.play(0); //play sound effect
					((WellPlate)shapes.get(0)).setState(WellPlateState.AFILLED); //update well plate state to fill first well
					((WellPlate)shapes.get(0)).setImage(); //setting image of well plate depending on new state
					lab.setWindowColor(((WellPlate)shapes.get(0)).getState()); //updating time of day by changing window colour
					sList.get(0).revertDefaultPos(); //moving pipette to default state
					sList.get(0).setState(PipetteState.EMPTY); //dropping substance from pipette into well plate by changing state
					sList.get(0).setImage(); //setting image of pipette depending on new state
					instr.updateText("Drag red pipette towards blood sample"); //update instructions
				}//if
			}//if
			//dragging pipette if second well of well plate is need to be filled with blood
			else if (((WellPlate)shapes.get(0)).getState() == WellPlateState.AFILLED && sList.get(0).clicked(mouseX, mouseY))
			{
				sList.get(0).setPos(mouseX, mouseY); //drag pipette
				if (sList.get(0).hit(((WellPlate)shapes.get(0))) && sList.get(0).getState() == PipetteState.FULL)
				{
					drip.play(0); //play sound effect
					((WellPlate)shapes.get(0)).setState(WellPlateState.BFILLED); //update well plate state to fill second well
					((WellPlate)shapes.get(0)).setImage(); //setting image of well plate depending on new state
					lab.setWindowColor(((WellPlate)shapes.get(0)).getState()); //updating time of day by changing window colour
					sList.get(0).revertDefaultPos(); //moving pipette to default state
					sList.get(0).setState(PipetteState.EMPTY); //dropping substance from pipette into well plate by changing state
					sList.get(0).setImage(); //setting image of pipette depending on new state
					instr.updateText("Drag red pipette towards blood sample"); //update instructions
				}//if
			}//else if
			//dragging pipette if third well of well plate is need to be filled with blood
			else if (((WellPlate)shapes.get(0)).getState() == WellPlateState.BFILLED && sList.get(0).clicked(mouseX, mouseY))
			{
				sList.get(0).setPos(mouseX, mouseY); //drag pipette
				if (sList.get(0).hit(((WellPlate)shapes.get(0))) && sList.get(0).getState() == PipetteState.FULL)
				{
					drip.play(0); //play sound effect
					((WellPlate)shapes.get(0)).setState(WellPlateState.FULL); //update well plate state to fill third well
					((WellPlate)shapes.get(0)).setImage(); //setting image of well plate depending on new state
					lab.setWindowColor(((WellPlate)shapes.get(0)).getState()); //updating time of day by changing window colour
					sList.get(0).revertDefaultPos(); //moving pipette to default state
					sList.get(0).setState(PipetteState.EMPTY); //dropping substance from pipette into well plate by changing state
					sList.get(0).setImage(); //setting image of pipette depending on new state
					instr.updateText("Press \"Serum AntiA\" button on the top of screen"); //update instructions
				}//if
			}//else if
			//dragging pipette if first well of well plate is need to be filled with serum
			else if (((WellPlate)shapes.get(0)).getState() == WellPlateState.FULL && sList.get(1).clicked(mouseX, mouseY))
			{
				sList.get(1).setPos(mouseX, mouseY); //drag pipette
				if (sList.get(1).hit(((WellPlate)shapes.get(0))) && sList.get(1).getState() == PipetteState.FULL)
				{
					drip.play(0); //play sound effect
					((WellPlate)shapes.get(0)).setState(WellPlateState.ACHECKED); //update well plate state to fill first well with serum
					((WellPlate)shapes.get(0)).setImage(); //setting image of well plate depending on new state
					lab.setWindowColor(((WellPlate)shapes.get(0)).getState()); //updating time of day by changing window colour 
					sList.get(1).revertDefaultPos(); //moving pipette to default state
					sList.get(1).setState(PipetteState.EMPTY); //dropping substance from pipette into well plate by changing state
					sList.get(1).setImage(); //setting image of pipette depending on new state
					instr.updateText("Press \"Serum AntiB\" button on the top of screen"); //update instructions
				}//if
			}//else if
			//dragging pipette if second well of well plate is need to be filled with serum
			else if (((WellPlate)shapes.get(0)).getState() == WellPlateState.ACHECKED && sList.get(1).clicked(mouseX, mouseY))
			{
				sList.get(1).setPos(mouseX, mouseY); //drag pipette
				if (sList.get(1).hit(((WellPlate)shapes.get(0))) && sList.get(1).getState() == PipetteState.FULL)
				{
					drip.play(0); //play sound effect
					((WellPlate)shapes.get(0)).setState(WellPlateState.BCHECKED); //update well plate state to fill second well with serum
					((WellPlate)shapes.get(0)).setImage(); //setting image of well plate depending on new state
					lab.setWindowColor(((WellPlate)shapes.get(0)).getState()); //updating time of day by changing window colour
					sList.get(1).revertDefaultPos(); //moving pipette to default state
					sList.get(1).setState(PipetteState.EMPTY); //dropping substance from pipette into well plate by changing state
					sList.get(1).setImage(); //setting image of pipette depending on new state
					instr.updateText("Press \"Serum AntiRh\" button on the top of screen"); //update instructions
				}//if
			}//else if
			//dragging pipette if third well of well plate is need to be filled with serum
			else if (((WellPlate)shapes.get(0)).getState() == WellPlateState.BCHECKED && sList.get(1).clicked(mouseX, mouseY))
			{
				sList.get(1).setPos(mouseX, mouseY); //drag pipette
				if (sList.get(1).hit(((WellPlate)shapes.get(0))) && sList.get(1).getState() == PipetteState.FULL)
				{
					drip.play(0); //play sound effect
					((WellPlate)shapes.get(0)).setState(WellPlateState.ALLCHECKED); //update well plate state to fill third well with serum
					((WellPlate)shapes.get(0)).setImage(); //setting image of well plate depending on new state
					lab.setWindowColor(((WellPlate)shapes.get(0)).getState()); //updating time of day by changing window colour
					sList.get(1).revertDefaultPos(); //moving pipette to default state
					sList.get(1).setState(PipetteState.EMPTY); //dropping substance from pipette into well plate by changing state 
					sList.get(1).setImage(); //setting image of pipette depending on new state
					instr.updateText("Click on cheat sheet on the right side to enlarge it"); //update instructions
				}//if
			}//else if
			//dragging well plate to the cheat sheet
			else if (((WellPlate)shapes.get(0)).getState() == WellPlateState.ALLCHECKED && ((WellPlate)shapes.get(0)).clicked(mouseX, mouseY))
			{
				//checking if cheat sheet is enlarged
				if (((CheatSheet)shapes.get(1)).getState() == true)
				{
					((WellPlate)shapes.get(0)).setPos(mouseX, mouseY); //drag well plate
					//checking for collision between well plate and cheat sheet
					if (((WellPlate)shapes.get(0)).hit(((CheatSheet)shapes.get(1))) && ((CheatSheet)shapes.get(1)).getState() == true)
					{
						state = GeneralState.RESTART; //moving to restart screen
					}//if
				}//if
			}//else if

		}//mouseDragged
	}//MyMotionListener
}//LabPanel
