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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Vehicles.Car;

/**
 * @author hogan
 *
 */
public class CarTests {
	
	Car testCar;

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
	 * Test method for {@link asgn2Vehicles.Car#toString()}.
	 * @throws VehicleException 
	 */
	
	@Test(expected = VehicleException.class)
	public void negArrivalTime() throws VehicleException {
		testCar = new Car("111AAA", -1, false);
	}
	
	@Test(expected = VehicleException.class)
	public void zeroArrivalTime() throws VehicleException {
		testCar = new Car("111AAA", 0, false);
	}
	
	@Test
	public void isSmallCar() throws VehicleException {
		testCar = new Car("111AAA", 20, true);
		assertTrue(testCar.isSmall());
	}
	
	@Test
	public void isNormalCar() throws VehicleException {
		testCar = new Car("111AAA", 20, false);
		assertFalse(testCar.isSmall());
	}
	
	@Test
	public void checkArrivalTime() throws VehicleException {
		testCar = new Car("111AAA", 20, false);
		assertEquals(testCar.getArrivalTime(), 20);
	}
	
	
	/**
	@Test
	public void testToString() {
		fail("Not yet implemented"); // TODO
	}


	@Test
	public void testCar() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testIsSmall() {
		fail("Not yet implemented"); // TODO
	}  */

}
