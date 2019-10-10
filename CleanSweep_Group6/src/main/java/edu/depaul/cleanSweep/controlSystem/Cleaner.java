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
	// The vacuumbag is a list, with each node representing a "cleaning" of a tile
	// Each clean appends a Pair representing the amount of dirt cleaned, as well the surface type
	// In order to traverse through the history, start at the head, and work downward
	private List<Pair<Integer, SurfaceType>> vacuumBag = new LinkedList<Pair<Integer, SurfaceType>>();

	
	private static final int MAX_DIRT_CAPACITY = 50;


	// Check for "cleanliness" of current surface. Clean if need be and update capacity
	public void cleanSurface(Cell currentCell){

		// Cell is currently clean. No need to do anything
		if(currentCell.surface == SurfaceType.BARE){
			return;
		}

		// Cell is not clean, clean it, update bag, and change cell state

		// Check for space
		if(currentCell.dirtAmount + getCurrentBagSize() > MAX_DIRT_CAPACITY){
			// Can't hold any more. Do not clean cell
			return;
		}
		else{
			// Add to vaccumbag
			vacuumBag.add(
					new Pair<Integer, SurfaceType>(currentCell.dirtAmount, currentCell.surface));

			// Clean dirt
			currentCell.dirtAmount = 0;
			currentCell.surface = SurfaceType.BARE;
		}
	}

	public void changeHeading(char h) {
		headingTowards = h;
	}



}
