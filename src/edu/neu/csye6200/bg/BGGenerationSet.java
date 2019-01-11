package edu.neu.csye6200.bg;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

/**
 * @author mitalisalvi 
 * NUID: 001630137
 */
public class BGGenerationSet extends Observable implements Runnable
{
	//Rule set from which rule will be applied
	private static final BGRule[] bgRuleSet = new BGRule[]{new BGRule( 3, 120, 0.5), new BGRule( 4, 30, 0.75),	
							new BGRule( 3, 45, 0.68), new BGRule(3, 60, 0.69), new BGRule(3, 15, 0.6)};
	
	//Stores the current generation stems of the plant
	private ArrayList<BGGeneration> generationsOfStem = new ArrayList<BGGeneration>();
	
	//Stores the current generation of the plant
	private BGGeneration currentGeneration ;
	
	//logger for logging all logs
	public static final Logger log_bgGenerationSet = Logger.getLogger(BGGenerationSet.class.getName());
	
	private BGRule appliedRule = null; //rule applied to get the generation
	private BGScene scene = null; //reference to JPanel for drawing
	private int countOfGeneration = 0;
	private Object monitor; //monitor for keeping thread synchronised
	private boolean flagForPauseResume = false;

	//constructor called for initializing object with ruleNo and maximum number of generations
	public BGGenerationSet(int ruleNo , int maxNoOfGenerations , Object o) 
	{
		super();
		if (ruleNo==0)
		{
			appliedRule = bgRuleSet[0];
		}
		else if (ruleNo ==1)
		{
			appliedRule = bgRuleSet[1];
		}
		else if (ruleNo ==2)
		{
			appliedRule = bgRuleSet[2];
		}
		else if (ruleNo ==3)
		{
			appliedRule = bgRuleSet[3];
		}
		else if (ruleNo ==4)
		{
			appliedRule = bgRuleSet[4];
		}
		monitor = o;
		scene = new BGScene();
		addObserver(scene); //adding observer to the JPanel class for drawing plant
	}
	

	/**
	 * Getter for obtaining generations of stems
	 * @return ArrayList<BGGeneration>
	 */
	public ArrayList<BGGeneration> getGenerationsOfStem() {
		return generationsOfStem;
	}

	/**
	 * run method which is called when new thread is instantiated
	 * @return void
	 */
	@Override
	public void run() 
	{
			//creating the trunk of the plant which will be the base stem
			log_bgGenerationSet.info("Creating a base stem - trunk of the plant");
			BGStem trunk = new BGStem();
			trunk.setStart(new Point (BioGrowth.getFrame().getWidth()/2,30));
			trunk.setDisplacement(0, 180);
			trunk.setEnd();
			log_bgGenerationSet.info("Creating the first generation of the plant - which consists of the trunk of the plant");
			BGGeneration firstGen = new BGGeneration();
			firstGen.addStemToGeneration(trunk);
			generationsOfStem.add(firstGen);
			
			currentGeneration = firstGen;
			
			//send Generation to observer - BGScenes
			setChanged();
			notifyObservers(generationsOfStem);
			
			//Wait after printing one generation
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				log_bgGenerationSet.warning("Interrupted Exception encountered:"+e.getMessage());
			}
			catch (Exception e) {
				log_bgGenerationSet.warning("Exception encountered:"+e.getMessage());
			}
			
			//refresh the display
//			log_bgGenerationSet.info("Refreshing the display after putting thread to sleep");
			try {
				scene.repaint();
				SwingUtilities.updateComponentTreeUI(BioGrowth.getFrame());		
			}
			catch (Exception e) {
				log_bgGenerationSet.warning("Exception encountered:"+e.getMessage());
			}
			
			//printing the later generations
			while(countOfGeneration < BioGrowth.getMaxGenerations() && (!BioGrowth.isStopFlag())) 
			{
				if (flagForPauseResume)
				{
					log_bgGenerationSet.info("Rule changed::"+appliedRule);
					synchronized (monitor)
					{
						try {
							monitor.wait();
						} catch (InterruptedException e) {
							log_bgGenerationSet.warning("Interrupted Exception encountered:"+e.getMessage());
						}
					}
				}
				
				BGGeneration nextGen = currentGeneration.growFromCurrentGeneration(appliedRule);
				generationsOfStem.add(nextGen);
				currentGeneration = nextGen;
				countOfGeneration++;
				
////				int timeFactor = countOfGeneration * 450;
//				//Wait after printing one generation
//				try {
//					Thread.sleep(3000);
//				} 
//				catch (InterruptedException e) {
//					log_bgGenerationSet.warning("Interrupted Exception encountered:"+e.getMessage());
//				}
//				catch (Exception e) {
//					log_bgGenerationSet.warning("Exception encountered:"+e.getMessage());
//				}
				
				//refresh the display
				try
				{
					scene.repaint();
					SwingUtilities.updateComponentTreeUI(BioGrowth.getFrame());		
				}
				catch (Exception e) {
					log_bgGenerationSet.warning("Exception encountered:"+e.getMessage());
				}
				
				//send an ArrayList to observer - BGCanvas
				setChanged();
				notifyObservers(generationsOfStem);
				
				//Wait after printing one generation
				try {
					Thread.sleep(3000);
				} 
				catch (InterruptedException e) {
					log_bgGenerationSet.warning("Interrupted Exception encountered:"+e.getMessage());
				}
				catch (Exception e) {
					log_bgGenerationSet.warning("Exception encountered:"+e.getMessage());
				}
							
			}
	}
	
	/**
	 * Update appliedRule after user changes rule mid generation
	 * @return void
	 */
	public void setRule(int ruleNo)
	{
		if (ruleNo==0)
		{
			appliedRule = bgRuleSet[0];
		}
		else if (ruleNo ==1)
		{
			appliedRule = bgRuleSet[1];
		}
		else if (ruleNo ==2)
		{
			appliedRule = bgRuleSet[2];
		}
		else if (ruleNo ==3)
		{
			appliedRule = bgRuleSet[3];
		}
		else if (ruleNo ==4)
		{
			appliedRule = bgRuleSet[4];
		}
	}


	/**
	 * @return the flagForPauseResume
	 */
	public boolean isFlagForPauseResume() {
		return flagForPauseResume;
	}


	/**
	 * @param flagForPauseResume the flagForPauseResume to set
	 */
	public void setFlagForPauseResume(boolean flagForPauseResume) {
		this.flagForPauseResume = flagForPauseResume;
	}
	
	
	

}
