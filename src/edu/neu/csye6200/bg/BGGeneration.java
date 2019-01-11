package edu.neu.csye6200.bg;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author mitalisalvi
 * NUID: 001630137
 */
public class BGGeneration 
{
	//holds stems of the current generation
	private ArrayList<BGStem> stemsOfGeneration = new ArrayList<BGStem>(); 
	
	//logger for logging all logs
	public static final Logger log_bgGeneration = Logger.getLogger(BGGeneration.class.getName());

	/**
	 * Getter method which returns the stems of the current generation
	 * @return Stems of the current generation
	 */
	public ArrayList<BGStem> getStemsOfGeneration() {
		return stemsOfGeneration;
	}

	/**
	 * Setter method which sets the input arrayList as the class attribute - stemsOfGeneration
	 * @param
	 * @return Void
	 */
	public void setStemsOfGeneration(ArrayList<BGStem> stemsOfGeneration) {
		this.stemsOfGeneration = stemsOfGeneration;
	}

	public void addStemToGeneration (BGStem s)
	{
		//log_bgGeneration.info("Adding stem to the current stem of the generation");
		stemsOfGeneration.add(s);
	}
	
	/**
	 * Grow child stems using the current generation and the applied rule
	 * @param BGGeneration current
	 * @param BGRule ruleToBeApplied
	 * @return BGGeneration
	 */
	public BGGeneration growFromCurrentGeneration (BGRule appliedRule)
	{
		BGGeneration nextGen = new BGGeneration();
		log_bgGeneration.info("Create new generation of stems based on the current generation");
		for (BGStem stems : getStemsOfGeneration())
		{
			BGStem[] childOfCurr = stems.growChildStems(appliedRule);
			for (int i=0; i<childOfCurr.length; i++ )
			{
				nextGen.addStemToGeneration(childOfCurr[i]);
			}
		}
		return nextGen;
	}


}
