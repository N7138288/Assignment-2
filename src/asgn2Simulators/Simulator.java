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

import java.util.Random;

import asgn2Exceptions.SimulationException;
import asgn2Simulators.Constants;


/**
 * Class to hold  parameters and to provide random trial services for the simulation. 
 * Simulation relies by default on parameters set in the file Constants.java. 
 * User has the option to set these parameters using the multi-argument constructor. 
 * Methods provide intended stay durations according to N(meanStay,sdStay^2), and Bernoulli 
 * trial outcomes for vehicle creation.  
 * @author hogan
 *
 */
public class Simulator {
	
	private Random rng;
	private double meanStay = Constants.DEFAULT_INTENDED_STAY_MEAN;
	private double sdStay = Constants.DEFAULT_INTENDED_STAY_SD;
	private int seed = Constants.DEFAULT_SEED; 
	private double carProb = Constants.DEFAULT_CAR_PROB;
	private double smallCarProb = Constants.DEFAULT_SMALL_CAR_PROB;
	private double mcProb = Constants.DEFAULT_MOTORCYCLE_PROB;
	
	
	/**
	 * Constructor for Simulator using defaults
	 * @throws SimulationException if one or more probabilities are invalid, or if meanStay < 0 or sdStay < 0
	 */
	public Simulator() throws SimulationException {
		this(Constants.DEFAULT_SEED,Constants.DEFAULT_INTENDED_STAY_MEAN,Constants.DEFAULT_INTENDED_STAY_SD,
	 		 Constants.DEFAULT_CAR_PROB,Constants.DEFAULT_SMALL_CAR_PROB,
			 Constants.DEFAULT_MOTORCYCLE_PROB);
	}

	/**
	 * @param seed int random number generator seed
	 * @param meanStay double holding the mean of the Normal Distribution of intended parking stays 
	 * @param sdStay double holding the standard deviation of the stay distribution
	 * @param carProb double holding the probability that a car will arrive in the current minute
	 * @param smallCarProb double holding the probability that the car that has arrived is a small car
	 * @param mcProb double holding the probability that a motorcycle will arrive in the current minute
	 * @throws SimulationException if one or more probabilities are invalid, or if meanStay < 0 or sdStay < 0
	 */
	public Simulator(int seed,double meanStay, double sdStay,
			double carProb, double smallCarProb, double mcProb) throws SimulationException {
		checkProbabilties(carProb, smallCarProb, mcProb);
		if ((meanStay <0) || (sdStay <0)) {
			throw new SimulationException(" Invalid mean or standard deviation");
		}

		this.meanStay = meanStay;
		this.sdStay = sdStay;
		this.seed = seed;
		this.carProb = carProb;
		this.smallCarProb = smallCarProb;
		this.mcProb = mcProb;
		this.rng = new Random(this.seed);
	}
	
	/**
	 * Coin toss to see whether motorCycle is created.
	 * Uses recorded mc probability
	 * @return true if trial is successful, false otherwise
	 */
	public boolean motorCycleTrial() {
		return randomSuccess(this.mcProb);
	}

		
	/**
	 * Coin toss to see whether car is created.
	 * Uses recorded car existence probability
	 * @return true if trial is successful, false otherwise
	 */
	public boolean newCarTrial() {
		return randomSuccess(this.carProb);
	}
	
	/**
	 * Method to set intended stay according to Gaussian RNG
	 * @return random duration drawn from N(meanStay,meanStay/3) or MINIMUM_STAY, whichever is greater
	 */
	public int setDuration() {
		//z ~ N(0,1) so transform 
		double z = this.rng.nextGaussian(); 
		double x = z*this.sdStay + this.meanStay;
		int duration = ((int) x);
		return Math.max(duration,Constants.MINIMUM_STAY);
	}
	
	/**
	 * Coin toss to see whether car is to be small car
	 * Uses recorded small car probability
	 * @return true if trial is successful, false otherwise
	 */
	public boolean smallCarTrial() {
		return randomSuccess(this.smallCarProb);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Simulator [meanStay=" + meanStay + ", sdStay=" + sdStay
				+ ", seed=" + seed + ", carProb=" + carProb + ", smallCarProb="
				+ smallCarProb + ", mcProb=" + mcProb + "]";
	}
	
	/**
	 * Helper to check all probabilities are valid
	 * @param carProb double holding the probability that a car will arrive in the current minute
	 * @param smallCarProb double holding the probability that the car that has arrived is a small car
	 * @param mcProb double holding the probability that a motorcycle will arrive in the current minute
	 * @throws SimulationException if one or more probabilities are invalid
	 */
	private void checkProbabilties(double carProb, double smallCarProb,
			double mcProb) throws SimulationException {
		String msg = "";
		boolean throwExcept = false; 
		if (invalidProbability(carProb)) {
			msg += " carProb ";
			throwExcept = true;
		} 
		if (invalidProbability(smallCarProb)) {
			msg += " smallCarProb ";
			throwExcept = true;
		}
		if (invalidProbability(mcProb)) {
			msg += " mcProb ";
			throwExcept = true;
		}
		if (throwExcept) {
			throw new SimulationException(msg + " must lie in [0,1]");
		}
	}
	
	/**
	 * Helper method to ensure valid probability 
	 * @param prob double holding probability 
	 * @return true if valid, false if prob > 1.0  or prob < 0.0
	 */
	private boolean invalidProbability(double prob) {
		return (prob < 0.0) || (prob > 1.0);
	}

	/**
	 * Utility method to implement a <a href="http://en.wikipedia.org/wiki/Bernoulli_trial">Bernoulli Trial</a>, 
	 * a coin toss with two outcomes: success (probability successProb) and failure (probability 1-successProb)
	 * @param successProb double holding the success probability 
	 * @return true if trial was successful, false otherwise
	 */
	private boolean randomSuccess(double successProb) {
		boolean result = rng.nextDouble() <= successProb;
		return result;
	}

}
