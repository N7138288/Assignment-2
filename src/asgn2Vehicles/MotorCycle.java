/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Vehicles 
 * 20/04/2014
 * 
 */
package asgn2Vehicles;
import asgn2Exceptions.VehicleException;

/**
 * The MotorCycle class is a 'specialisation' of the Vehicle class to cater for motorcycles
 * This version uses only those facilities specified in the Vehicle class 
 * 
 * @author hogan
 *
 */
public class MotorCycle extends Vehicle {
	/**
	 * MotorCycle constructor 
	 * @param vehID - identification number or plate of the vehicle
	 * @param arrivalTime - time (minutes) at which the vehicle arrives and is 
	 *        either queued or given entry to the carpark 
	 * @throws VehicleException if arrivalTime is <= 0  
	 */
	
	public MotorCycle(String vehID, int arrivalTime) throws VehicleException 
	{
		if(arrivalTime <= 0) //If Arrival time is not strictly positive: throw an exception.
		{
			throw new VehicleException("Vehicle Exception: Arrival Time is not strictly positive. Motor Cycle was not created.");
		}
		else //Assign Vehicle parameters
		{
			this.vehID = vehID;
			this.arrivalTime = arrivalTime;
		}
	}
	/* (non-Javadoc)
	 * @see asgn2Vehicles.Vehicle#toString()
	 */
	@Override
	public String toString() 
	{
		return "Vehicle: Motor Cycle | vehID: " + this.vehID + " | Arrival Time: "
				+ this.getArrivalTime() + " | Parking Time: " 
				+ this.getParkingTime()	+ " | Departure Time: " + this.getDepartureTime()
				+ " | Satisfied: " + this.isSatisfied();
	}
}
