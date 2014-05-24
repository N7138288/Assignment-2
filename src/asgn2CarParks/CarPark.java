/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2CarParks 
 * 21/04/2014
 * 
 */
package asgn2CarParks;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;
import asgn2Vehicles.Vehicle;

/**
 * The CarPark class provides a range of facilities for working with a car park in support 
 * of the simulator. In particular, it maintains a collection of currently parked vehicles, 
 * a queue of vehicles wishing to enter the car park, and an historical list of vehicles which 
 * have left or were never able to gain entry. 
 * 
 * The class maintains a wide variety of constraints on small cars, normal cars and motorcycles 
 * and their access to the car park. See the method javadoc for details. 
 * 
 * The class relies heavily on the asgn2.Vehicle hierarchy, and provides a series of reports 
 * used by the logger. 
 * 
 * @author hogan
 *
 */
public class CarPark 
{
	//Parameter Setup: Constructor Parameters
	private int maxCarSpaces; //The number of car park spots in the car park.
	private int maxSmallCarSpaces; //The number of small car park spots in the car park.
	private int maxMotorCycleSpaces; //The number of motorcycle park spots in the car park.
	private int maxQueueSize; //The maximum amount of vehicles allowable in the queue.
	
	//Parameter Setup: Counting Parameters
	private int count; //Total number of vehicles to have entered the system ever.
	private int numDissatisfied; //The number of vehicles to have been: turned away because queue is full OR turned away because waited in queue too long.
	private int numQueue = 0; //The number of vehicles currently in the queue.
	private int normalSpots = 0; //The number of vehicles currently parked in normal car spots.
	private int smallSpots = 0; //The number of vehicles currently parked in small car spots.
	private int motorCycleSpots = 0; //The number of vehicles currently parked in motorcycle spots.
	private int numCars = 0; //The number of small or normal cars currently parked.
	private int numMotorCycles = 0; //The number of small cars currently parked.
	private int numSmallCars = 0; //The number of motorcycles currently parked.
	
	//Parameter Setup: Lists to store the queue, etc.
	private List<Vehicle> queue = new ArrayList<Vehicle>(); //Stores a list of vehicles currently in the queue.
	private List<Vehicle> spaces = new ArrayList<Vehicle>(); //Stores a list of vehicles currently parked.
	private List<String> typeSpaces = new ArrayList<String>();//Stores a list of strings ("S", "N", "M") that represents the type of car park in use for a parked vehicle.
	private List<Vehicle> past = new ArrayList<Vehicle>(); //Stores a list of vehicles currently archived.

	//Other Parameters:
	private String status;
	
	/**
	 * CarPark constructor sets the basic size parameters. 
	 * Uses default parameters
	 */
	public CarPark() {
		this(Constants.DEFAULT_MAX_CAR_SPACES,Constants.DEFAULT_MAX_SMALL_CAR_SPACES,
				Constants.DEFAULT_MAX_MOTORCYCLE_SPACES,Constants.DEFAULT_MAX_QUEUE_SIZE);
	}
	
	/**
	 * CarPark constructor sets the basic size parameters. 
	 * @param maxCarSpaces maximum number of spaces allocated to cars in the car park 
	 * @param maxSmallCarSpaces maximum number of spaces (a component of maxCarSpaces) 
	 * 						 restricted to small cars
	 * @param maxMotorCycleSpaces maximum number of spaces allocated to MotorCycles
	 * @param maxQueueSize maximum number of vehicles allowed to queue
	 */
	public CarPark(int maxCarSpaces,int maxSmallCarSpaces, int maxMotorCycleSpaces, int maxQueueSize) {
		this.maxCarSpaces = maxCarSpaces;
		this.maxMotorCycleSpaces = maxMotorCycleSpaces;
		this.maxQueueSize = maxQueueSize;
		this.maxSmallCarSpaces = maxSmallCarSpaces;
	}

