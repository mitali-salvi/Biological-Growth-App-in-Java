/**
 * 
 */
package edu.neu.csye6200.bg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author mitalisalvi
 * NUID: 001630137
 */
public class BioGrowth extends BGAppHelper 
{
	//logger for logging all logs
	public static final Logger log_biogrowth = Logger.getLogger(BioGrowth.class.getName());
	
	protected JPanel mainPanel ; //main panel to display the plant
	protected JButton startBtn ; //start button which starts the plant growth
	protected JButton pauseBtn ; //pause button which pauses the plant growth
	protected JButton resumeBtn; //resume button which resumes growth after pausing
	protected JButton stopBtn; //stop button which stops plant growth
    private JComboBox<String> comboBox; //combo box for selecting rule
    private JLabel maxNoOfGenerations; //label for text
    private JTextField maxNoOfGenerationsTL; //input text field
    
    int userSelectedRule = 0;
    private static int rule =0;
    private static int maxGeneration = 0;
    
    private Thread threadForGeneration = null; //thread for generating generations in the background
    
    private BGScene bgScene = null; //display panel
    private BGGenerationSet plant = null; //plant growth
 
    //flags for tracking buttons
    private static boolean startFlag ;
	private static boolean pauseFlag ;
	private static boolean resumeFlag ;
	private static boolean stopFlag ;
	
	private Object monitor; //thread monitor for synchronization
    
	//constructor to initialize frame and add bg scene panel
	public BioGrowth() 
	{
		super();
		monitor = new Object(); 
		bgScene = new BGScene();
		frame.add(bgScene, BorderLayout.CENTER);
		showUI();
	}

	/**
	 * Main method which starts the plant growth application
	 * @param args String[]
	 * @return void
	 */
	public static void main(String[] args) 
	{
		BioGrowth bg = new BioGrowth();
		log_biogrowth.info("Growth of bio tree started");
	}

