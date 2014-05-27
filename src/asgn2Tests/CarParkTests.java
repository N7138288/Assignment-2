/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 29/04/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import java.lang.reflect.Array;

import org.junit.Before;
import org.junit.Test;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;

public class CarParkTests {
	
	CarPark testCarPark;
	Car carTest;
	Car smallCarTest;
	MotorCycle bikeTest;
	Simulator testSim;
	
	//Setup of parameters to be used throughout testing
	@Before
	public void setUp() throws Exception {
		testCarPark = new CarPark();
		carTest = new Car("111AAA", 1, false);
		smallCarTest = new Car("222AAA", 1, true);
		bikeTest = new MotorCycle("333AAA", 1);
	}
	
	//Check exception state on archiving a new vehicle when it is parked
	@Test (expected=SimulationException.class )
	public void archiveNewVehicleParkedFail() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(carTest, 2, 120);
		testCarPark.archiveNewVehicle(carTest);
	}
	
	//Test exception on archiving a vehicle that is already queued
	@Test (expected=SimulationException.class )
	public void archiveNewVehicleQueuedFail() throws SimulationException, VehicleException {
		testCarPark.enterQueue(carTest);
		testCarPark.archiveNewVehicle(carTest);
	}
	
	//Assert that when created the car park is indeed empty
	@Test
	public void checkEmptyCarParkInitial() throws SimulationException {
		assertTrue(testCarPark.carParkEmpty());
	}
	
	//Make sure that when vehicles are added to the car park, it is no longer empty
	@Test
	public void checkEmptyCarParkWorking() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(carTest, 2, 120);
		assertFalse(testCarPark.carParkEmpty());
	}
	
	//Check the car park addition to make sure it is adding the correct amount of cars to the 
	//car park when entered
	@Test
	public void checkCarParkAddition() throws SimulationException, VehicleException {
		Simulator testSim = new Simulator();
		testCarPark.enterQueue(carTest);
		testCarPark.enterQueue(smallCarTest);
		testCarPark.processQueue(10, testSim);
		testCarPark.processQueue(15, testSim);
		assertEquals(testCarPark.getNumCars(), 2);
	}	
	
	//Check to make sure with spaces available the processQueue method removes cars
	//from the queue and assigns them to a park
	@Test
	public void checkCarParks() throws SimulationException, VehicleException {
		Simulator testSim = new Simulator();
		testCarPark.enterQueue(carTest);
		testCarPark.processQueue(110, testSim);
		assertTrue(carTest.isParked());
	}	
	
	//Test to check that after the alloted stay time of a vehicle arriving it can be archived
	//Without being forced. processQueue was not used as a time could not be set. This way after
	//the vehicle arrives at 2 and has a stay of 20, at 22 the vehicle can be archived and departed
	@Test
	public void testArchiveDepartingVehicles() throws SimulationException, VehicleException {
		testCarPark.enterQueue(carTest);
		carTest.exitQueuedState(2);
		testCarPark.parkVehicle(carTest, 2, 20);
		testCarPark.archiveDepartingVehicles(2+20, false);//2+20 = arrival + stay
		assertTrue(testCarPark.carParkEmpty());
	}
	
	//Test to make sure that the force a vehicle to depart works. This is done by departing the vehicle 
	//the next time frame after it has arrived
	@Test
	public void testArchiveDepartingVehiclesForce() throws SimulationException, VehicleException {
		Simulator testSim = new Simulator();
		testCarPark.enterQueue(carTest);
		testCarPark.processQueue(1, testSim);
		testCarPark.archiveDepartingVehicles(2, true);
		assertTrue(testCarPark.carParkEmpty());
	}

	//Test to see if a vehicle leaves the queue after the max wait time
	//Vehicle arrives at 1, waits till max queue time then leaves on the next time frame
	@Test
	public void testArchiveQueueFailures() throws SimulationException, VehicleException {
		testCarPark.enterQueue(carTest);
		testCarPark.archiveQueueFailures(asgn2Simulators.Constants.MAXIMUM_QUEUE_TIME+2);
		assertTrue(testCarPark.queueEmpty());
	}

	//Check to see if the carParkFull method functions properly
	@Test
	public void testCarParkFull() throws SimulationException, VehicleException {
		testCarPark = new CarPark(1,0,0,0);
		testCarPark.parkVehicle(carTest, 2, 120);
		assertTrue(testCarPark.carParkFull());
	}

	//Check the SimulationException works when queue is full and a car is trying to join the queue
	@Test (expected = SimulationException.class)
	public void checkEnterQueueExcept() throws SimulationException, VehicleException {
		testCarPark = new CarPark(50,50,50,0);
		testCarPark.enterQueue(carTest);
	}
	
	//Check that adding cars to the queue effects the method numVehiclesInQueue
	@Test
	public void checkCarAdditionQueue() throws SimulationException, VehicleException {
		testCarPark.enterQueue(carTest);
		testCarPark.enterQueue(smallCarTest);
		assertEquals(testCarPark.numVehiclesInQueue(), 2);
	}	
	
	//Check exception on trying to get a car to leave the queue when it is not in it
	@Test (expected = SimulationException.class)
	public void checkExitQueueExcept() throws SimulationException, VehicleException {
		testCarPark.exitQueue(carTest, 120);
	}
	
	//Checking that the process exitQueue actually takes the vehicle out of the queue
	@Test
	public void checkExitQueueLogic() throws SimulationException, VehicleException {
		testCarPark.enterQueue(carTest);
		testCarPark.exitQueue(carTest, 120);
		assertEquals(testCarPark.numVehiclesInQueue(), 0);
	}

	//Check that motorcycles are correctly stored within the car park
	@Test
	public void testGetNumMotorCycles() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(bikeTest, 2, 120);
		assertEquals(testCarPark.getNumMotorCycles(), 1);
	}

	//Check that small cars are correctly stored within the car park
	@Test
	public void testGetNumSmallCars() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(smallCarTest, 2, 120);
		assertEquals(testCarPark.getNumSmallCars(), 1);
	}

	//Assert that the processQueue function takes a vehicle out of the queue when there are spaces available
	@Test
	public void testProcessQueue() throws VehicleException, SimulationException {
		Simulator testSim = new Simulator();
		testCarPark.enterQueue(carTest);
		testCarPark.processQueue(150, testSim);
		assertFalse(carTest.isQueued());
	}

	//Test car park queue is empty on creation
	@Test
	public void testQueueEmpty() throws SimulationException, VehicleException {
		assertTrue(testCarPark.queueEmpty());
	}
	
	//Test that the queue can fill up and the method queueFull shows this when at capacity
	@Test
	public void testQueueFull() throws SimulationException, VehicleException {
		testCarPark = new CarPark(50,50,50,2);
		testCarPark.enterQueue(carTest);
		testCarPark.enterQueue(smallCarTest);
		assertTrue(testCarPark.queueFull());
	}

	//Test the the spacesAvaliable method returns null for a car for a 
	//car park of only small and motorcycle parks
	@Test
	public void testSpacesAvailableNotNormal() throws SimulationException, VehicleException {
		CarPark testCarPark = new CarPark(20, 20, 10, 1);
		assertFalse(testCarPark.spacesAvailable(carTest));
	}
	
	//Test the the spacesAvaliable method returns null for a bike for a 
	//car park of only normal car parks
	@Test
	public void testSpacesAvailableNormal() throws SimulationException, VehicleException {
		CarPark testCarPark = new CarPark(20, 0, 0, 1);
		assertFalse(testCarPark.spacesAvailable(bikeTest));
	}
	
	//Check the exception thrown when trying to un-park a vehicle not in the car park
	@Test (expected = SimulationException.class)
	public void simulationExceptUnparkVehicle() throws VehicleException, SimulationException {
		Car notInitializedCar = null;
		testCarPark.unparkVehicle(notInitializedCar, 150);
	}	
	
	//Meant to throw vehicle except if in queue
	
	//Check the exception thrown for trying to un-park a queued vehicle
	@Test (expected = VehicleException.class)
	public void vehicleQueueExceptUnparkVehicle() throws VehicleException, SimulationException {
		testCarPark.enterQueue(carTest);
		testCarPark.unparkVehicle(carTest, 150);
	}
	
	//Vehicle is in car park but throws exception not in car park
	
	//Test the exception when trying to un-park a vehicle that is in the car park but not parked
	@Test (expected = VehicleException.class)
	public void vehicleNotParkedExceptUnparkVehicle() throws VehicleException, SimulationException {
		testCarPark.enterQueue(carTest);
		carTest.exitQueuedState(120);
		testCarPark.unparkVehicle(carTest, 150);
	}
	
	/* CarParkTests.java */
	/*
	 * Confirm that the API spec has not been violated through the
	 * addition of public fields, constructors or methods that were
	 * not requested
	 */
	@Test
	public void NoExtraPublicMethods() {
		//Extends Object, extras less toString() 
		final int ExtraMethods = 21; 
		final int NumObjectClassMethods = Array.getLength(Object.class.getMethods());
		final int NumCarParkClassMethods = Array.getLength(CarPark.class.getMethods());
		assertTrue("obj:"+NumObjectClassMethods+":cp:"+NumCarParkClassMethods,(NumObjectClassMethods+ExtraMethods)==NumCarParkClassMethods);
	}

	@Test 
	public void NoExtraPublicFields() {
		//Same as Vehicle 
		final int NumObjectClassFields = Array.getLength(Object.class.getFields());
		final int NumCarParkClassFields = Array.getLength(CarPark.class.getFields());
		assertTrue("obj:"+NumObjectClassFields+":cp:"+NumCarParkClassFields,(NumObjectClassFields)==NumCarParkClassFields);
	}

	@Test 
	public void NoExtraPublicConstructors() {
		//One extra cons used. 
		final int NumObjectClassConstructors = Array.getLength(Object.class.getConstructors());
		final int NumCarParkClassConstructors = Array.getLength(CarPark.class.getConstructors());
		assertTrue(":obj:"+NumObjectClassConstructors+":cp:"+NumCarParkClassConstructors,(NumObjectClassConstructors+1)==NumCarParkClassConstructors);
	}
}
