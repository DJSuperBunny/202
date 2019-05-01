
public class bTree {

	// setting up references in x and the number of running balls
	
	public double refX = 0;
	public int runningBalls = 0;

	bNode root = null;

	// gBall objects
	public void addNode(gBall aBall) {
		root = rNode (root,aBall);
	}

	// placing gBall in the bTree 
	private bNode rNode(bNode root, gBall aBall) {
		
		//if there is no root:
		if (root==null) {
			bNode node = new bNode();
			node.aBall = aBall;
			node.left = null;
			node.right = null;
			root = node;
			return root;
		}
		//placing smaller balls to the left
		else if (aBall.bSize<root.aBall.bSize) {
			root.left = rNode(root.left,aBall);
		}
		//placing big balls to the right
		else {
			root.right = rNode(root.right,aBall);
		}
		return root;

	} 

	// check if all gBalls stopped running 
	public boolean isRunning() {
		boolean sentinel = true;
		runningBalls = 0;
		traversal_inorder_runningcheck(root);
		if (runningBalls!=0) {
			return sentinel;
		}
		else {
			sentinel = false;
			return sentinel;
		}
	} 

	
	private void traversal_inorder_runningcheck(bNode root) {
		if (root.left!= null) 
			traversal_inorder_runningcheck(root.left);
		if (root.aBall.is_Running()==true)
			runningBalls+=1; 
		if (root.right!=null)
			traversal_inorder_runningcheck(root.right);
	} 
	
	//moving and sorting the balls by size
	public void moveSort() {
		refX = 0;
		moveSort(root);
	} 
	
	// scanning the bTree to sort all gBalls in their correct position/order
	private void moveSort (bNode root) {

		if (root.left!= null) 
			moveSort(root.left);
		root.aBall.moveTo(refX, 800-200-root.aBall.bSize); //height being 800, and offset being 200
		refX= root.aBall.bSize + refX;


		if (root.right!= null)
			moveSort(root.right);

	} 
	
	public void clearBalls() { 
		clearAll(root);  
		}    
	
	private void clearAll( bNode root) { 
	   if (root.left!= null) 
		   clearAll(root.left);
		//  remove(root.aBall.myBall);
		  root.aBall.interrupt();
		   
		
		 if (root.right!= null) 
		    clearAll(root.right);
		 
		 
	}
	
	public void stopBalls() {
		stopBalls(root);
	}
	
	private void stopBalls(bNode root) {
		if (root.left!= null) 
			   stopBalls(root.left);
			   root.aBall.stop();
			 
	     if (root.right!= null) 
			    stopBalls(root.right);
			 
	}
	
} 

