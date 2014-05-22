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
public class CarPark {
	
	//Parameter Setup
	private int maxCarSpaces; //Mandatory
	private int maxSmallCarSpaces; //Mandatory
	private int maxMotorCycleSpaces; //Mandatory
	private int maxQueueSize; //Mandatory
	
	private int count; //Mandatory
	

	private int numDissatisfied; //Mandatory


	private String status; //Mandatory
	
	
	//Current Queue
	private List<Vehicle> queue = new ArrayList<Vehicle>();
	private int numQueue = 0; //Mandatory
	
	//Spaces in the carpark: smallSpots + motorCycleSpots + NormalSpots = spaces.size();
	private List<Vehicle> spaces = new ArrayList<Vehicle>(); //Mandatory
	//Value for cars in small car parks
	private int smallSpots;
	//Value for cars in motorCycleSpots;
	private int motorCycleSpots;
	//Value for cars in normal spots;
	private int normalSpots;
	
	private List<String> typeSpaces;
	
	private int numCars; //Mandatory
	private int numMotorCycles; //Mandatory
	private int numSmallCars; //Mandatory
	
	//Archive:
	private List<Vehicle> past = new ArrayList<Vehicle>();
	private int numArchives;
	
	
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
			if ((force) || (spaces.get(index).getDepartureTime() <= time))
			{
				//Vehicle leaves parked state
				spaces.get(index).exitQueuedState(time);
				
				//Vehicle added to archives
				past.add(spaces.get(index));
				
				//Vehicle removed from park
				spaces.remove(index);
				
				//Archives incremented
				numArchives += 1;
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
		//If vehicle is currently parked
		if (v.isParked())
		{
			//Throw exception
			throw new SimulationException("Simulation Exception: Vehicle is currently parked. This function is for vehicles that don't get parked or queued. Vehicle is archived.");
		}
		//If vehicle is currently queued
		if (v.isQueued())
		{
			//Throw exception
			throw new SimulationException("Simulation Exception: Vehicle is currently queued. This function is for vehicles that don't get parked or queued. Vehicle is archived.");
		}
		//Add vehicle to archive
		past.add(v);
		
		//Archives Incremented
		numArchives += 1;
	}
	
	/**
	 * Archive vehicles which have stayed in the queue too long 
	 * @param time int holding current simulation time 
	 * @throws VehicleException if one or more vehicles not in the correct state or if timing constraints are violated
	 */
	public void archiveQueueFailures(int time) throws VehicleException 
	{
		//With no vehicles being inputted here, I'm guessing this is the solution?
		for (int index = 0; index != queue.size(); index++)
		{
			if (time - queue.get(index).getArrivalTime() >= asgn2Simulators.Constants.MAXIMUM_QUEUE_TIME)
			{
				//Vehicle leaves queue state
				queue.get(index).exitQueuedState(time);
				
				//Vehicle added to archives
				past.add(queue.get(index));
				
				//Vehicle removed from queue
				queue.remove(index);
				
				//Archives incremented
				numArchives += 1;
			}
		}
	}
	
	/**
	 * Simple status showing whether carPark is empty
	 * @return true if car park empty, false otherwise
	 */
	public boolean carParkEmpty() 
	{
		return spaces.isEmpty();
	}
	
