import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

@SuppressWarnings("serial")
public class bSim_original extends GraphicsProgram implements ChangeListener, ItemListener {

	// parameters 
	private static final int WIDTH = 1200; // n.b. screen coordinates
	private static final int HEIGHT = 600;
	private static final int OFFSET = 100;
	private int NUMBALLS = 15; // # balls to simulate
	private double MINSIZE = 1; // Minimum ball size
	private double MAXSIZE = 25; // Maximum ball size
	private double XMIN = 1; // Min X starting location
	private double XMAX = 200; // Max X starting location
	private double YMIN = 1; // Min Y starting location
	private double YMAX = 200; // Max Y starting location
	private double EMIN = 0; // Minimum loss coefficient
	private double EMAX = 100; // Maximum loss coefficient
	private double VMIN = 0; // Minimum X velocity
	private double VMAX = 10; // Maximum Y velocity
	//^I chose these values because my values from assignment 3 don't seem to work, so my bad :)
	
	// objects
	private bTree myTree;
	private Thread RunningBalls; 
	// variables 
	int PS_numballs;
	int PS_minsize;
	int PS_maxsize;
	int PS_lossmin;
	int PS_lossmax;
	int PS_xmin;
	int PS_xmax;
	int PS_ymin;
	int PS_ymax;
	int PS_xvelmin;
	int PS_xvelmax; 

	//Creating variables for sliders
	sliderBox numballsSlider;
	sliderBox minsizeSlider;
	sliderBox maxsizeSlider;
	sliderBox lossminSlider;
	sliderBox lossmaxSlider;
	sliderBox xminSlider;
	sliderBox xmaxSlider; 
	sliderBox yminSlider;
	sliderBox ymaxSlider;
	sliderBox xvelminSlider;
	sliderBox xvelmaxSlider;

	// Drop-down menu 
	JComboBox<String> bSimC;

	//creating options for the chooser panel JComboBox
	void setChoosers() {
		bSimC = new JComboBox<String>();
		bSimC.addItem("bSim");
		bSimC.addItem("Run");
		bSimC.addItem("Stop");
		bSimC.addItem("Sort");
		bSimC.addItem("Clear");
		bSimC.addItem("Quit");
		add(bSimC,NORTH);
		addJComboListeners();

	}
	
	//Adding a listener
	void addJComboListeners() {
		bSimC.addItemListener((ItemListener)this);
	}

	//did this because deprecation warnings keep popping up, and i grew tired of them
	@SuppressWarnings("deprecation")
	public  void run() {

		//adding a new JPanel
		JPanel eastPannel = new JPanel();

		JLabel gen_sim = new JLabel("General Simulation Parameters");
		eastPannel.add(gen_sim);

		//setting slider values for every slider and setting them on the JPanel
		eastPannel.setLayout(new GridLayout(15,1));
		numballsSlider = new sliderBox("NUMBALLS", MINSIZE,15, MAXSIZE);
		eastPannel.add(numballsSlider.myPanel,"EAST");
		numballsSlider.mySlider.addChangeListener((ChangeListener) this);

		minsizeSlider = new sliderBox("MIN SIZE", MINSIZE, 1, MAXSIZE);
		eastPannel.add(minsizeSlider.myPanel,"EAST");
		minsizeSlider.mySlider.addChangeListener((ChangeListener) this);

		maxsizeSlider = new sliderBox ("MAX SIZE", MINSIZE, 8, MAXSIZE);
		eastPannel.add(maxsizeSlider.myPanel,"EAST");
		maxsizeSlider.mySlider.addChangeListener((ChangeListener)this);

		lossminSlider = new sliderBox("LOSS MIN", EMIN,40, EMAX);
		eastPannel.add(lossminSlider.myPanel,"EAST");
		lossminSlider.mySlider.addChangeListener((ChangeListener)this);

		lossmaxSlider = new sliderBox("LOSS MAX", EMIN,90,EMAX);
		eastPannel.add(lossmaxSlider.myPanel,"EAST");
		lossmaxSlider.mySlider.addChangeListener((ChangeListener)this);

		xminSlider = new sliderBox("XMIN",XMIN,10,XMAX );
		eastPannel.add(xminSlider.myPanel,"EAST");
		xminSlider.mySlider.addChangeListener((ChangeListener)this);

		xmaxSlider = new sliderBox("XMAX",XMIN,50,XMAX );
		eastPannel.add(xmaxSlider.myPanel,"EAST");
		xmaxSlider.mySlider.addChangeListener((ChangeListener)this);

		yminSlider = new sliderBox("YMIN",YMIN,50,YMAX );
		eastPannel.add(yminSlider.myPanel,"EAST");
		yminSlider.mySlider.addChangeListener((ChangeListener)this);

		ymaxSlider = new sliderBox("YMAX",YMIN,100,YMAX );
		eastPannel.add(ymaxSlider.myPanel,"EAST");
		ymaxSlider.mySlider.addChangeListener((ChangeListener)this);

		xvelminSlider = new sliderBox("X VEL MIN", VMIN, VMIN, VMAX);
		eastPannel.add(xvelminSlider.myPanel,"EAST");
		xvelminSlider.mySlider.addChangeListener((ChangeListener)this);

		xvelmaxSlider = new sliderBox("X VEL MIN", VMIN, 5, VMAX);
		eastPannel.add(xvelmaxSlider.myPanel,"EAST");
		xvelmaxSlider.mySlider.addChangeListener((ChangeListener)this);

		//adding a choice menu for BSim, clear, stop, etc.
		add(eastPannel);
		setChoosers();

		this.resize(WIDTH,HEIGHT+OFFSET); 
		myTree = new bTree();
		RunningBalls = new Thread(new RunBalls());
		if (bSimC.getSelectedIndex()==1) 
			RunningBalls.start();

	} // run ()