	/**
	 * Archives vehicles exiting the car park after a successful stay. Includes transition via 
	 * Vehicle.exitParkedState(). 
	 * @param time int holding time at which vehicle leaves
	 * @param force boolean forcing departure to clear car park 
	 * @throws VehicleException if vehicle to be archived is not in the correct state 
	 * @throws SimulationException if one or more departing vehicles are not in the car park when operation applied
	 */
	public void archiveDepartingVehicles(int time,boolean force) throws VehicleException, SimulationException 
	{
		for (int index = 0; index != spaces.size(); index++)
		{
			//If forced to leave car park, or if departure time is less or equal to the current time:
			if ((force) || (spaces.get(index).getDepartureTime() <= time))
			{
				spaces.get(index).exitQueuedState(time); //Vehicle leaves the parked state.
				past.add(spaces.get(index)); //Vehicle is added to the list of archives.
				spaces.remove(index); //Vehicle is removed from the list of cars parked.
			}
		}
	}
		
	/**
	 * Method to archive new vehicles that don't get parked or queued and are turned 
	 * away
	 * @param v Vehicle to be archived
	 * @throws SimulationException if vehicle is currently queued or parked
	 */
	public void archiveNewVehicle(Vehicle v) throws SimulationException 
	{
		if (v.isParked()) //If vehicle is currently parked: throw an exception.
		{
			throw new SimulationException("Simulation Exception: Vehicle is currently parked. This function is for vehicles that don't get parked or queued. Vehicle is archived.");
		}
		if (v.isQueued()) //If vehicle is currently queued: throw an exception.
		{
			throw new SimulationException("Simulation Exception: Vehicle is currently queued. This function is for vehicles that don't get parked or queued. Vehicle is archived.");
		}
		past.add(v); //Vehicle is added to the list of archived.
		count += 1; //The number of vehicles processed is incremented.
		numDissatisfied += 1; //Number of Dissatisfied vehicles is incremented.
	}
	
	/**
	 * Archive vehicles which have stayed in the queue too long 
	 * @param time int holding current simulation time 
	 * @throws VehicleException if one or more vehicles not in the correct state or if timing constraints are violated
	 */
	public void archiveQueueFailures(int time) throws VehicleException 
	{
		//With no vehicles being inputted here, I'm guessing this is the solution?
		for (int index = 0; index != queue.size(); index++) //For vehicle currently in the queue.
		{
			//If the vehicle has been in the queue too long.
			if (time - queue.get(index).getArrivalTime() >= asgn2Simulators.Constants.MAXIMUM_QUEUE_TIME) 
			{
				queue.get(index).exitQueuedState(time); //Vehicle leaves the queue state.
				past.add(queue.get(index)); //Vehicle is added to the list of archives
				queue.remove(index); //Vehicle is removed from the list of cars queued.
			}
		}
	}
	
	/**
	 * Simple status showing whether carPark is empty
	 * @return true if car park empty, false otherwise
	 */
	public boolean carParkEmpty() 
	{
		return spaces.isEmpty(); //return if the list is empty.
	}
	
	/**
	 * Simple status showing whether carPark is full
	 * @return true if car park full, false otherwise
	 */
	public boolean carParkFull() {
		return (spaces.size() >= maxSmallCarSpaces); //return if the list's size is greater or equal to the max amount of spaces.
	}
	
	/**
	 * Method to add vehicle successfully to the queue
	 * Precondition is a test that spaces are available
	 * Includes transition through Vehicle.enterQueuedState 
	 * @param v Vehicle to be added 
	 * @throws SimulationException if queue is full  
	 * @throws VehicleException if vehicle not in the correct state 
	 */
	public void enterQueue(Vehicle v) throws SimulationException, VehicleException {
		if (queueFull()) //If the queue is full: throw an exception.
		{
			throw new SimulationException("Simuation Exception: Queue is full: Will not add vehicle to queue.");
		}
		else //Otherwise:
		{
			v.enterQueuedState(); //Vehicle enters the queued state.
			queue.add(v); //Add Vehicle to the end of the queue.
			numQueue += 1; //The number of vehicles in the queue is incremented.
			count += 1; //The number of vehicles process is incremented.
		}
	}
	
	/**
	 * Method to remove vehicle from the queue after which it will be parked or 
	 * removed altogether. Includes transition through Vehicle.exitQueuedState.  
	 * @param v Vehicle to be removed from the queue 
	 * @param exitTime int time at which vehicle exits queue
	 * @throws SimulationException if vehicle is not in queue 
	 * @throws VehicleException if the vehicle is in an incorrect state or timing 
	 * constraints are violated
	 */
	//In a post to fb I think Jim said that we only can park the vehicle at the front of the queue. IE
	//we can't loop through the queue to see if we can park one of the cars
	//What do you think?
	