	/**
	 * Invoked when an action occurs
	 * @param e ActionEvent
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		log_biogrowth.info("Action performed:"+e.getSource());
		
		if (e.getSource() == startBtn)
		{
			log_biogrowth.info("Start plant growth");			

			//check if max generation is entered by user or not. If not, throw error
			boolean valid = true;
			if (maxNoOfGenerationsTL.getText().isEmpty() || maxGeneration ==0)
			{
				JOptionPane.showMessageDialog(bgScene, "Please enter maximum number of generations", "Error", JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
			
			if (valid)
			{
				// create a Runnable reference
				plant = new BGGenerationSet(rule , maxGeneration , monitor);
				threadForGeneration = new Thread(plant);
				log_biogrowth.info("Created thread for BG Generation Set:"+threadForGeneration.getName());
				
				startFlag = true;
				stopFlag = false;
				
				comboBox.setSelectedIndex(userSelectedRule);
				
				bgScene.setClearFlag(false);
				
				//start the thread for growing plants
				threadForGeneration.start();
				
				//disable start button after pressing it once. 
				//ensures that multiple threads are not started if user presses start button accidently
				startBtn.setEnabled(false);
				
				//disable and enable buttons according to start action 
				resumeBtn.setEnabled(false);
				pauseBtn.setEnabled(true);
				comboBox.setEnabled(false);
				
			}
		}
		
		if(e.getSource() == pauseBtn) {
			log_biogrowth.info("Plant growth paused");	
			pauseFlag = true;
			resumeFlag = false;
			
			comboBox.setSelectedIndex(userSelectedRule);
			
			//disable and enable buttons according to pause action 
			resumeBtn.setEnabled(true); 
			comboBox.setEnabled(true);
			
			plant.setFlagForPauseResume(true);
		}
		
		if(e.getSource() == resumeBtn) 
		{
			log_biogrowth.info("Plant growth resumed");
			resumeFlag = true;
			pauseFlag = false;
			
			comboBox.setSelectedIndex(userSelectedRule);
			
			//disable and enable buttons according to resume action 
			comboBox.setEnabled(false);
			
			plant.setFlagForPauseResume(false);
			synchronized (monitor) 
			{
				monitor.notifyAll();
			}
		} 
		
		if(e.getSource() == stopBtn) {
			log_biogrowth.info("Stop plant growth");
			stopFlag = true;
			
			//comboBox.setSelectedIndex(comboBox.getSelectedIndex());
			
			//enable start button and clear panel for redrawing
			bgScene.repaint();
			bgScene.revalidate();

			bgScene.setClearFlag(true);
			
			resumeFlag = true;
			pauseFlag = false;
			
			//disable and enable buttons according to stop action
			startBtn.setEnabled(true);
			pauseBtn.setEnabled(false);
			resumeBtn.setEnabled(false);
			comboBox.setEnabled(true);
			maxNoOfGenerationsTL.setEnabled(true);
		} 
		
		if(e.getSource() == comboBox) 
		{
			log_biogrowth.info("Plant growth paused");
			userSelectedRule = comboBox.getSelectedIndex();
			switch(userSelectedRule) {
			case 0: 
				rule = 0;
				break;
			case 1: 
				rule = 1;
				break;
			case 2: 
				rule = 2;
				break;
			case 3:
				rule = 3;
				break;
			case 4:
				rule = 4;
				break;
			}
			comboBox.setSelectedIndex(userSelectedRule);
			log_biogrowth.info("Rule selected by the user::"+rule);
			
			if (pauseFlag)
			{
				log_biogrowth.info("Rule changed by the user::"+rule);
				plant.setRule(rule);
			}
		}
		
		if (e.getSource() == maxNoOfGenerationsTL)
		{
			boolean valid = true;
			
			if (maxNoOfGenerationsTL.getText().isEmpty())
			{
				log_biogrowth.info("No text entered by the user");
				JOptionPane.showMessageDialog(bgScene, "Please enter maximum number of generations", "Error", JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
			else
			{
				maxGeneration = Integer.parseInt(maxNoOfGenerationsTL.getText());
				log_biogrowth.info("maxGen::"+maxGeneration);
				if (maxGeneration ==8 || maxGeneration==9 || maxNoOfGenerationsTL.getText().length()>1)
				{
					log_biogrowth.info("Number greater than 7");
					JOptionPane.showMessageDialog(bgScene, "The maximum number of generations is 7", "Error", JOptionPane.ERROR_MESSAGE);
					valid = false;
				}
			}
			
			if (valid)
			{
				//disable text field
				maxNoOfGenerationsTL.setEnabled(false);
				maxNoOfGenerationsTL.setBackground(Color.WHITE);
			}
		}
		log_biogrowth.info("Number of active threads from the given thread: " + Thread.activeCount());
	}

	/**
	 * Creates the base panel for the application
	 * @return panel displayed for the application
	 */
	@SuppressWarnings("serial")
	@Override
	public JPanel getPanel() 
	{
		log_biogrowth.info("Setting up the panel, buttons and text field");
		mainPanel = new JPanel();
    	mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    	
    	//Add buttons for execution of application
    	startBtn = new JButton("Start");
    	pauseBtn = new JButton("Pause"); 
    	resumeBtn = new JButton("Resume");
    	stopBtn = new JButton ("Stop");
    	
    	//Adding a drop down box for selecting rule
	    String[] choices = { "Rule 1","Rule 2", "Rule 3","Rule 4","Rule 5"};
	    comboBox = new JComboBox<String>(choices);
	    comboBox.setPrototypeDisplayValue("1234567890");
    	
    	mainPanel.add(startBtn);
    	mainPanel.add(pauseBtn);
    	mainPanel.add(resumeBtn);
    	mainPanel.add(stopBtn);
	    mainPanel.add(comboBox);
	   
	    //Add a label for max number of generations
	    maxNoOfGenerations = new JLabel("Maximum number of generations (Max=7): ");
	    maxNoOfGenerationsTL = new JTextField(5) {
	    	  public void processKeyEvent(KeyEvent ev) {
	    	    char c = ev.getKeyChar();
	    	    if (c >= 48 && c <= 57 || ev.getKeyCode() == KeyEvent.VK_ENTER || ev.getKeyCode() == KeyEvent.VK_BACK_SPACE) { //Only numeric and ENTER can be entered
	    	      super.processKeyEvent(ev);
	    	    }
	    	  }
	    	};

	    maxNoOfGenerations.setDisplayedMnemonic(KeyEvent.VK_U);
	    maxNoOfGenerations.setLabelFor(maxNoOfGenerationsTL);
	    
	    mainPanel.add(maxNoOfGenerations);
	    mainPanel.add(maxNoOfGenerationsTL);
	    
	    //allow the application to hear about button pushes
    	startBtn.addActionListener(this); 
    	pauseBtn.addActionListener(this);
    	resumeBtn.addActionListener(this);
    	stopBtn.addActionListener(this);
	    comboBox.addActionListener(this);
	    maxNoOfGenerationsTL.addActionListener(this);

    	return mainPanel;
	}
    
	/**
	 * Return the rule number selected by the user
	 * @return rule selected
	 */
    public static int getRule() {
		return rule;
	}

	/**
	 * Return the main frame
	 * @return main frame
	 */
    public static JFrame getFrame() {
		return frame;
	}
    
	/**
	 * Return maximum number of generations
	 * @return maximum number of generations
	 */
    public static int getMaxGenerations() {
    	return maxGeneration;
    }
    
    //getters for all the flags 
    
	public static boolean isStartFlag() {
		return startFlag;
	}

	public static boolean isPauseFlag() {
		return pauseFlag;
	}

	public static boolean isResumeFlag() {
		return resumeFlag;
	}
	
	public static boolean isStopFlag() {
		return stopFlag;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}
	
	

}
