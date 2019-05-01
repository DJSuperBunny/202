import acm.graphics.GOval;
import java.awt.Color;
public class gBall extends Thread {

	//defining ball characteristics
	public GOval myBall; 
	double Xi;
	double Yi;
	double bSize;
	Color bColor;
	double bLoss;
	double bVel;

	public gBall (double xi, double yi, double bSize, Color bColor, double bLoss, double bVel) {

		//parameters for each instance of a ball 
		Xi = xi;
		Yi = yi;
		this.bSize = bSize;
		this.bColor = bColor;
		this.bLoss = bLoss;
		this.bVel = bVel;

		myBall = new GOval (Xi,Yi,bSize,bSize); 
		myBall.setFilled(true);
		myBall.setFillColor(bColor);   
	}

	// constant parameters for the simulation 
	
	boolean check = true; 

	public void run() {

		// setting simulation parameters 
		double totalTime=0.0;
		double time =0; 
		double height = Yi;
		double initialDirectionUp =0; 
		double yVel = Math.sqrt(2*9.8*Yi);
		boolean DirectionDown = true;
		double eLoss = Math.sqrt(1-bLoss);
		
		double OFFSET = 100;
		
		// start of the simulation
		while (true) 
		{
			check = true; // sentinel variable 
			
			myBall.setLocation(Xi+totalTime*bVel, 600-10*height-bSize-OFFSET); 

			// when ball goes down
			if (DirectionDown) 
			{
				height = Yi - 0.5*9.8*Math.pow(time,2);

				if (height<=0)
				{
					//to prevent the ball to go below the ground:
					
					height =0; 
					Yi = height; 
					initialDirectionUp = height;
					DirectionDown = false;
					time = 0;
					yVel = yVel*eLoss; 

				}
			}

			else
				// ball goes back up
			{
				height = initialDirectionUp + yVel*time-0.5*9.8*Math.pow(time,2);

				if (height >Yi)
				{
					Yi = height;
				}
				else
					// ball goes back down	
				{
					DirectionDown =true;
					time = 0;

				}


			}
			if (yVel<1.0) //1.0 is the ball velocity for stopping 
				
				//checking if the balls are still running or not
			{ 
				check=false;
				break; 
			} 

		
			time+= 0.1; //time step set at 0.1
			totalTime+= 0.1; //time step at 0.1

			try {
				Thread.sleep(15); //program's "speed" set at 15
			}
			catch(InterruptedException e) {
				e.printStackTrace(); 
			}
		} 
	} 

	// check if the gBalls are still running
	public boolean is_Running() {
		return check; 
	}
	
	public void moveTo(double x,double y) {
		myBall.setLocation(x,y);
	}
} 
