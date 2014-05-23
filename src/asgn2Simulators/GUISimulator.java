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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public abstract class GUISimulator extends JFrame implements Runnable, ActionListener{

	//Parameter Setup
	
	// Display for simulation messages
	private JTextArea display;           
	private JScrollPane textScrollPane; 
	
	//Text Fields: Things to be changed via entering data and command line
	private JTextField defaultSeedText; //Default Seed
	private JTextField defaultCarProbText; //Default Car Probability
	private JTextField defaultSmallCarProbText; //Default Small Car Probability
	private JTextField defaultMotorCycleProbText; //Default Motor Cycle Probability
	private JTextField defaultIntendedMeanText; //Default Intended Stay Mean
	private JTextField defaultIntendedSDText; //Default Intended Stay Standard Deviation
	private JTextField defaultSpacesText; //Default Maximum Car Spaces
	private JTextField defaultSmallSpacesText; //Default Maximum Small Car Spaces
	private JTextField defaultMotorCycleSpacesText; //Default Maximum Motor Cycle Spaces
	private JTextField defaultMaxQueueText; //Default Maximum Queue Size
	
	//Start button
	private JButton startButton;
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
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	//@Override
	public void initialiseComponents() {	
		// Choose a grid layout for the main frame
		this.setLayout(new GridBagLayout());
		
		// Create a scrollable text area for displaying instructions and messages
		display = new JTextArea(5, 40); // lines by columns
		display.setEditable(false);
		display.setLineWrap(true);
		textScrollPane = new JScrollPane(display);
		this.add(textScrollPane, positionConstraints(Position.TOPCENTRE, mainMargin));
		resetDisplay("Set the initial simulation parameters and press 'Start'\n\n");

		// Button for starting the simulation
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		
		// Add editable panels for simulation parameters
		defaultSeedText = addParameterPanel("Random number seed:", Constants.DEFAULT_SEED);
		defaultCarProbText = addParameterPanel("Dam capacity (megalitres):", Constants.DEFAULT_CAR_PROB);
		defaultSmallCarProbText = addParameterPanel("Maximum daily inflow (megalitres):", Constants.DEFAULT_SMALL_CAR_PROB);
		defaultMotorCycleProbText = addParameterPanel("Maximum daily consumption (megalitres):", Constants.DEFAULT_MOTORCYCLE_PROB);
		defaultIntendedMeanText = addParameterPanel("Default downriver release (megalitres):", Constants.DEFAULT_INTENDED_STAY_MEAN);
		defaultIntendedSDText = addParameterPanel("Job duration (days):", Constants.DEFAULT_INTENDED_STAY_SD);
		defaultSpacesText = addParameterPanel("Maximum daily inflow (megalitres):", Constants.DEFAULT_MAX_CAR_SPACES);
		defaultSmallSpacesText = addParameterPanel("Maximum daily consumption (megalitres):", Constants.DEFAULT_MAX_SMALL_CAR_SPACES);
		defaultMotorCycleSpacesText = addParameterPanel("Default downriver release (megalitres):", Constants.DEFAULT_MAX_MOTORCYCLE_SPACES);
		defaultMaxQueueText = addParameterPanel("Job duration (days):", Constants.DEFAULT_MAX_QUEUE_SIZE);
		
		// Panel to contain the buttons
		buttons = new JPanel(new GridBagLayout());
		buttons.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Dam", CENTER, BOTTOM));
		this.add(buttons, positionConstraints(Position.BOTTOMCENTRE, mainMargin));
		buttons.setVisible(true);
	}
	
	/*
	 * Convenience method for resetting the text in the display area
	 */
	private void resetDisplay(String initialText) {
		display.setText(initialText);
	}

	/*
	 * Convenience method for adding text to the display area without
	 * overwriting what's already there
	 */
	private void appendDisplay(String newText) {
		display.setText(display.getText() + newText);
	}
	
	/*
	 * Convenience method to add a labelled, editable text field to the
	 * main frame, with a fixed label and a mutable default text value
	 */
	private JTextField addParameterPanel(String label, Number defaultValue) {
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
	private GridBagConstraints positionConstraints(Position location, Integer margin) {
		
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
	
	/*
	 * Perform an appropriate action when a button is pushed
	 */
	public void actionPerformed(ActionEvent event) {
		
		// Get event's source 
		Object source = event.getSource(); 

		//Consider the alternatives (not all are available at once) 
		if (source == startButton)
		{
			//startSimulation();
		}
		/*
		else if (source == halfReleaseButton)
		{
			simulateOneDay(Selection.HALF);
			checkForEndOfSimulation();
		}
		else if (source == defaultReleaseButton)
		{
			simulateOneDay(Selection.DEFAULT);
			checkForEndOfSimulation();
		}
		else if (source == doubleReleaseButton)
		{
			simulateOneDay(Selection.DOUBLE);
			checkForEndOfSimulation();
		};
		*/
	}
}