	//This would cover trying to park only the first vehicle anywhos - Jared
	//Yeah I changed my idea sorry haha, I think we still might need to take cars out of the queuue,
	//if they stay to long and we need to arvhive them.
	public void exitQueue(Vehicle v,int exitTime) throws SimulationException, VehicleException 
	{
		if (!v.isQueued()) //If vehicle is not queued: throw an exception.
		{
			throw new SimulationException("Simuation Exception: Vehicle is not in a queue.");
		}
		else //Otherwise:
		{
			for (int loop = 0; loop != maxQueueSize; loop++) //For each vehicle in the queue.
			{
				if (queue.get(loop) == v) //If the vehicle trying to leave is found in the queue.
				{
					v.exitQueuedState(exitTime); //Vehicle exits the queued state.
					queue.remove(loop); //Vehicle is removed from the list of queued vehicles.
					loop = this.maxQueueSize-1; //Change the loop value to break the for loop.
					numQueue -= 1; //The number of vehicles in queue is decremented.
				}
			}
		}			
	}
	
	/**
	 * State dump intended for use in logging the final state of the carpark
	 * All spaces and queue positions should be empty and so we dump the archive
	 * @return String containing dump of final carpark state 
	 */
	public String finalState() {
		String str = "Vehicles Processed: count:" + 
				this.count + ", logged: " + this.past.size() 
				+ "\nVehicle Record: \n";
		for (Vehicle v : this.past) {
			str += v.toString() + "\n\n";
		}
		return str + "\n";
	}
	
	/**
	 * Simple getter for number of cars in the car park 
	 * @return number of cars in car park, including small cars
	 */
	public int getNumCars() {
		return numCars;
	}
	
	/**
	 * Simple getter for number of motorcycles in the car park 
	 * @return number of MotorCycles in car park, including those occupying 
	 * 			a small car space
	 */
	public int getNumMotorCycles() {
		return numMotorCycles;
	}
	
	/**
	 * Simple getter for number of small cars in the car park 
	 * @return number of small cars in car park, including those 
	 * 		   not occupying a small car space. 
	 */
	public int getNumSmallCars() {
		return numSmallCars;
	}
	
	/**
	 * Method used to provide the current status of the car park. 
	 * Uses private status String set whenever a transition occurs. 
	 * Example follows (using high probability for car creation). At time 262, 
	 * we have 276 vehicles existing, 91 in car park (P), 84 cars in car park (C), 
	 * of which 14 are small (S), 7 MotorCycles in car park (M), 48 dissatisfied (D),
	 * 176 archived (A), queue of size 9 (CCCCCCCCC), and on this iteration we have 
	 * seen: car C go from Parked (P) to Archived (A), C go from queued (Q) to Parked (P),
	 * and small car S arrive (new N) and go straight into the car park<br>
	 * 262::276::P:91::C:84::S:14::M:7::D:48::A:176::Q:9CCCCCCCCC|C:P>A||C:Q>P||S:N>P|
	 * @return String containing current state 
	 */
	public String getStatus(int time) {
		String str = time +"::"
		+ this.count + "::" 
		+ "P:" + this.spaces.size() + "::"
		+ "C:" + this.numCars + "::S:" + this.numSmallCars 
		+ "::M:" + this.numMotorCycles 
		+ "::D:" + this.numDissatisfied 
		+ "::A:" + this.past.size()  
		+ "::Q:" + this.queue.size(); 
		for (Vehicle v : this.queue) {
			if (v instanceof Car) {
				if (((Car)v).isSmall()) {
					str += "S";
				} else {
					str += "C";
				}
			} else {
				str += "M";
			}
		}
		str += this.status;
		this.status="";
		return str+"\n";
	}

	/**
	 * State dump intended for use in logging the initial state of the carpark.
	 * Mainly concerned with parameters. 
	 * @return String containing dump of initial carpark state 
	 */
	public String initialState() {
		return "CarPark [maxCarSpaces: " + this.maxCarSpaces
				+ " maxSmallCarSpaces: " + this.maxSmallCarSpaces 
				+ " maxMotorCycleSpaces: " + this.maxMotorCycleSpaces 
				+ " maxQueueSize: " + this.maxQueueSize + "]";
	}

