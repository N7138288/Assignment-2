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
 * This class represents exceptions associated with the misuse of the Vehicle hierarchy. 
 * @author hogan 
 */
@SuppressWarnings("serial") // We're not interested in binary i/o here
public class VehicleException extends Exception {

	/**
	 * Creates a new instance of VehicleException.
	 * 
	 * @param message String holding an informative message about the problem encountered
	 */
	public VehicleException(String message) {
		super("Vehicle Exception: " + message);
	}
}
