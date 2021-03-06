/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Simulators 
 * 20/04/2014
 * 
 */
package asgn2Simulators;

import static javax.swing.border.TitledBorder.BOTTOM;
import static javax.swing.border.TitledBorder.CENTER;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {
	//Parameter Setup
	private SimulationRunner sr;
	// Display for simulation messages
	private static JTextArea display;           
	private JScrollPane textScrollPane; 
	
	//Text Fields: Things to be changed via entering data and command line
	//Public to allow being set in command line.
	public JTextField defaultSeedText; //Default Seed
	public JTextField defaultCarProbText; //Default Car Probability
	public JTextField defaultSmallCarProbText; //Default Small Car Probability
	public JTextField defaultMotorCycleProbText; //Default Motor Cycle Probability
	public JTextField defaultIntendedMeanText; //Default Intended Stay Mean
	public JTextField defaultIntendedSDText; //Default Intended Stay Standard Deviation
	public JTextField defaultSpacesText; //Default Maximum Car Spaces
	public JTextField defaultSmallSpacesText; //Default Maximum Small Car Spaces
	public JTextField defaultMotorCycleSpacesText; //Default Maximum Motor Cycle Spaces
	public JTextField defaultMaxQueueText; //Default Maximum Queue Size
	
	//Start button
	private JButton startButton;
	private JButton lineGraph;
	private JButton barChart;
	private JPanel buttons;

	// Places where we'll add components to a frame
	private enum Position {MIDDLELEFT, TOPCENTRE, MIDDLECENTRE, BOTTOMCENTRE};
	
	// How big a margin to allow for the main frame
	final Integer mainMargin = 20; // pixels
	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		//super(arg0);
		initialiseComponents();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	//@Override
	public void initialiseComponents() {	
		// Choose a grid layout for the main frame
		this.setLayout(new GridBagLayout());
		
		// Create a scrollable text area for displaying instructions and messages
		display = new JTextArea(10, 40); // lines by columns
		display.setEditable(false);
		display.setLineWrap(true);
		textScrollPane = new JScrollPane(display);
		this.add(textScrollPane, positionConstraints(Position.TOPCENTRE, mainMargin));
		resetDisplay("Set the initial simulation parameters and press 'Start'\n\n");

		// Add editable panels for simulation parameters
		defaultSeedText = addParameterPanel("Random number seed:",
				Constants.DEFAULT_SEED);
		defaultCarProbText = addParameterPanel("Default car probability:",
				Constants.DEFAULT_CAR_PROB);
		defaultSmallCarProbText = addParameterPanel("Default small car probability:",
				Constants.DEFAULT_SMALL_CAR_PROB);
		defaultMotorCycleProbText = addParameterPanel("Default motor cycle probability:", 
				Constants.DEFAULT_MOTORCYCLE_PROB);
		defaultIntendedMeanText = addParameterPanel("Default intended stay mean:", 
				Constants.DEFAULT_INTENDED_STAY_MEAN);
		defaultIntendedSDText = addParameterPanel("Default intended stay standard deviation:",
				Constants.DEFAULT_INTENDED_STAY_SD);
		defaultSpacesText = addParameterPanel("Default maximum car spaces:", 
				Constants.DEFAULT_MAX_CAR_SPACES);
		defaultSmallSpacesText = addParameterPanel("Default maximum small car spaces:", 
				Constants.DEFAULT_MAX_SMALL_CAR_SPACES);
		defaultMotorCycleSpacesText = addParameterPanel("Default maximum motor cycle spaces:",
				Constants.DEFAULT_MAX_MOTORCYCLE_SPACES);
		defaultMaxQueueText = addParameterPanel("Default maximum queue:",
				Constants.DEFAULT_MAX_QUEUE_SIZE);
		
		// Panel to contain the buttons
		buttons = new JPanel(new GridBagLayout());
		buttons.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Car Park", CENTER, BOTTOM));
		this.add(buttons, positionConstraints(Position.BOTTOMCENTRE, mainMargin));
		buttons.setVisible(true);
		
		// Button for starting the simulation
		startButton = new JButton("Start");
		lineGraph = new JButton("Show Line Graph");
		barChart = new JButton("Show Bar Chart");
		buttons.add(startButton);
		buttons.add(lineGraph);
		buttons.add(barChart);
		lineGraph.setEnabled(false);
		barChart.setEnabled(false);

		startButton.addActionListener(new ActionListener() 
		{   
          //Handle JButton event if Enter key is pressed or if mouse is clicked.  
          public void actionPerformed(ActionEvent event) 
          {
        	  startSimulation();
          }
		}
		);
		
		lineGraph.addActionListener(new ActionListener() 
		{   
          //Handle JButton event if Enter key is pressed or if mouse is clicked.  
          public void actionPerformed(ActionEvent event) 
          {
      		//Show Chart
      		sr.tsc.createLineChart();
    		lineGraph.setEnabled(false);
          }
		}
		);
		
		barChart.addActionListener(new ActionListener() 
		{   
          //Handle JButton event if Enter key is pressed or if mouse is clicked.  
          public void actionPerformed(ActionEvent event) 
          {
      		ChartPanel.createBarChart(sr.carPark.getStatus(Constants.CLOSING_TIME));
    		barChart.setEnabled(false);
      	  }
		}
		);
	}
	
	/*
	 * Convenience method for resetting the text in the display area
	 */
	public static void resetDisplay(String initialText) {
		display.setText(initialText);
	}

	/*
	 * Convenience method for adding text to the display area without
	 * overwriting what's already there
	 */
	public static void appendDisplay(String newText) {
		display.setText(display.getText() + newText);
	}
	
	/*
	 * Convenience method to add a labelled, editable text field to the
	 * main frame, with a fixed label and a mutable default text value
	 */
	private JTextField addParameterPanel(String label, Number defaultValue) 
	{
		// A parameter panel has two components, a label and a text field
		JPanel parameterPanel = new JPanel();
		JLabel parameterLabel = new JLabel(label);
		JTextField parameterText = new JTextField("" + defaultValue, 3);
		// Add the label to the parameter panel
		parameterLabel.setHorizontalAlignment(JTextField.RIGHT); // flush right
		parameterPanel.add(parameterLabel);
		// Add the text field
		parameterText.setEditable(true);
		parameterText.setHorizontalAlignment(JTextField.RIGHT); // flush right
		parameterPanel.add(parameterText);
		// Add the parameter panel to the main frame
		this.add(parameterPanel, positionConstraints(Position.MIDDLELEFT, mainMargin));
		// Return the newly-created text field (but not the label, which never changes)
		return parameterText;
	}
	
	/*
	 * Convenience method for creating a set of positioning constraints for the
	 * specific layout we want for components of our GUI
	 */
	private GridBagConstraints positionConstraints(Position location, Integer margin) 
	{
		GridBagConstraints constraints = new GridBagConstraints();
		switch (location) {
		case TOPCENTRE:
			constraints.anchor = GridBagConstraints.NORTH;
			constraints.insets = new Insets(margin, margin, 0, margin); // top, left, bottom, right	
			constraints.gridwidth = GridBagConstraints.REMAINDER; // component occupies whole row
			break;
		case MIDDLECENTRE:
			constraints.anchor = GridBagConstraints.CENTER;
			constraints.insets = new Insets(margin, margin, margin, margin); // top, left, bottom, right	
			constraints.gridwidth = GridBagConstraints.REMAINDER; // component occupies whole row
			constraints.weighty = 100; // give extra vertical space to this object
			break;
		case BOTTOMCENTRE:
			constraints.anchor = GridBagConstraints.SOUTH;
			constraints.insets = new Insets(margin, margin, margin, margin); // top, left, bottom, right	
			constraints.gridwidth = GridBagConstraints.REMAINDER; // component occupies whole row
			constraints.weighty = 100; // give extra vertical space to this object
			break;
		case MIDDLELEFT:
			constraints.anchor = GridBagConstraints.WEST;
			constraints.insets = new Insets(0, margin, 0, margin); // top, left, bottom, right	
			constraints.gridwidth = GridBagConstraints.REMAINDER; // component occupies whole row
			constraints.weightx = 100; // give extra horizontal space to this object
			break;
		}
		return constraints;
	}
	
	private void startSimulation()
	{
		try
		{
			//Ensure correct type of data:
			int defaultSeed = Integer.parseInt(defaultSeedText.getText().trim());
			double carProb = Double.parseDouble(defaultCarProbText.getText().trim());
			double smallCarProb = Double.parseDouble(defaultSmallCarProbText.getText().trim());
			double motorCycleProb = Double.parseDouble(defaultMotorCycleProbText.getText().trim());
			double intendedMean = Double.parseDouble(defaultIntendedMeanText.getText().trim());
			double intendedSD = Double.parseDouble(defaultIntendedSDText.getText().trim());
			int maxCarspaces = Integer.parseInt(defaultSpacesText.getText().trim());
			int maxSmallCarSpaces = Integer.parseInt(defaultSmallSpacesText.getText().trim());
			int maxMotorCycleSpaces = Integer.parseInt(defaultMotorCycleSpacesText.getText().trim());
			int maxQueueSize = Integer.parseInt(defaultMaxQueueText.getText().trim());
			
			//Tests to ensure valid data:
			/*
			 * At this point, you should do the proper checks on the CarPark:
			�	maxCarSpaces, maxMotorCycleSpaces, maxQueueSize >= 0
			� 0 <= maxSmallCarSpaces <= maxCarSpaces
			 */
			if (maxCarspaces < 0)
			{
				throw new SimulationException("Maximum car spaces must be non-negative, given "
						+ maxCarspaces);
			}
			if (maxMotorCycleSpaces < 0)
			{
				throw new SimulationException("Maximum motor cycle spaces must be non-negative, given "
						+ maxMotorCycleSpaces);
			}
			if (maxQueueSize < 0)
			{
				throw new SimulationException("Maximum queue size must be non-negative, given "
						+ Constants.DEFAULT_MAX_QUEUE_SIZE);
			}
			if ((maxSmallCarSpaces < 0) || (maxSmallCarSpaces > maxCarspaces))
			{
				throw new SimulationException("Maximum small car spaces must be non-negative and less "
						+ "then the maximum car spaces, given "
						+ maxSmallCarSpaces);
			}
			
			//Disable things on successful run
			defaultSeedText.setEnabled(false);
			defaultCarProbText.setEnabled(false);
			defaultSmallCarProbText.setEnabled(false);
			defaultMotorCycleProbText.setEnabled(false);
			defaultIntendedMeanText.setEnabled(false);
			defaultIntendedSDText.setEnabled(false);
			defaultSpacesText.setEnabled(false);
			defaultSmallSpacesText.setEnabled(false);
			defaultMotorCycleSpacesText.setEnabled(false);
			defaultMaxQueueText.setEnabled(false);
			startButton.setEnabled(false);
			lineGraph.setEnabled(true);
			barChart.setEnabled(true);
			
			run(defaultSeed, carProb, smallCarProb,
					motorCycleProb, intendedMean, intendedSD,
					maxCarspaces, maxSmallCarSpaces, maxMotorCycleSpaces, maxQueueSize);

		}	
		catch (NumberFormatException exception) // User has entered an invalid number
		{
			appendDisplay("Invalid number - " + exception.getMessage() + "\n");
		}
		catch (SimulationException exception) //Invalid Data
		{
			appendDisplay(exception.getMessage() + "\n");
		}
	}
	
	public void run(int defaultSeed, double carProb, double smallCarProb,
			double motorCycleProb, double intendedMean, double intendedSD,
			int maxCarspaces, int maxSmallCarSpaces, int maxMotorCycleSpaces, int maxQueueSize) {
		//Run the simulation 
		CarPark cp = null;
		Simulator s = null;
		Log l = null; 
		try {
			cp = new CarPark(maxCarspaces, maxSmallCarSpaces, maxMotorCycleSpaces, maxQueueSize);
			s = new Simulator(defaultSeed, intendedMean, intendedSD, carProb, smallCarProb, motorCycleProb);
			l = new Log();
		} catch (IOException | SimulationException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		sr = new SimulationRunner(cp,s,l);
		try {
			sr.runSimulation();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} 
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
