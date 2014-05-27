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
import asgn2Vehicles.Car;
import asgn2Vehicles.Vehicle;

/**
 * @author hogan
 *
 */
public class CarTests {
	
	Car testCar;

	//Check exception for negative arrival time
	@Test(expected = VehicleException.class)
	public void negArrivalTime() throws VehicleException {
		testCar = new Car("111AAA", -1, false);
	}
	
	//Check exception for zero arrival time
	@Test(expected = VehicleException.class)
	public void zeroArrivalTime() throws VehicleException {
		testCar = new Car("111AAA", 0, false);
	}
	
	//Test setting the car to small changes parameters
	@Test
	public void isSmallCar() throws VehicleException {
		testCar = new Car("111AAA", 20, true);
		assertTrue(testCar.isSmall());
	}
	
	//Check that normal car is input as a normal car
	@Test
	public void isNormalCar() throws VehicleException {
		testCar = new Car("111AAA", 20, false);
		assertFalse(testCar.isSmall());
	}
	
	//Test the arrival time of car is working properly
	@Test
	public void checkArrivalTime() throws VehicleException {
		testCar = new Car("111AAA", 20, false);
		assertEquals(testCar.getArrivalTime(), 20);
	}

	/* CarTests.java */ 
	/*
	 * Confirm that the API spec has not been violated through the
	 * addition of public fields, constructors or methods that were
	 * not requested
	 */
	@Test
	public void NoExtraPublicMethods() {
		//Car Class implements Vehicle, adds isSmall() 
		final int NumVehicleClassMethods = Array.getLength(Vehicle.class.getMethods());
		final int NumCarClassMethods = Array.getLength(Car.class.getMethods());
		assertTrue("veh:"+NumVehicleClassMethods+":car:"+NumCarClassMethods,
				(NumVehicleClassMethods+1)==NumCarClassMethods);
	}

	@Test 
	public void NoExtraPublicFields() {
		//Same as Vehicle 
		final int NumVehicleClassFields = Array.getLength(Vehicle.class.getFields());
		final int NumCarClassFields = Array.getLength(Car.class.getFields());
		assertTrue("veh:"+NumVehicleClassFields+":car:"+NumCarClassFields,
				(NumVehicleClassFields)==NumCarClassFields);
	}

	@Test 
	public void NoExtraPublicConstructors() {
		//Same as Vehicle
		final int NumVehicleClassConstructors = Array.getLength(Vehicle.class.getConstructors());
		final int NumCarClassConstructors = Array.getLength(Car.class.getConstructors());
		assertTrue(":veh:"+NumVehicleClassConstructors+":car:"+NumCarClassConstructors,
				(NumVehicleClassConstructors)==NumCarClassConstructors);
	}
}