	/**
	 * Simple status showing whether carPark is full
	 * @return true if car park full, false otherwise
	 */
	public boolean carParkFull() {
		return (spaces.size() == this.maxSmallCarSpaces);
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
		//If Queue is full
		if (this.queueFull())
		{
			//Throw Exception
			throw new SimulationException("Simuation Exception: Queue is full: Will not add vehicle to queue.");
		}
		else //If queue is not full
		{
			v.enterQueuedState();
			this.queue.set(numQueue, v);
			numQueue += 1;
			count += 1;
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
	public void exitQueue(Vehicle v,int exitTime) throws SimulationException, VehicleException {
		
		if (!v.isQueued()) {
			throw new SimulationException("Simuation Exception: Vehicle is not in a queue.");
		} else {
			//Loop counter: for each vehicle in queue:
			int loop = 0;
			while (loop != this.maxQueueSize)
			{
				//If the vehicle trying to leave is in queue
				if (queue.get(loop) == v) //loop
				{
					//Remove Queued Status
					v.exitQueuedState(exitTime);
					//Remove from Queue
					queue.remove(loop);
					//Break the While Loop
					loop = this.maxQueueSize-1;
					//Adjust count
					numQueue -= 1;
				}
				loop += 1;
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
		//return number of vehicles in queue
		return this.numQueue;
	}
	
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
		if (v.getClass() == asgn2Vehicles.MotorCycle.class) //If the vehicle is a Motor Cycle
		{
			//If there are no Motor Cycle Spots Remaining
			if (this.motorCycleSpots == this.maxMotorCycleSpaces)
			{
				//If there are no Small Car Spots Remaining
				if (this.smallSpots == this.maxSmallCarSpaces)
				{
					//If there are no Normal Car Sports Remaining
					if (this.normalSpots == this.maxCarSpaces)
					{
						//Throw Exception: No Spaces available
						throw new SimulationException("Simulation Exception: There are no car parks available for a motor cycle.");
					}
					else //If there are Normal Car Spots Remaining
					{
						//Add to Normal Car Park Spots.
						spaces.add(v);
						typeSpaces.add("N");
						this.normalSpots += 1;
						this.numMotorCycles += 1;
					}
				}
				else //If there are Small Car Park Spots Remaining
				{
					//Add to Small Car Park Spots.
					spaces.add(v);
					typeSpaces.add("S");
					this.smallSpots += 1;
					this.numMotorCycles += 1;
				}
			}
			else //If there are Motor Cycle Park Spots Remaining
			{
				//Add to Motor Cycle Park Spots.
				spaces.add(v);
				typeSpaces.add("M");
				this.motorCycleSpots += 1;
				this.numMotorCycles += 1;
			}
		}
		else if (((Car) v).isSmall()) //If the car is a small car
		{
			//If there are no Small Car Park Spots Remaning
			if (this.smallSpots == this.maxSmallCarSpaces)
			{
				//If there are no Normal Car Park Spots Remaining
				if (this.normalSpots == this.maxCarSpaces)
				{
					//Throw Exception: No Spaces available
					throw new SimulationException("Simulation Exception: There are no car parks available for a small car.");
				}
				else //If there are Normal Car Park Spots Remaining
				{
					//Add to Normal Car Park Spots.
					spaces.add(v);
					typeSpaces.add("N");
					this.normalSpots += 1;
					this.numSmallCars += 1;
					this.numCars += 1;
				}
			}
			else
			{
				//Add to Small Car Park Spots.
				spaces.add(v);
				typeSpaces.add("S");
				this.smallSpots += 1;
				this.numSmallCars += 1;
				this.numCars += 1;
			}
		}
		else //If the car is a normal car
		{
			//If there are no Normal Car Park Spots Remaining
			if (this.normalSpots == this.maxCarSpaces)
			{
				//Throw Exception: No Spaces available
				throw new SimulationException("Simulation Exception: There are no car parks available for a normal car.");
			}
			else
			{
				//Add to Normal Car Park Spots.
				spaces.add(v);
				typeSpaces.add("N");
				this.normalSpots += 1;
				this.numCars += 1;
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
		
	}

	/**
	 * Simple status showing whether queue is empty
	 * @return true if queue empty, false otherwise
	 */
	public boolean queueEmpty() {
		//If the size of the queue is zero: return true, otherwise return false
		return queue.isEmpty();
	}

	/**
	 * Simple status showing whether queue is full
	 * @return true if queue full, false otherwise
	 */
	public boolean queueFull() {
		//If cars in queue = to max Queue Size: return true, otherwise return false
		if (this.numQueue == this.maxQueueSize)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Method determines, given a vehicle of a particular type, whether there are spaces available for that 
	 * type in the car park under the parking policy in the class header.  
	 * @param v Vehicle to be stored. 
	 * @return true if space available for v, false otherwise 
	 */
	public boolean spacesAvailable(Vehicle v) 
	{
		//If the vehicle is a motorcycle:
		if (v.getClass() == asgn2Vehicles.MotorCycle.class)
		{
			//If there are no spots left:
			if (motorCycleSpots + smallSpots + normalSpots == maxCarSpaces)
			{
				return false;
			}
			else //If there are spots left:
			{
				return true;
			}
		}
		else if (((Car) v).isSmall()) //If the vehicle is a small car
		{
			//If there are no spots left:
			if (smallSpots + normalSpots == maxCarSpaces - maxMotorCycleSpaces)
			{
				return false;
			}
			else //If there are spots left:
			{
				return true;
			}
		}
		else //If the vehicle is a normal car
		{
			//If there are no spots left:
			if (normalSpots == maxCarSpaces - maxMotorCycleSpaces - maxSmallCarSpaces)
			{
				return false;
			}
			else //If there are spots left:
			{
				return true;
			}
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
		boolean inPark = false;
		for (int index = 0; index != spaces.size(); index++)
		{
			if (spaces.get(index).getVehID() == v.getVehID())
			{
				//Vehicle is found in park
				inPark = true;
				//Vehicle leaves parked state
				v.exitParkedState(departureTime);
			}
		}
		if (!inPark)
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
