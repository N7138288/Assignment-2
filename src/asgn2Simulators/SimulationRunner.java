/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Simulators 
 * 23/04/2014
 * 
 */
package asgn2Simulators;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextField;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;

/**
 * Class to operate the simulation, taking parameters and utility methods from the Simulator
 * to control the CarPark, and using Log to provide a record of operation. 
 * @author hogan
 *
 */
public class SimulationRunner {
	private CarPark carPark;
	private Simulator sim;
	
	private Log log;
	
	private ChartPanel tsc;
	
	/**
	 * Constructor just does initialisation 
	 * @param carPark CarPark currently used 
	 * @param sim Simulator containing simulation parameters
	 * @param log Log to provide logging services 
	 */
	public SimulationRunner(CarPark carPark, Simulator sim,Log log) {
		this.carPark = carPark;
		this.sim = sim;
		this.log = log;
		
		this.tsc = new ChartPanel("Car Park Simulator");
	}
	
	
	/**
	 * Method to run the simulation from start to finish. Exceptions are propagated upwards from Vehicle,
	 * Simulation and Log objects as necessary 
	 * @throws VehicleException if Vehicle creation or operation constraints violated 
	 * @throws SimulationException if Simulation constraints are violated 
	 * @throws IOException on logging failures
	 */
	public void runSimulation() throws VehicleException, SimulationException, IOException {
		//Start chart
		this.tsc.createLineChartData();
		
		//
		this.log.initialEntry(this.carPark,this.sim);
		for (int time=0; time<=Constants.CLOSING_TIME; time++) {
			//queue elements exceed max waiting time
			if (!this.carPark.queueEmpty()) {
				this.carPark.archiveQueueFailures(time);
			}
			//vehicles whose time has expired
			if (!this.carPark.carParkEmpty()) {
				//force exit at closing time, otherwise normal
				boolean force = (time == Constants.CLOSING_TIME);
				this.carPark.archiveDepartingVehicles(time, force);
			}
			//attempt to clear the queue 
			if (!this.carPark.carParkFull()) {
				this.carPark.processQueue(time,this.sim);
			}
			// new vehicles from minute 1 until the last hour
			if (newVehiclesAllowed(time)) { 
				this.carPark.tryProcessNewVehicles(time,this.sim);
			}
			//Log progress 
			this.log.logEntry(time,this.carPark);
			
			//Chart progress
			this.tsc.addLineChartData(time, this.carPark);
		}
		this.log.finalise(this.carPark);
		
		//Finalise Chart
		this.tsc.concludeLineChartData();
		
		//Show Chart
		this.tsc.createLineChart();
	}

	/**
	 * Main program for the simulation 
	 * @param args Arguments to the simulation 
	 */
	public static void main(String[] args) {

		//Run the Gui
    	// Create the main frame
		GUISimulator mainFrame = new GUISimulator("");
	 	// Terminate the program if the user closes the main frame
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Set the title for the main frame
		mainFrame.setTitle("Car Park Simulator");
		// Resize the main frame to fit its components
		mainFrame.pack();
        // Make the simulation visible
        mainFrame.setVisible(true);
        
		//TODO: Implement Argument Processing 
		//Task 3: allow command line components: All or Nothing.
		//Step 1: Zero Arguments - not required
		//Step 2: Ten Arguments
		if (args.length == 10)
		{
			mainFrame.defaultSeedText.setText(args[0]);
			mainFrame.defaultCarProbText.setText(args[1]);
			mainFrame.defaultSmallCarProbText.setText(args[2]);
			mainFrame.defaultMotorCycleProbText.setText(args[3]);
			mainFrame.defaultIntendedMeanText.setText(args[4]);
			mainFrame.defaultIntendedSDText.setText(args[5]);
			mainFrame.defaultSpacesText.setText(args[6]);
			mainFrame.defaultSmallSpacesText.setText(args[7]);
			mainFrame.defaultMotorCycleSpacesText.setText(args[8]);
			mainFrame.defaultMaxQueueText.setText(args[9]);
		}
	} 

	/**
	 * Helper method to determine if new vehicles are permitted
	 * @param time int holding current simulation time
	 * @return true if new vehicles permitted, false if not allowed due to simulation constraints. 
	 */
	private boolean newVehiclesAllowed(int time) {
		boolean allowed = (time >=1);
		return allowed && (time <= (Constants.CLOSING_TIME - 60));
	}

}
