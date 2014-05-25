/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Vehicles 
 * 19/04/2014
 * 
 */
package asgn2Vehicles;

import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;

/**
 * Vehicle is an abstract class specifying the basic state of a vehicle and the methods used to set and access that
 * state. A vehicle is created upon arrival, at which point it must either enter the car park to take a vacant space or
 * become part of the queue. If the queue is full, then the vehicle must leave and never enters the car park. The
 * vehicle cannot be both parked and queued at once and both the constructor and the parking and queuing state
 * transition methods must respect this constraint.
 * 
 * Vehicles are created in a neutral state. If the vehicle is unable to park or queue, then no changes are needed if the
 * vehicle leaves the carpark immediately. Vehicles that remain and can't park enter a queued state via
 * {@link #enterQueuedState() enterQueuedState} and leave the queued state via {@link #exitQueuedState(int)
 * exitQueuedState}. Note that an exception is thrown if an attempt is made to join a queue when the vehicle is already
 * in the queued state, or to leave a queue when it is not.
 * 
 * Vehicles are parked using the {@link #enterParkedState(int, int) enterParkedState} method and depart using
 * {@link #exitParkedState(int) exitParkedState}
 * 
 * Note again that exceptions are thrown if the state is inappropriate: vehicles cannot be parked or exit the car park
 * from a queued state.
 * 
 * The method javadoc below indicates the constraints on the time and other parameters. Other time parameters may vary
 * from simulation to simulation and so are not constrained here.
 * 
 * @author hogan
 * 
 */
public abstract class Vehicle {
	protected String vehID; // String representation of a vehicle's ID. Should be, but not enforced, unique.

	// TIMES:
	protected int arrivalTime; // The time the vehicle is created. The vehicle is immediately queued or parked uopon
								// creation.
	private int exitTime; // The time the vehicle exits the queue. This is greater or equal to the arrivalTime, incase
							// it goes from queue to park immediately.
	private int parkingTime; // The time the vehicle enters a park. This is greater then or equal the the exit time.
								// According to specs, this has to be just greater then
	// or equal to zero.
	private int departureTime; // The time the vehicle leaves a park. This is equal to the time it arrives in a park +
								// the time it intends to stay. This has a forced maximum
	// of midnight, when the car park closes.

	// BOOLEANS:
	private boolean parked; // The parked state of the vehicle.
	private boolean everParked = false; // Has the vehicle ever parked before.
	private boolean queued; // The queued state of the vehicle.
	private boolean everQueued = false; // Has the vehicle ever queued before.

	/**
	 * Vehicle Constructor
	 * 
	 * @param vehID
	 *            String identification number or plate of the vehicle
	 * @param arrivalTime
	 *            int time (minutes) at which the vehicle arrives and is either queued, given entry to the car park or
	 *            forced to leave
	 * @throws VehicleException
	 *             if arrivalTime is <= 0
	 */
	public Vehicle(String vehID, int arrivalTime) throws VehicleException {
		if (arrivalTime <= 0) // If Arrival Time is not strictly positive: throw an exception.
		{
			throw new VehicleException(
					"Vehicle Exception: Arrival Time is not strictly positive. Vehicle was not created.");
		} else // Set Vehicle Parameters
		{
			this.vehID = vehID;
			this.arrivalTime = arrivalTime;
		}
	}

	// This stops the errors on the constructors for Car, Motor Cycle
	public Vehicle() {
	}

	/**
	 * Transition vehicle to parked state (mutator) Parking starts on arrival or on exit from the queue, but time is set
	 * here
	 * 
	 * @param parkingTime
	 *            int time (minutes) at which the vehicle was able to park
	 * @param intendedDuration
	 *            int time (minutes) for which the vehicle is intended to remain in the car park. Note that the
	 *            parkingTime + intendedDuration yields the departureTime
	 * @throws VehicleException
	 *             if the vehicle is already in a parked or queued state, if parkingTime < 0, or if intendedDuration is
	 *             less than the minimum prescribed in asgnSimulators.Constants
	 */

	// There is an error with this, it is not setting the car's state to parked
	// Can't figure it out/am too tired

	// Will check it in a sec - Jared
	public void enterParkedState(int parkingTime, int intendedDuration) throws VehicleException {
		boolean correctState = true; // Boolean used to determine if vehicle will exit queued state despite exceptions.
		if ((parked) || (queued)) // Check to see if in incorrect state: parked or false.
		{
			// Throw exception and change state if so
			correctState = false;
			throw new VehicleException(
					"Not in Correct state (required state: parked = queued = false). Vehicle has not entered parked state.");
		}
		if (intendedDuration < Constants.MINIMUM_STAY) // If intendedDuration is less then the minimum stay.
		{
			// Throw exception and change state
			correctState = false;
			throw new VehicleException(
					"Vehicle Exception: Intended Duration less then the minimum stay. Vehicle has not entered parked state.");
		}
		if (parkingTime < 0) // If parking time is strictly negative.
		{
			// Throw exception and change state
			correctState = false;
			throw new VehicleException(
					"Vehicle Exception: Parking Time less then zero. Vehicle has not entered parked state.");
		}
		if (correctState) // If in the correct state:
		{
			if (!everParked) // Set the fact the vehicle is parked before.
			{
				everParked = true;
			}
			// Set parking parameters.
			parked = true;
			this.parkingTime = parkingTime;
			departureTime = parkingTime + intendedDuration;
		}
	}

