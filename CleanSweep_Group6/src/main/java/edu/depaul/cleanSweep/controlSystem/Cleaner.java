package edu.depaul.cleanSweep.controlSystem;

import edu.depaul.cleanSweep.cell.CellNode;
import edu.depaul.cleanSweep.cell.SideType;
import edu.depaul.cleanSweep.cell.Cell;
import edu.depaul.cleanSweep.cell.SurfaceType;
import org.javatuples.Pair;

import java.util.LinkedList;
import java.util.List;

public class Cleaner {

	private static final int MAX_BATTERY_POWER = 250;
	private static final int MAX_DIRT_CAPACITY = 50;
	// A random number I chose. cannot find a specific number in the instruction

	private int currBattery;
	private int currDirtCapacity;
	private boolean atCapacity;

	private CellNode currCell;
	private char headingTowards = 'N';

	// The vacuumbag is a list, with each node representing a "cleaning" of a tile
	// Each clean appends a Pair representing the amount of dirt cleaned, as well the surface type
	// In order to traverse through the history, start at the head, and work downward
	private List<Pair<Integer, SurfaceType>> vacuumBag = new LinkedList<Pair<Integer, SurfaceType>>();

	public Cleaner(){

	}

	public Cleaner(int battery, int dirtCapacity, CellNode node) {
		currBattery = battery;
		currDirtCapacity = dirtCapacity;
		currCell = node;
	}

	/*
	 * According to the current heading direction, first check if the corresponding side is blocked,
	 * and then move to the next cell.
	 */
	public void moveAhead() {
		switch(this.headingTowards) {
			case 'N':
				if(this.currCell.getSideN()== SideType.FLOORCELL || this.currCell.getSideN() == SideType.OPENDOOR) {
					this.currCell = currCell.getCellN();
				}
				break;
			case 'S':
				if(this.currCell.getSideS()== SideType.FLOORCELL || this.currCell.getSideS() == SideType.OPENDOOR) {
					this.currCell = currCell.getCellS();
				}
				break;
			case 'W':
				if(this.currCell.getSideW()== SideType.FLOORCELL || this.currCell.getSideW() == SideType.OPENDOOR) {
					this.currCell = currCell.getCellW();
				}
				break;
			case 'E':
				if(this.currCell.getSideE()== SideType.FLOORCELL || this.currCell.getSideE() == SideType.OPENDOOR) {
					this.currCell = currCell.getCellE();
				}
				break;
		}
		printCoordinate();
		//change current battery level
	}
	/*
	 * For moving left, right or back, the heading direction will change correspondingly, and then move forward.
	 */
	public void moveLeft() {
		switch(this.headingTowards) {
			case 'N':
				this.headingTowards = 'W';
				this.moveAhead();
				break;
			case 'S':
				this.headingTowards = 'E';
				this.moveAhead();
				break;
			case 'W':
				this.headingTowards = 'S';
				this.moveAhead();
				break;
			case 'E':
				this.headingTowards = 'N';
				this.moveAhead();
				break;
		}
		printCoordinate();
		//change current battery level
	}

	public void moveRight() {
		switch(this.headingTowards) {
			case 'N':
				this.headingTowards = 'E';
				this.moveAhead();
				break;
			case 'S':
				this.headingTowards = 'W';
				this.moveAhead();
				break;
			case 'W':
				this.headingTowards = 'N';
				this.moveAhead();
				break;
			case 'E':
				this.headingTowards = 'S';
				this.moveAhead();
				break;
		}
		printCoordinate();
		//change current battery level
	}

	public void moveBack() {
		switch(this.headingTowards) {
			case 'N':
				this.headingTowards = 'S';
				this.moveAhead();
				break;
			case 'S':
				this.headingTowards = 'N';
				this.moveAhead();
				break;
			case 'W':
				this.headingTowards = 'E';
				this.moveAhead();
				break;
			case 'E':
				this.headingTowards = 'W';
				this.moveAhead();
				break;
		}
		printCoordinate();
		//change current battery level
	}


	// Check for "cleanliness" of current surface. Clean if need be and update capacity
	public void cleanSurface(Cell currentCell){
		SurfaceType surfaceCleaned = currentCell.getSurface();

		// Cell is currently clean. No need to do anything
		if(currentCell.getDirtAmount() <= 0){
			return;
		}

		// Cell is not clean, clean it, update bag, and change cell state
		Integer spaceLeft = MAX_DIRT_CAPACITY - getCurrentBagSize();
		// Check for space
		if(spaceLeft <= 0 || atCapacity){
			// Can't hold any more. Do not clean cell
			return;
		}
		else{
			// Add to vaccumbag
			currentCell.decreaseDirt();
			vacuumBag.add(
					new Pair<Integer, SurfaceType>(1, currentCell.getSurface()));
			checkBagSize();
		}
	}

	private void checkBagSize(){
		assert(getCurrentBagSize() <= MAX_DIRT_CAPACITY);
		atCapacity = (getCurrentBagSize() == MAX_DIRT_CAPACITY);
		// todo - replace println statements with ui calls
		if(atCapacity){
			System.out.println("The Clean Sweep is out of space for dirt!");
		}
		else if(getCurrentBagSize() >= 35){
			System.out.println("The Clean Sweep's current bag size is: " + getCurrentBagSize());
		}
	}

	public boolean isAtCapacity() {
		return atCapacity;
	}

	public Integer getCurrentBagSize(){
		return vacuumBag.stream().mapToInt(record -> {
			return record.getValue0();
		}).sum();
	}

	private String printCoordinate() {
		return "My coordinate is " + this.currCell.getCoordinateX() +", "+ this.currCell.getCoordinateY();
	}

	public void changeHeading(char h){
		headingTowards = h;
	}
}
