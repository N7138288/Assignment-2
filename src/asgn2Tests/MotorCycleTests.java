/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 22/04/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import java.lang.reflect.Array;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Vehicles.MotorCycle;
import asgn2Vehicles.Vehicle;

/**
 * @author hogan
 *
 */
public class MotorCycleTests {
	
	MotorCycle testBike;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link asgn2Vehicles.MotorCycle#MotorCycle(java.lang.String, int)}.
	 */
	
	@Test(expected = VehicleException.class)
	public void negArrivalTime() throws VehicleException {
		testBike = new MotorCycle("111AAA", -1);
	}
	
	@Test(expected = VehicleException.class)
	public void zeroArrivalTime() throws VehicleException {
		testBike = new MotorCycle("111AAA", 0);
	}
	
	@Test
	public void checkArrivalTime() throws VehicleException {
		testBike = new MotorCycle("111AAA", 50);
		assertEquals(testBike.getArrivalTime(), 50);
	}
	
	@Test
	public void notParkedCreation() throws VehicleException {
		testBike = new MotorCycle("111AAA", 50);
		assertFalse(testBike.isParked());
	}
	/**
	@Test
	public void testMotorCycle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#Vehicle(java.lang.String, int)}.
	 
	@Test
	public void testVehicle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getVehID()}.
	 
	@Test
	public void testGetVehID() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getArrivalTime()}.
	 
	@Test
	public void testGetArrivalTime() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterQueuedState()}.
	 
	@Test
	public void testEnterQueuedState() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 
	@Test
	public void testExitQueuedState() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 
	@Test
	public void testEnterParkedState() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState(int)}.
	 
	@Test
	public void testExitParkedStateInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState()}.
	 
	@Test
	public void testExitParkedState() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isParked()}.
	 
	@Test
	public void testIsParked() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isQueued()}.
	 
	@Test
	public void testIsQueued() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getParkingTime()}.
	 
	@Test
	public void testGetParkingTime() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getDepartureTime()}.
	 
	@Test
	public void testGetDepartureTime() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#wasQueued()}.
	 
	@Test
	public void testWasQueued() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#wasParked()}.
	 
	@Test
	public void testWasParked() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isSatisfied()}.
	 
	@Test
	public void testIsSatisfied() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#toString()}.
	 
	@Test
	public void testToString() {
		fail("Not yet implemented"); // TODO
	}*/


	/* MotorCycleTEsts */
	/*
	 * Confirm that the API spec has not been violated through the
	 * addition of public fields, constructors or methods that were
	 * not requested
	 */
	@Test
	public void NoExtraPublicMethods() {
		//MotorCycle Class implements Vehicle
		final int NumVehicleClassMethods = Array.getLength(Vehicle.class.getMethods());
		final int NumMotorCycleClassMethods = Array.getLength(MotorCycle.class.getMethods());
		assertTrue("veh:"+NumVehicleClassMethods+":MotorCycle:"+NumMotorCycleClassMethods,(NumVehicleClassMethods)==NumMotorCycleClassMethods);
	}
	
	@Test 
	public void NoExtraPublicFields() {
		//Same as Vehicle 
		final int NumVehicleClassFields = Array.getLength(Vehicle.class.getFields());
		final int NumMotorCycleClassFields = Array.getLength(MotorCycle.class.getFields());
		assertTrue("veh:"+NumVehicleClassFields+":MotorCycle:"+NumMotorCycleClassFields,(NumVehicleClassFields)==NumMotorCycleClassFields);
	}
	
	@Test 
	public void NoExtraPublicConstructors() {
		//Same as Vehicle
		final int NumVehicleClassConstructors = Array.getLength(Vehicle.class.getConstructors());
		final int NumMotorCycleClassConstructors = Array.getLength(MotorCycle.class.getConstructors());
		assertTrue(":veh:"+NumVehicleClassConstructors+":mc:"+NumMotorCycleClassConstructors,(NumVehicleClassConstructors)==NumMotorCycleClassConstructors);
	}
}