	/**
	 * Transition vehicle to queued state (mutator) Queuing formally starts on arrival and ceases with a call to
	 * {@link #exitQueuedState(int) exitQueuedState}
	 * 
	 * @throws VehicleException
	 *             if the vehicle is already in a queued or parked state
	 */
	public void enterQueuedState() throws VehicleException {
		if ((parked) || (queued)) // If the vehicle is not in the correct state: throw an exception.
		{
			throw new VehicleException(
					"Vehicle Exception: Not in Correct state (required state: parked = queued = false). Vehicle has "
					+ "not entered queued state.");
		} else {
			if (!everQueued) // Set that the vehicle has queued before.
			{
				everQueued = true;
			}
			queued = true; // Set queued parameters.
		}
	}

	/**
	 * Transition vehicle from parked state (mutator)
	 * 
	 * @param departureTime
	 *            int holding the actual departure time
	 * @throws VehicleException
	 *             if the vehicle is not in a parked state, is in a queued state or if the revised departureTime <
	 *             parkingTime
	 */
	public void exitParkedState(int departureTime) throws VehicleException {
		boolean correctState = true; // Boolean used to determine if vehicle will exit queued state despite exceptions.
		if (queued) // If the vehicle is in a queued state.
		{
			// Throw exception and change state
			correctState = false;
			throw new VehicleException(
					"Vehicle Exception: Not in Correct state (currently queued). Vehicle has not exited the parked state.");
		}
		if (parked == false) // If the vehicle is not in a parked state.
		{
			// Throw exception and change state
			correctState = false;
			throw new VehicleException(
					"Vehicle Exception: Not in Correct state (currently not parked). Vehicle has not exited the parked state.");
		}
		if (departureTime < parkingTime) // If the time leaving the car park is before the time entering the car park:
											// throw an exception
		{
			throw new VehicleException(
					"Vehicle Exception: The departure time is before the time the vehicle parked. Vehicle has exited "
					+ "the parked state.");
		}
		if (correctState) // If in the correct state.
		{
			// Set parked parameters
			parked = false;
			this.departureTime = departureTime;
		}
	}

	/**
	 * Transition vehicle from queued state (mutator) Queuing formally starts on arrival with a call to
	 * {@link #enterQueuedState() enterQueuedState} Here we exit and set the time at which the vehicle left the queue
	 * 
	 * @param exitTime
	 *            int holding the time at which the vehicle left the queue
	 * @throws VehicleException
	 *             if the vehicle is in a parked state or not in a queued state, or if exitTime is not later than
	 *             arrivalTime for this vehicle
	 */
	public void exitQueuedState(int exitTime) throws VehicleException {
		boolean correctState = true; // Boolean used to determine if vehicle will exit queued state despite exceptions.
		if (parked) // If the vehicle is in a parked state.
		{
			// Throw exception and change state
			correctState = false;
			throw new VehicleException(
					"Vehicle Exception: Not in Correct state (currently parked). Vehicle has not exited the queued state.");
		}
		if (!queued) // If the vehicle is in a queued state.
		{
			// Throw exception and change state
			correctState = false;
			throw new VehicleException(
					"Vehicle Exception: Not in Correct state (currently not queued). Vehicle has not exited the queued state.");
		}
		if (exitTime < arrivalTime) // If the time leaving the queue is before the time entering the queue: 
									//throw an exception.
		{
			throw new VehicleException(
					"Vehicle Exception: The exit time is strictly before the time the vehicle arrived. Vehicle has "
					+ "exited the parked state.");
		}
		if (correctState) // if in the correct state to leave the queue.
		{
			//Set queued parameters
			queued = false;
			this.exitTime = exitTime;
		}
	}

	/**
	 * Simple getter for the arrival time
	 * 
	 * @return the arrivalTime
	 */
	public int getArrivalTime() {
		return arrivalTime;
	}

	// I made this, not part of spec
	/**Simple getter for the exit time of a vehicle
	 * 
	 * @return the exitTime
	 */
	public int getExitTime() {
		return exitTime;
	}

	/**
	 * Simple getter for the departure time from the car park Note: result may be 0 before parking, show intended
	 * departure time while parked; and actual when archived
	 * 
	 * @return the departureTime
	 */
	public int getDepartureTime() {
		return departureTime;
	}

	/**
	 * Simple getter for the parking time Note: result may be 0 before parking
	 * 
	 * @return the parkingTime
	 */
	public int getParkingTime() {
		return parkingTime;
	}

	/**
	 * Simple getter for the vehicle ID
	 * 
	 * @return the vehID
	 */
	public String getVehID() {
		return vehID;
	}

	/**
	 * Boolean status indicating whether vehicle is currently parked
	 * 
	 * @return true if the vehicle is in a parked state; false otherwise
	 */
	public boolean isParked() {
		return parked;
	}

	/**
	 * Boolean status indicating whether vehicle is currently queued
	 * 
	 * @return true if vehicle is in a queued state, false otherwise
	 */
	public boolean isQueued() {
		return queued;
	}

	/**
	 * Boolean status indicating whether customer is satisfied or not Satisfied if they park; dissatisfied if turned
	 * away, or queuing for too long Note that calls to this method may not reflect final status
	 * 
	 * @return true if satisfied, false if never in parked state or if queuing time exceeds max allowable
	 */
	public boolean isSatisfied() {
		if (exitTime - arrivalTime >= asgn2Simulators.Constants.MAXIMUM_QUEUE_TIME) // If the car was in queue too long:
																					// return false:
		{
			return false;
		} else if (!everParked) // If the car never parked: return false:
		{
			return false;
		} else // Otherwise: return true
		{
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return null;
	}

	/**
	 * Boolean status indicating whether vehicle was ever parked Will return false for vehicles in queue or turned away
	 * 
	 * @return true if vehicle was or is in a parked state, false otherwise
	 */
	public boolean wasParked() {
		return everParked;
	}

	/**
	 * Boolean status indicating whether vehicle was ever queued
	 * 
	 * @return true if vehicle was or is in a queued state, false otherwise
	 */
	public boolean wasQueued() {
		return everQueued;
	}

}