	/**
	 * Simple status showing number of vehicles in the queue 
	 * @return number of vehicles in the queue
	 */
	public int numVehiclesInQueue() {
		return this.numQueue;
	}
	
	
	//Update: Motor cycle cannot park in a normal car park spot, only a motor cycle spot or small car spot. - Jared
	/**
	 * Method to add vehicle successfully to the car park store. 
	 * Precondition is a test that spaces are available. 
	 * Includes transition via Vehicle.enterParkedState.
	 * @param v Vehicle to be added 
	 * @param time int holding current simulation time
	 * @param intendedDuration int holding intended duration of stay 
	 * @throws SimulationException if no suitable spaces are available for parking 
	 * @throws VehicleException if vehicle not in the correct state or timing constraints are violated
	 */
	public void parkVehicle(Vehicle v, int time, int intendedDuration) throws SimulationException, VehicleException {
		if (v.getClass() == asgn2Vehicles.MotorCycle.class) //If the vehicle is a motor cycle:
		{
			if (motorCycleSpots == maxMotorCycleSpaces) //If there are no motor cycle spots remaining:
			{
				if (smallSpots == maxSmallCarSpaces) //If there are no small car spots remaining: throw an exception.
				{
					throw new SimulationException("Simulation Exception: There are no car parks available for a motor cycle.");
				}
				else //If there are small car park spots remaining: add motor cycle to small car park spots.
				{
					spaces.add(v); //Add motor cycle to list of parked vehicles.
					typeSpaces.add("S"); //Add small spot to list of used spots.
					motorCycleSpots += 1; //Increment number of motor cycles spots used.
					numMotorCycles += 1; //Increment number of motor cycles parked.
				}
			}
			else //If there are motor cycle spots remaining: add motor cycle to motor cycle spots.
			{
				spaces.add(v); //Add motor cycle to list of parked vehicles.
				typeSpaces.add("M"); //Add motor cycle spot to list of used spots.
				motorCycleSpots += 1; //Increment number of motor cycles spots used.
				numMotorCycles += 1; //Increment number of motor cycles parked.
			}
		}
		else if (((Car) v).isSmall()) //If the vehicle is a small car:
		{
			if (smallSpots == maxSmallCarSpaces) //If there are no small car park spots remaining:
			{
				if (normalSpots == maxCarSpaces) //If there are no normal car park spots remaining: throw an exception.
				{
					throw new SimulationException("Simulation Exception: There are no car parks available for a small car.");
				}
				else //If there are normal car park spots remaining: add small car to normal car park spots.
				{
					spaces.add(v); //Add motor cycle to list of parked vehicles.
					typeSpaces.add("N"); //Add normal car spot to list of used spots.
					normalSpots += 1; //Increment number of normal spots used.
					numSmallCars += 1; //Increment number of small cars parked.
					numCars += 1; //Increment number of cars parked.
				}
			}
			else //If there are small car spots remaining: add small car to small car spots.
			{
				spaces.add(v); //Add small car to list of parked vehicles.
				typeSpaces.add("S"); //Add small car spot to list of used spots.
				smallSpots += 1; //Increment number of small car spots used.
				numSmallCars += 1; //Increment number of small cars parked.
				numCars += 1; //Increment number of cars parked.
			}
		}
		else //else: this vehicle is a normal car:
		{
			if (normalSpots == maxCarSpaces) //If there are no normal car park spots remaining: throw an exception.
			{
				throw new SimulationException("Simulation Exception: There are no car parks available for a normal car.");
			}
			else //If there are normal car park spots remaining: add normal car to normal car park spots.
			{
				spaces.add(v); //Add normal car to list of parked vehicles.
				typeSpaces.add("N"); //Add normal car spot to list of used spots.
				normalSpots += 1; //Increment number of normal cars spots used.
				numCars += 1; //Increment number of cars parked.
			}
		}
	}
	
