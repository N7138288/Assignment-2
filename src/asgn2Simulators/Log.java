/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Simulators 
 * 25/04/2014
 * 
 */
package asgn2Simulators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import asgn2CarParks.CarPark;

/**
 * Class to support logging for the CarParkSimulator 
 * @author hogan
 *
 */
public class Log {
	BufferedWriter writer = null;
	
	/**
	 * Constructor establishes a log file based on the current time in the canonical directory 
	 * @throws IOException if log file  or BufferedWriter cannot be created
	 */
	public Log () throws IOException {
		//File management based on http://stackoverflow.com/questions/15754523/how-to-write-text-file-java 
        File logFile = new File(getLogTime());

        // This will output the full path where the file will be written to...
        System.out.println(logFile.getCanonicalPath());
        this.writer = new BufferedWriter(new FileWriter(logFile));
	}
	
	/**
	 * Final state 
	 * @param cp CarPark being used
	 * @throws IOException on write or closure failures 
	 */
	public void finalise(CarPark cp) throws IOException {
		writer.write("\n" + getLogTime() + ": End of Simulation\n");
		writer.write(cp.finalState());
		writer.close();
	}
	
	/**
	 * Initial state of the simulation and carpark 
	 * @param cp CarPark being used
	 * @param sim Simulator providing parameters 
	 * @throws IOException on write failures 
	 */
	public void initialEntry(CarPark cp,Simulator sim) throws IOException {
		writer.write(getLogTime() + ": Start of Simulation\n");
		writer.write(sim.toString() + "\n");
		writer.write(cp.initialState() + "\n\n");
	}
	
	/**
	 * Log entry for each time step 
	 * @param time int holding current simulation time step 
	 * @param cp CarPark being used 
	 * @throws IOException on write failures 
	 */
	public void logEntry(int time,CarPark cp) throws IOException {
		writer.write(cp.getStatus(time));
	}
	
	/**
	 * Helper returning Log Time format for filename
	 * @return filename String yyyyMMdd_HHmmss
	 */
	private String getLogTime() {
		String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		return timeLog;
	}

}
