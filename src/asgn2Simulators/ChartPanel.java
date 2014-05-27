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
package asgn2Simulators;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/** 
 * Example code based on the Stack Overflow example and the 
 * standard JFreeChart demos showing the construction of a time series 
 * data set. Some of the data creation code is clearly a quick hack
 * and should not be taken as indicative of the required coding style. 
 * @see http://stackoverflow.com/questions/5048852
 * 
 *  */
@SuppressWarnings("serial")
public class ChartPanel extends ApplicationFrame {

     private String TITLE;
     private TimeSeriesCollection tsc;
     private TimeSeries totalVehicles;
     private TimeSeries parkedVehicles;
     private TimeSeries parkedCars;
     private TimeSeries parkedSmallCars;
     private TimeSeries parkedMotorCycles;
     private TimeSeries queuedVehicles;
     private TimeSeries archivedVehicles;
     private TimeSeries dissatisfiedVehicles;
	 
    /**
     * Constructor shares the work with the run method. 
     * @param title Frame display title
     */
    public ChartPanel(String title, boolean chart, String status) {
    	super(title);
    	TITLE = title;
    	if (chart == false)
    	{
    		
    	}
    	else
    	{
            final CategoryDataset dataset = createBarColumnDataset(status);
            final JFreeChart chart1 = createChart(dataset);
            final org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(chart1);
            chartPanel.setPreferredSize(new Dimension(500, 270));
            setContentPane(chartPanel);
    	}
    }

    /**
     * Private method creates the dataset. Lots of hack code in the 
     * middle, but you should use the labelled code below  
     * @return 
	 * @return collection of time series for the plot 
	 */
	public void createLineChartData() {
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
	public void addLineChartData(int time, String status) //String status
	{
		//These lines are important 
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(2014,0,1,6,time);
	    Date timePoint = cal.getTime();

		Pattern pattern = Pattern.compile("([0-9]+)");
		Matcher matcher = pattern.matcher(status);
		if (matcher.find())
		{
			//I am so sorry for this wreck. HOURS of screwing this up, I went with this highly unprofessional
			//Piece of code.
			//Time
			//Count
			matcher.find();
			totalVehicles.add(new Minute(timePoint), Integer.parseInt(matcher.group()));
			//Parked
			matcher.find();
			parkedVehicles.add(new Minute(timePoint), Integer.parseInt(matcher.group()));
			//Cars
			matcher.find();
			parkedCars.add(new Minute(timePoint), Integer.parseInt(matcher.group()));
			//Small Cars
			matcher.find();
			parkedSmallCars.add(new Minute(timePoint), Integer.parseInt(matcher.group()));
			//Motor Cycles
			matcher.find();
		 	parkedMotorCycles.add(new Minute(timePoint),Integer.parseInt(matcher.group()));
		 	//Dissatisfied
		 	matcher.find();
		 	dissatisfiedVehicles.add(new Minute(timePoint), Integer.parseInt(matcher.group()));
		 	//Archive
		 	matcher.find();
		 	archivedVehicles.add(new Minute(timePoint),Integer.parseInt(matcher.group()));
		 	//Queue
		 	matcher.find();
		 	queuedVehicles.add(new Minute(timePoint),Integer.parseInt(matcher.group()));		 	
		}
	}
		
	//Add series to tsc.
	//Do this only once the time is done
	//e.g.
	public TimeSeriesCollection concludeLineChartData()
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
    private JFreeChart createLineChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            TITLE, "hh:mm:ss", "Vehicles", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setAutoRange(true);
        return result;
    }
    
    public void createLineChart()
    {
    	JFreeChart chart = createLineChart(tsc);
    	this.add(new org.jfree.chart.ChartPanel(chart), BorderLayout.CENTER);
    	JPanel btnPanel = new JPanel(new FlowLayout());
    	this.add(btnPanel, BorderLayout.SOUTH);
    	this.pack();
    	RefineryUtilities.centerFrameOnScreen(this);
    	this.setVisible(true);
    }
    
    private CategoryDataset createBarColumnDataset(String status) {
        
        //Rows
        final String series1 = "Total Cars Processed";
        final String series2 = "Total Cars Dissatisfied";

        //Columns
        final String category1 = "";

        //Data Set
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
		Pattern pattern = Pattern.compile("([0-9]+)");
		Matcher matcher = pattern.matcher(status);
		if (matcher.find())
		{
			//Time
			//Count
			matcher.find();
			dataset.addValue(Integer.parseInt(matcher.group()), series1, category1);
			//Parked
			matcher.find();
			//Cars
			matcher.find();
			//Small Cars
			matcher.find();
			//Motor Cycles
			matcher.find();
		 	//Dissatisfied
		 	matcher.find(); 
		 	dataset.addValue(Integer.parseInt(matcher.group()), series2, category1);;		 	
		}      
        return dataset;          
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {
        
        //Create Chart
        final JFreeChart chart = ChartFactory.createBarChart(
            "Car Park Results",         // chart title
            "",               // domain axis label
            "Cars",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips
            false                     // URLs?
        );

        //Background Colour
        chart.setBackgroundPaint(Color.white);

        //Set more colours.
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        //Set the axis to be integers.
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        //Make pretty bars.
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        
        return chart;  
    }
 
    //Function to be called for Bar Chart
    public static void createBarChart(String status) {
    	//This is a train wreck.
    	final ChartPanel barChart = new ChartPanel("Car Park Results", true ,status);
    	barChart.pack();
        RefineryUtilities.centerFrameOnScreen(barChart);
        barChart.setVisible(true);
    }
}