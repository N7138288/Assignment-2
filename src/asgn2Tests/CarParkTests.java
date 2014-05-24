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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;

/**
 * @author hogan
 *
 */

//Need to add vehicle state execptions for certain methods
public class CarParkTests {
	
	CarPark testCarPark;
	Car carTest;
	Car smallCarTest;
	MotorCycle bikeTest;
	Simulator testSim;
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testCarPark = new CarPark();
		carTest = new Car("111AAA", 1, false);
		smallCarTest = new Car("222AAA", 1, true);
		bikeTest = new MotorCycle("333AAA", 1);
	}
	/*
	@Test (expected=SimulationException.class )
	public void negSetupMaxCarSpaces() throws SimulationException {
		testCarPark = new CarPark(-1,50,50,50);
	}
	
	@Test (expected=SimulationException.class )
	public void negSetupMaxSmallCarSpaces() throws SimulationException {
		testCarPark = new CarPark(50,-1,50,50);
	}
	
	@Test (expected=SimulationException.class )
	public void negSetupMaxMotorCycleSpaces() throws SimulationException {
		testCarPark = new CarPark(50,50,-1,50);
	}
	
	@Test (expected=SimulationException.class )
	public void negSetupMaxQueueSize() throws SimulationException {
		testCarPark = new CarPark(50,50,50,-1);
	}
	*/
	
	@Test (expected=SimulationException.class )
	public void archiveNewVehicleParkedFail() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(carTest, 2, 120);
		testCarPark.archiveNewVehicle(carTest);
	}
	
	@Test (expected=SimulationException.class )
	public void archiveNewVehicleQueuedFail() throws SimulationException, VehicleException {
		testCarPark.enterQueue(carTest);
		testCarPark.archiveNewVehicle(carTest);
	}
	
	@Test
	public void checkEmptyCarParkInitial() throws SimulationException {
		assertTrue(testCarPark.carParkEmpty());
	}
	
	@Test
	public void checkEmptyCarParkWorking() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(carTest, 2, 120);
		assertFalse(testCarPark.carParkEmpty());
	}
	
	@Test
	public void checkCarParkAddition() throws SimulationException, VehicleException {
		testCarPark.enterQueue(carTest);
		testCarPark.enterQueue(smallCarTest);
		testCarPark.parkVehicle(carTest, 2, 120);
		testCarPark.parkVehicle(smallCarTest, 2, 120);
		assertEquals(testCarPark.getNumCars(), 2);
	}	
	
	@Test
	public void checkCarParks() throws SimulationException, VehicleException {
		testCarPark.enterQueue(carTest);
		testCarPark.parkVehicle(carTest, 2, 120);
		assertFalse(carTest.isParked());
	}	
	
	@Test
	public void testArchiveDepartingVehicles() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveQueueFailures(int)}.
	 */
	@Test
	public void testArchiveQueueFailures() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCarParkFull() throws SimulationException, VehicleException {
		testCarPark = new CarPark(1,0,0,0);
		testCarPark.parkVehicle(carTest, 2, 120);
		assertTrue(testCarPark.carParkFull());
	}

	@Test (expected = SimulationException.class)
	public void checkEnterQueueExcept() throws SimulationException, VehicleException {
		testCarPark = new CarPark(50,50,50,0);
		testCarPark.enterQueue(carTest);
	}
	
	@Test
	public void checkCarAdditionQueue() throws SimulationException, VehicleException {
		testCarPark.enterQueue(carTest);
		testCarPark.enterQueue(smallCarTest);
		assertEquals(testCarPark.numVehiclesInQueue(), 2);
	}	
	
	@Test (expected = SimulationException.class)
	public void checkExitQueueExcept() throws SimulationException, VehicleException {
		testCarPark.exitQueue(carTest, 120);
	}
	
	@Test
	public void checkExitQueueLogic() throws SimulationException, VehicleException {
		testCarPark.enterQueue(carTest);
		assertEquals(testCarPark.numVehiclesInQueue(), 1);
		testCarPark.exitQueue(carTest, 120);
		assertEquals(testCarPark.numVehiclesInQueue(), 0);
	}

	@Test
	public void testGetNumMotorCycles() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(bikeTest, 2, 120);
		assertEquals(testCarPark.getNumMotorCycles(), 1);
	}

	@Test
	public void testGetNumSmallCars() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(smallCarTest, 2, 120);
		assertEquals(testCarPark.getNumSmallCars(), 1);
	}

	//add more tests for this method according to the specs
	@Test
	public void testProcessQueue() throws VehicleException, SimulationException {
		testCarPark.enterQueue(carTest);
		testCarPark.processQueue(150, testSim);
		assertTrue(carTest.isParked());
	}

	@Test
	public void testQueueEmpty() throws SimulationException, VehicleException {
		assertTrue(testCarPark.queueEmpty());
	}
	

	@Test
	public void testQueueFull() throws SimulationException, VehicleException {
		testCarPark = new CarPark(50,50,50,2);
		testCarPark.enterQueue(carTest);
		assertTrue(testCarPark.queueFull());
	}

	@Test
	public void testSpacesAvailable() throws SimulationException, VehicleException {
		testCarPark = new CarPark(50,50,50,2);
		testCarPark.enterQueue(carTest);
		assertEquals(testCarPark.spacesAvailable(carTest), 1);
	}


	@Test
	public void testTryProcessNewVehicles() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#unparkVehicle(asgn2Vehicles.Vehicle, int)}.
	 */
	@Test
	public void testUnparkVehicle() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test (expected = SimulationException.class)
	public void simulationExceptUnparkVehicle() throws VehicleException, SimulationException {
		Car notInitializedCar = null;
		testCarPark.unparkVehicle(notInitializedCar, 150);
	}	
	
	@Test (expected = VehicleException.class)
	public void vehicleNoParkExceptUnparkVehicle() throws VehicleException, SimulationException {
		testCarPark.enterQueue(carTest);
		testCarPark.unparkVehicle(carTest, 150);
	}	
	
	@Test (expected = VehicleException.class)
	public void vehicleQueueExceptUnparkVehicle() throws VehicleException, SimulationException {
		testCarPark.unparkVehicle(carTest, 150);
		testCarPark.enterQueue(carTest);
		testCarPark.unparkVehicle(carTest, 150);
	}	
	/* Need to look further into the time constraint violations for vehicles
	@Test (expected = VehicleException.class)
	public void vehicleTimingExceptUnparkVehicle() throws VehicleException, SimulationException {
		testCarPark.unparkVehicle(carTest, 150);
	}	
	*/
}
