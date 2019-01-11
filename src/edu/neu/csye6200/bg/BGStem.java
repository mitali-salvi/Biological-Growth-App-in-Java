package edu.neu.csye6200.bg;

import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author mitalisalvi
 * NUID: 001630137
 *
 */
public class BGStem 
{
	private Point start; //start point of the stem
	private Point end; //end point of the stem
	private double dx ; //displacement in x direction
	private double dy; //displacement in y direction
	private ArrayList<BGStem> childStems = new ArrayList<BGStem>(); //child stems of the current stem
	
	//logger for logging all logs
	public static final Logger log_bgStem = Logger.getLogger(BGStem.class.getName());
	
	//static counter for generating ID of each stem
	private static int idCounter = 0;
	//id of the stem
	private int id = 0;
	
	//Constructor for initializing counter to keep track of BGStem ID
	public BGStem()
	{
		idCounter++; //static counter for keeping record of ID
		setId(idCounter);
	}	
	
	/**
	 * Getter method for start point
	 * @return Start Point co-ordinates
	 */
	public Point getStart() {
		return start;
	}
	
	/**
	 * Setter method for start point
	 */
	public void setStart(Point start) {
		this.start = start;
	}	
	
	/**
	 * Grow child stems of the current BGStem according to the applied rule and return the child stems
	 * @return ChildStems Array
	 */
	public BGStem[] growChildStems ( BGRule appliedRule)
	{
		//log_bgStem.info("Growing child stems of stem::"+this.getId());
		BGStem[] branchStems = new BGStem[appliedRule.getChildStems()];
		double[] childAngles = BGRule.getChildAngles(appliedRule.getCompleteAngle(), appliedRule.getChildStems());
		 
	    for (int i = 0; i < appliedRule.getChildStems(); i++) 
	    {
	    	//getting new length and displacement using rotation matrix concept. 
	    	double[] displacements = this.getNewLengthFromParentStem(childAngles[i], appliedRule.getGrowFactor());
	    	//creating new BG stem
	        branchStems[i] = new BGStem(); 
	        branchStems[i].setStart(this.end);
	        branchStems[i].dx = displacements[0];
	        branchStems[i].dy = displacements[1];
	        branchStems[i].setEnd();
	        this.addChildStem(branchStems[i]);
	    }
	    return branchStems;
	}
	
	@Override
	public String toString() 
	{
		String s = "Stem: "+id+" start=[" + start.x + "," + start.y + "]  end=[" + end.x +  "," + end.y 
				   + "]   length="  + calculateLength() 
					+ "   childStems::"+childStems.size() +"  Child IDs:="+ getChildStemsIDs();
		return s;
	}
	
	/**
	 * Getter for end point of BGStem
	 * @return End Point
	 */
	public Point getEnd() {
		return end;
	}
	
	/**
	 * Getter for displacement in the X direction
	 * @return Displacement - X
	 */
	public double getDx() {
		return dx;
	}
	
	/**
	 * Getter for displacement in the Y direction
	 * @return Displacement - Y
	 */
	public double getDy() {
		return dy;
	}
	
	/**
	 * Getter for child stems of the current BGStem
	 * @return ArrayList of type BGStem
	 */
	public ArrayList<BGStem> getChildStems() {
		return childStems;
	}
	
	/**
	 * Get new length of child stem based on the parent stem
	 * 	https://en.wikipedia.org/wiki/Rotation_matrix
	 *	X' = X*cos(theta) - Y*sin(theta)
	 *	Y' = X*sin(theta) + Y*cos(theta)
	 * @return displacement in the X and Y direction
	 */
	public double[] getNewLengthFromParentStem (double childAngle, double growthRatio)
	{
        double dispX = Math.round((growthRatio * (Math.cos(Math.toRadians(childAngle)) * this.dx - Math.sin(Math.toRadians(childAngle)) * this.dy)));
        double dispY =  Math.round((growthRatio * (Math.sin(Math.toRadians(childAngle)) * this.dx + Math.cos(Math.toRadians(childAngle)) * this.dy)));
        double[] result = {dispX, dispY};
        return result;
	}
	
	/**
	 * Set the end point of BGStem considering the displacements in the x and y direction
	 */
	public void setEnd ()
	{
		int x = (int) Math.round((this.start.x + this.dx));
	    int y = (int) Math.round(this.start.y + this.dy);
	    end = new Point(x, y);
	}
	
	/**
	 * Set the displacements
	 * @param displacement in x and y direction
	 */
	public void setDisplacement (int x, int y)
	{
		this.dx= x;
		this.dy =y;
	}
	
	/**
	 * Add child stems to the current BGStems
	 * @param BGStem child to be added
	 */
	public void addChildStem(BGStem s)
	{
		childStems.add(s);
	}
	
	/**
	 * Apply hypotenuse rule to get length of the stem from the start and end points
	 */
	private String calculateLength ()
	{
		double distance = Math.hypot(start.x - end.x, start.y - end.y);
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(distance);
	}
	
	/**
	 * Return the ID of the current BGStem
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set the ID of the current BGStem
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Return the child stem IDs of the current BGStem
	 */
	private String getChildStemsIDs ()
	{
		String result ="";
		for (BGStem child: childStems)
		{
			result += child.getId()+ "	";
		}
		return result;
		
	}


}