	public class RunBalls implements Runnable {
		public void run() {
			
			// Creating a ground for the balls
			GRect gPlane = new GRect(0,HEIGHT-OFFSET,WIDTH,3);
			gPlane.setColor(Color.BLACK);
			gPlane.setFilled(true);
			add(gPlane);
			
			// Set up random number generator & bTree
			RandomGenerator rg = RandomGenerator.getInstance();
			// Generate a series of random gBalls and let the simulation run till completion
			NUMBALLS = (int)numballsSlider.getISlider();
			MINSIZE = minsizeSlider.getISlider()*10; //making sliders bigger
			MAXSIZE = maxsizeSlider.getISlider()*10; //making slider bigger
			YMIN = yminSlider.getISlider();
			YMAX = ymaxSlider.getISlider();
			XMIN = xminSlider.getISlider();
			XMAX = xmaxSlider.getISlider();
			EMIN = lossminSlider.getISlider()/100;
			EMAX = lossmaxSlider.getISlider()/100;
			VMIN = xvelminSlider.getISlider();
			VMAX = xvelmaxSlider.getISlider();
			
			
			for (int i=0; i<NUMBALLS; i++) {
				//randomizing every value for xi, yi, iSize, iColor, iLoss, iVel, iBall and stuff
				double Xi = rg.nextDouble(XMIN,XMAX); 
				double Yi = rg.nextDouble(YMIN,YMAX); 
				double iSize = rg.nextDouble(MINSIZE,MAXSIZE); 
				Color iColor = rg.nextColor(); 
				double iLoss = rg.nextDouble(EMIN,EMAX); 
				double iVel = rg.nextDouble(VMIN,VMAX); 
				gBall iBall = new gBall(Xi,Yi,iSize,iColor,iLoss/10,iVel); 
				add(iBall.myBall); 
				myTree.addNode(iBall); // Saving this instance
				iBall.start(); // Starting this instance

			}
		}
	}

	//Setting up the item listener
	public void itemStateChanged(ItemEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<String> source = (JComboBox<String>)e.getSource();
		if (source==bSimC) {
			if (bSimC.getSelectedIndex()==1) {
				RunningBalls.stop();
				removeAll();
				myTree = new bTree();
				RunningBalls = new Thread(new RunBalls());
				RunningBalls.start();

			}
			else if (bSimC.getSelectedIndex()==4) {
				RunningBalls.stop();
				RunningBalls.interrupt();
				removeAll();
			}
			else if (bSimC.getSelectedIndex()== 5) {
			
				System.exit(0); 
			}
			else if (bSimC.getSelectedIndex()==2) {
				myTree.stopBalls(); 
				}
			else if (bSimC.getSelectedIndex()==3){
					myTree.stopBalls();
					myTree.moveSort();
				}
		
		}
	} 
	
	//setting up reactions to any particular sliders 
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (source==numballsSlider.mySlider) {
			PS_numballs=numballsSlider.mySlider.getValue();
			numballsSlider.setISlider(PS_numballs);
		}
		else if(source == minsizeSlider.mySlider)
		{
			PS_minsize = minsizeSlider.getISlider();
			minsizeSlider.setISlider(PS_minsize);
		}
		else if (source == maxsizeSlider.mySlider) {
			PS_maxsize = maxsizeSlider.getISlider();
			maxsizeSlider.setISlider(PS_maxsize);
		}
		else if (source == lossminSlider.mySlider) {
			PS_lossmin = lossminSlider.getISlider();
			lossminSlider.setISlider(PS_lossmin);
		}
		else if (source == lossmaxSlider.mySlider ) {
			PS_lossmax = lossmaxSlider.getISlider();
			lossmaxSlider.setISlider(PS_lossmax);
		}
		else if (source == xminSlider.mySlider) {
			PS_xmin = xminSlider.getISlider();
			xminSlider.setISlider(PS_xmin);
		}
		else if (source == xmaxSlider.mySlider) {
			PS_xmax = xmaxSlider.getISlider();
			xmaxSlider.setISlider(PS_xmax);
		}
		else if (source == yminSlider.mySlider) {
			PS_ymin = yminSlider.getISlider();
			yminSlider.setISlider(PS_ymin);
		}
		else if (source == ymaxSlider.mySlider ) {
			PS_ymax = ymaxSlider.getISlider();
			ymaxSlider.setISlider(PS_ymax);
		}
		else if (source == xvelminSlider.mySlider) {
			PS_xvelmin = xvelminSlider.getISlider();
			xvelminSlider.setISlider(PS_xvelmin);
		}
		else if (source == xvelmaxSlider.mySlider) {
			PS_xvelmax = xvelmaxSlider.getISlider();
			xvelmaxSlider.setISlider(PS_xvelmax);
		}
	} 
	
	
}


