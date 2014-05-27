/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Simulation 
 * 20/04/2014
 * 
 */
package asgn2Simulators;

/**
 * Global constants for the simulation
 * @author hogan
 *
 */
public class Constants {
	//Basic simulation time parameters - unchangeable 
	public static final int MINIMUM_STAY = 20;
	public static final int MAXIMUM_QUEUE_TIME = 25;
	public static final int CLOSING_TIME = 18*60;
	public static final int LAST_ENTRY = CLOSING_TIME-60;
	
	//RNG and Probs
	public static final int DEFAULT_SEED = 100; 
	public static final double DEFAULT_CAR_PROB = 1.0;
	public static final double DEFAULT_SMALL_CAR_PROB = 0.20;
	public static final double DEFAULT_MOTORCYCLE_PROB = 0.05;
	public static final double DEFAULT_INTENDED_STAY_MEAN = 120.0;
	public static final double DEFAULT_INTENDED_STAY_SD = 0.33*Constants.DEFAULT_INTENDED_STAY_MEAN;
	
	//Default Size parameters
	public static final int DEFAULT_MAX_CAR_SPACES = 100;
	public static final int DEFAULT_MAX_SMALL_CAR_SPACES = 20;
	public static final int DEFAULT_MAX_MOTORCYCLE_SPACES = 20;
	public static final int DEFAULT_MAX_QUEUE_SIZE = 10;
}
