package edu.neu.csye6200.bg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.JPanel;

/**
 * @author mitalisalvi 
 * NUID: 001630137
 */
public class BGScene extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// logger for logging all logs
	private Logger log_bgScene = Logger.getLogger(BGScene.class.getName());

	private static ArrayList<BGGeneration> generationArrayList;
	private static Random random = new Random(); // Random for generation of random colors
	public boolean clearFlag = false;
	
	public BGScene()
	{
		setBackground(Color.BLACK);
		setSize(new Dimension(getWidth(), getHeight()));
	}

	@Override
	protected void paintComponent(Graphics g) 
	{
//		log_bgScene.info("Paint method called");
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int x = this.getWidth() / 2;
		int y = this.getHeight() / 2;
		g2d.rotate(Math.toRadians(180.0), x, y);
		drawTree(g2d);
	}

	/**
	 * Method which draws the plant
	 * @param Graphics2D
	 * @return void
	 */
	private void drawTree(Graphics2D g2d) 
	{
		
		if (!clearFlag) 
		{
			try 
			{
//				BGGeneration trunk = generationArrayList.get(0);
				log_bgScene.info("in DrawTree generationArrayList:::" + generationArrayList.size());
				for (BGGeneration c : generationArrayList) 
				{
					for (BGStem stem : c.getStemsOfGeneration()) 
					{
						if (BioGrowth.getRule() == 0) 
						{
							g2d.setColor(new Color(34, 139, 34));
							g2d.setStroke(new BasicStroke(3));
						}
						if (BioGrowth.getRule() == 1) 
						{
							g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
							Color color1 = Color.RED;
							Color color2 = Color.GREEN;
							GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
							g2d.setPaint(gp);
//							g2d.setColor(Color.BLUE);
							g2d.setStroke(new BasicStroke(3));
						}
						if (BioGrowth.getRule() == 2) 
						{
							g2d.setColor(new Color(255, 215, 0));
						}
						if (BioGrowth.getRule() == 3)
						{
							g2d.setColor(Color.WHITE);
						}
						if ( BioGrowth.getRule() == 4) 
						{
							g2d.setStroke(new BasicStroke(3));
							g2d.setColor(Color.WHITE);
						}

						// draw line
						g2d.drawLine(stem.getStart().x, stem.getStart().y, stem.getEnd().x, stem.getEnd().y);

						//draw flower in the last generation
						if ((BioGrowth.getMaxGenerations() + 1) == generationArrayList.size()) 
						{
							if (BioGrowth.getRule() == 0) 
							{
								//no changes
							}
							if (BioGrowth.getRule() == 1) 
							{
								//no change
							}
							if (BioGrowth.getRule() == 2) 
							{
								g2d.setColor(new Color(255, 215, 0));
								g2d.fillRect(stem.getEnd().x, stem.getEnd().y, 5, 5);
							}
							if (BioGrowth.getRule() == 3) 
							{
								int redValue = random.nextInt(255);
								int greenValue = random.nextInt(255);
								int blueValue = random.nextInt(255);
								g2d.setColor(new Color(redValue, greenValue, blueValue));
								g2d.fillOval(stem.getEnd().x, stem.getEnd().y, 5, 5);
							}
							if (BioGrowth.getRule() == 4) 
							{
								//no change
							}
						}
					}
				}

			} catch (Exception e) 
			{
				log_bgScene.warning("Exception:"+e.getMessage());
			}
		} 
		else 
		{
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	/**
	 * Update if notification sent from Observable object
	 * @param Observable o, Object arg
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) 
	{
		log_bgScene.info("Received an update from the observable class");
		generationArrayList = ((ArrayList<BGGeneration>) arg);
		repaint();
	}

	/**
	 * Set or unset the clear flag
	 * @param boolean
	 * @return void
	 */
	public void setClearFlag(boolean clearFlag) {
		this.clearFlag = clearFlag;
	}

}
