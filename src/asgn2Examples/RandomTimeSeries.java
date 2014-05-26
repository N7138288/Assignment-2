/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Examples 
 * 11/05/2014
 * 
 */
package asgn2Examples;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import asgn2CarParks.CarPark;
/** 
 * Example code based on the Stack Overflow example and the 
 * standard JFreeChart demos showing the construction of a time series 
 * data set. Some of the data creation code is clearly a quick hack
 * and should not be taken as indicative of the required coding style. 
 * @see http://stackoverflow.com/questions/5048852
 * 
 *  */
@SuppressWarnings("serial")
public class RandomTimeSeries extends ApplicationFrame {

     private String TITLE;
     TimeSeriesCollection tsc;
	 TimeSeries totalVehicles;
	 TimeSeries parkedVehicles;
	 TimeSeries parkedCars;
	 TimeSeries parkedSmallCars;
	 TimeSeries parkedMotorCycles;
	 TimeSeries queuedVehicles;
	 TimeSeries archivedVehicles;
	 TimeSeries dissatisfiedVehicles
	 ;
    /**
     * Constructor shares the work with the run method. 
     * @param title Frame display title
     */
    public RandomTimeSeries(String title) {
    	super(title);
    	TITLE = title;
    }

    /**
     * Private method creates the dataset. Lots of hack code in the 
     * middle, but you should use the labelled code below  
     * @return 
	 * @return collection of time series for the plot 
	 */
	public void createTimeSeriesData() {
		tsc = new TimeSeriesCollection(); 	
		 totalVehicles = new TimeSeries("Total Vehicles to Date");
		 parkedVehicles = new TimeSeries("Total Vehicles Parked");
		 parkedCars = new TimeSeries("Total Cars Parked");
		 parkedSmallCars = new TimeSeries("Total Small Cars Parked");
		 parkedMotorCycles = new TimeSeries("Total Motor Cycles Parked");
		 queuedVehicles = new TimeSeries("Total Vehicles Queued");
		 archivedVehicles = new TimeSeries("Total Vehicles Archived");
		 dissatisfiedVehicles = new TimeSeries("Total Vehicles Dissatisfied");
	}
		 
		
		//For each unit of time:
		//e.g. 
		public void addTimeSeriesData(int time, CarPark cp)
		{
			//These lines are important 
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(2014,0,1,6,time);
		    Date timePoint = cal.getTime();
		    
		   new Minute(timePoint);
			totalVehicles.add(new Minute(timePoint), cp.getCount());
		 	parkedVehicles.add(new Minute(timePoint), cp.getNumCars() + cp.getNumMotorCycles());
		 	parkedCars.add(new Minute(timePoint), cp.getNumCars());
		 	parkedSmallCars.add(new Minute(timePoint),cp.getNumSmallCars());
		 	parkedMotorCycles.add(new Minute(timePoint),cp.getNumMotorCycles());
		 	queuedVehicles.add(new Minute(timePoint),cp.getQueue().size());
		 	archivedVehicles.add(new Minute(timePoint),cp.getPast().size());
		 	dissatisfiedVehicles.add(new Minute(timePoint), cp.getNumDissatisfied());
		}
		
		//Add series to tsc.
		//Do this only once the time is done
		//e.g.
		public TimeSeriesCollection concludeTimeSeriesData()
		{
			tsc.addSeries(totalVehicles);
			tsc.addSeries(parkedVehicles);
			tsc.addSeries(parkedCars);
			tsc.addSeries(parkedSmallCars);
			tsc.addSeries(parkedMotorCycles);
			tsc.addSeries(queuedVehicles);
			tsc.addSeries(archivedVehicles);
			tsc.addSeries(dissatisfiedVehicles);
			
			return tsc;
		}
	

    /**
     * Helper method to deliver the Chart - currently uses default colours and auto range 
     * @param dataset TimeSeriesCollection for plotting 
     * @returns chart to be added to panel 
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            TITLE, "hh:mm:ss", "Vehicles", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setAutoRange(true);
        return result;
    }
    
    public void createChart()
    {
    	JFreeChart chart = createChart(tsc);
    	this.add(new ChartPanel(chart), BorderLayout.CENTER);
    	JPanel btnPanel = new JPanel(new FlowLayout());
    	this.add(btnPanel, BorderLayout.SOUTH);
    	this.pack();
    	RefineryUtilities.centerFrameOnScreen(this);
    	this.setVisible(true);
    }
    //
}