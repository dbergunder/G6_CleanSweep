package edu.depaul.cleanSweep.diagnostics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.depaul.cleanSweep.floorPlan.FloorTile;

public class PowerConsumptionLog {
	// this class is a singleton class because we only ever want 1 power consumption log running
	private static PowerConsumptionLog pcl_instance = null; // singleton pattern
	public static File file = null; //holds the file location
	
	// singleton constructor
	private PowerConsumptionLog() throws IOException {
		file = new File("files/PowerConsumptionLog.txt");
		startPowerConsumptionLog();
	}
	
	// singleton get instance
	public static PowerConsumptionLog getInstance() throws IOException {
		if (pcl_instance == null) {pcl_instance = new PowerConsumptionLog();} 
  
        return pcl_instance; 
	}
	
	// create a new file if not already created or overwrite a current file with a header
	public void startPowerConsumptionLog() throws IOException {
		try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("could not create new log file");
            e.printStackTrace();
        }
		
		// formatted string header
		String fileHeader = String.format("%-10s | %-10s | %-15s | %-15s | %-15s", "Event", "New (Y,X)", "Power Started", "Power Consumed", "Power Final");

	    FileWriter fstream;
	    try {
	        fstream = new FileWriter(file, false); // false option will overwrite a current power consumption log
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write(fileHeader);
	        out.newLine();
	        out.close();
	    } catch (IOException e) {
	        System.out.println("could not write to the file");
	        e.printStackTrace();
	    }
	}
	
	public void logPowerUsed(String event, FloorTile prevNode, FloorTile currNode, double currBattery, double powerCost) {
		String tupleCoord = String.format("(%d,%d)", currNode._y, currNode._x);
		String append = String.format("\n%-10s | %-10s | %-15.2f | %-15.2f | %-15.2f", event, tupleCoord, currBattery, powerCost, currBattery - powerCost);

	    FileWriter fstream;
	    try {
	        fstream = new FileWriter(file, true); // true option will append to a power consumption log
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write(append);
	        out.close();
	    } catch (IOException e) {
	        System.out.println("could not write to the file");
	        e.printStackTrace();
	    }
	}
}
