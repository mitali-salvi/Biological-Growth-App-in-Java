package edu.neu.csye6200.bg;

import java.util.logging.Logger;

/**
 * @author mitalisalvi 
 * NUID: 001630137
 */
public class BGRule 
{
	private int childStems = 0; //number of child stems of the stem
	private double completeAngle = 0; //the parent angle of the stem which will divide into child angles
	private double growFactor = 0; //growth factor of the plant (value between 0 and 1)
	
	//logger for logging all logs
	public static final Logger log_bgRule = Logger.getLogger(BGRule.class.getName());
	
	//constructor which set the parameters of the rule
	public BGRule( int childStems, double completeAngle, double growFactor) 
	{
		super();
		//log_bgRule.info("Creating a new rule for growing the plant");
		this.childStems = childStems;
		this.completeAngle = completeAngle;
		this.growFactor = growFactor;
	}

	/**
	 * Getter for child stems
	 */
	public int getChildStems() {
		return childStems;
	}

	/**
	 * Getter for inter-angle of the stem
	 */
	public double getCompleteAngle() {
		return completeAngle;
	}

	/**
	 * Getter for growth factor
	 */
	public double getGrowFactor() {
		return growFactor;
	}

	/**
	 * Get child angles of the stem based on the parent angle and number of child stems
	 */
	public static double[] getChildAngles (double angle, int childStems)
	{
		double[] childAngles = new double[childStems];
        double angleFactor = (childStems - 1) * angle / 2;
        for (int i = 0; i < childStems; i++) 
        {
        	childAngles[i] = i * angle - angleFactor;
        }
        return childAngles;
	}


}
