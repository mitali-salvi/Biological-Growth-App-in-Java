package edu.neu.csye6200.bg;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

/**
* @author mitalisalvi
* NUID: 001630137
*/
public abstract class BGAppHelper implements ActionListener, WindowListener 
{
	protected static JFrame frame = null;

	/**
	 * The Biological growth constructor
	 */
	public BGAppHelper() {
		initGUI();
	}
	
	/**
	 * Initialize the Graphical User Interface
	 */
    public void initGUI() 
    {
    	frame = new JFrame();
		frame.setSize(1500, 850);
		frame.setResizable(true);
		frame.setMinimumSize(new Dimension(800,500));
		frame.setTitle("BioGrowth");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(this);
		frame.setLayout(new BorderLayout());
		frame.add(getPanel(), BorderLayout.NORTH);
    }
    
    /**
     * Override this method to provide the main content panel.
     * @return a JPanel, which contains the main content of of your application
     */
    public abstract JPanel getPanel() ;

    
    /**
     * A convenience method that uses the Swing dispatch threat to show the UI.
     * This prevents concurrency problems during component initialization.
     */
    public void showUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	frame.setVisible(true); // The UI is built, so display it;
            }
        });	
    }
    
    /**
     * Shut down the application
     */
    public void exit() {
    	frame.dispose();
    	System.exit(0);
    }

    /**
     * Override this method to show a About Dialog
     */
    public void showHelp() {
    }
	
}