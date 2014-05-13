/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Exceptions 
 * 19/04/2014
 * 
 */
package asgn2Exceptions;

/**
 * This class represents exceptions generated during the
 * simulation, from classes which utilise the CarPark and Vehicle hierarchy.
 * @author hogan 
 */
@SuppressWarnings("serial") // We're not interested in binary i/o here
public class SimulationException extends Exception {
	
	/**
	 * Creates a new instance of SimulationException.
	 * 
	 * @param message String holding an informative message about the problem encountered
	 */
	public SimulationException(String message) {
		super("Simulation Exception: " + message);
	}
}
