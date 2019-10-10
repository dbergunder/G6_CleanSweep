package edu.depaul.cleanSweep.controlSystem;

import edu.depaul.cleanSweep.cell.CellNode;
import edu.depaul.cleanSweep.cell.SideType;
import edu.depaul.cleanSweep.cell.Cell;
import edu.depaul.cleanSweep.cell.SurfaceType;
import org.javatuples.Pair;

public class Cleaner {

	private static final int MAX_BATTERY_POWER = 250;
	private static final int MAX_DIRT_CAPACITY = 50; 
	//a random number I chose. cannot find a specific number in the instruction

	private int currBattery;
	private int currDirtCapacity;
	private CellNode currCell;
	private char headingTowards = 'N';

	
	private static final int MAX_DIRT_CAPACITY = 50;

	public Cleaner(int battery, int dirtCapacity, CellNode node) {
		currBattery = battery;
		currDirtCapacity = dirtCapacity;
		currCell = node;
	}

	public void changeHeading(char h) {
		headingTowards = h;
	}


}