	/**
	 * Silently process elements in the queue, whether empty or not. If possible, add them to the car park. 
	 * Includes transition via exitQueuedState where appropriate
	 * Block when we reach the first element that can't be parked. 
	 * @param time int holding current simulation time 
	 * @throws SimulationException if no suitable spaces available when parking attempted
	 * @throws VehicleException if state is incorrect, or timing constraints are violated
	 */
	public void processQueue(int time, Simulator sim) throws VehicleException, SimulationException {
		//No vehicles that have been in the queue too long are in this function
		boolean block = false;
		
		while (block == false)
		{
			if (queue.isEmpty())
			{
				block = true;
			}
			else if (spacesAvailable(queue.get(0)) == false)
			{
				block = true;
			}
			else
			{
				exitQueue(queue.get(0), time);
			}
		}
	}

	/**
	 * Simple status showing whether queue is empty
	 * @return true if queue empty, false otherwise
	 */
	public boolean queueEmpty() {
		return queue.isEmpty();
	}

	/**
	 * Simple status showing whether queue is full
	 * @return true if queue full, false otherwise
	 */
	public boolean queueFull() {
		return (this.numQueue == this.maxQueueSize);
	}
	
	/**
	 * Method determines, given a vehicle of a particular type, whether there are spaces available for that 
	 * type in the car park under the parking policy in the class header.  
	 * @param v Vehicle to be stored. 
	 * @return true if space available for v, false otherwise 
	 */
	public boolean spacesAvailable(Vehicle v) 
	{
		if (v.getClass() == asgn2Vehicles.MotorCycle.class) //If the vehicle is a motorcycle:
		{
			return (!(motorCycleSpots + smallSpots == maxSmallCarSpaces + maxMotorCycleSpaces));
		}
		else if (((Car) v).isSmall()) //If the vehicle is a small car:
		{
			return (!(smallSpots + normalSpots == maxCarSpaces - maxMotorCycleSpaces));
		}
		else //If the vehicle is a normal car:
		{
			return (!(normalSpots == maxCarSpaces - maxMotorCycleSpaces - maxSmallCarSpaces));
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return null;
	}

	/**
	 * Method to try to create new vehicles (one trial per vehicle type per time point) 
	 * and to then try to park or queue (or archive) any vehicles that are created 
	 * @param sim Simulation object controlling vehicle creation 
	 * @throws SimulationException if no suitable spaces available when operation attempted 
	 * @throws VehicleException if vehicle creation violates constraints 
	 */
	public void tryProcessNewVehicles(int time,Simulator sim) throws VehicleException, SimulationException {
		
	}

	/**
	 * Method to remove vehicle from the carpark. 
	 * For symmetry with parkVehicle, include transition via Vehicle.exitParkedState.  
	 * So vehicle should be in parked state prior to entry to this method. 
	 * @param v Vehicle to be removed from the car park 
	 * @throws VehicleException if Vehicle is not parked, is in a queue, or violates timing constraints 
	 * @throws SimulationException if vehicle is not in car park
	 */
	public void unparkVehicle(Vehicle v,int departureTime) throws VehicleException, SimulationException {
		boolean inPark = false; //Flag to say vehicle was found in car park.
		for (int index = 0; index != spaces.size(); index++) //For vehicle in car park:
		{
			if (spaces.get(index).getVehID() == v.getVehID()) //If parked vehicle is vehicle to remove from car park:
			{
				inPark = true; //Set found flag.
				v.exitParkedState(departureTime); //Vehicle is removed from the list of vehicles parked.
			}
		}
		if (!inPark) //If vehicle not found in car park: throw an exception.
		{
			throw new SimulationException("Simulation Exception: Vehicle not found in car park. Vehicle not removed from car park.");
		}
	}
	
	/**
	 * Helper to set vehicle message for transitions 
	 * @param v Vehicle making a transition (uses S,C,M)
	 * @param source String holding starting state of vehicle (N,Q,P,A) 
	 * @param target String holding finishing state of vehicle (Q,P,A) 
	 * @return String containing transition in the form: |(S|C|M):(N|Q|P|A)>(Q|P|A)| 
	 */
	private String setVehicleMsg(Vehicle v,String source, String target) {
		String str="";
		if (v instanceof Car) {
			if (((Car)v).isSmall()) {
				str+="S";
			} else {
				str+="C";
			}
		} else {
			str += "M";
		}
		return "|"+str+":"+source+">"+target+"|";
	}
}
