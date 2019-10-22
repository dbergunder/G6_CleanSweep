package edu.depaul.cleanSweep.controlSystem;

import edu.depaul.cleanSweep.cell.CellNode;
import edu.depaul.cleanSweep.cell.SideType;
import edu.depaul.cleanSweep.cell.Cell;
import edu.depaul.cleanSweep.cell.SurfaceType;
import edu.depaul.cleanSweep.floorPlan.FloorTile;

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
	private boolean almostAtCapacity;
	private String currStatus = new String("No status yet");

	private FloorTile currNode;
	private char headingTowards = 'N';

	// The vacuumbag is a list, with each node representing a "cleaning" of a tile
	// Each clean appends a Pair representing the amount of dirt cleaned, as well the surface type
	// In order to traverse through the history, start at the head, and work downward
	private List<Pair<Integer, SurfaceType>> vacuumBag = new LinkedList<Pair<Integer, SurfaceType>>();

	public Cleaner(){

	}

	public Cleaner(int battery, int dirtCapacity, FloorTile node) {
		currBattery = battery;
		currDirtCapacity = dirtCapacity;
		currNode = node;
	}
	
	public void setCurrNode(FloorTile n) {
		currNode = n;
	}

	
	public FloorTile getCurrNode() {
		return currNode;
	}
	
	
	/*
	 * According to the current heading direction, first check if the corresponding side is blocked,
	 * and then move to the next cell.
	 */
	public boolean moveAhead() {
		boolean flag = false;
		switch(this.headingTowards) {
			case 'N':
				if(this.currNode.north != null && this.currNode.north.getAccessable()) {
					this.currNode = currNode.north;
					flag = true;
				}
				break;
			case 'S':
				if(this.currNode.south != null && this.currNode.south.getAccessable()) {
					this.currNode = currNode.south;
					flag = true;
				}
				break;
			case 'W':
				if(this.currNode.west != null && this.currNode.west.getAccessable()) {
					this.currNode = currNode.west;
					flag = true;
				}
				break;
			case 'E':
				if(this.currNode.east != null && this.currNode.east.getAccessable()) {
					this.currNode = currNode.east;
					flag = true;
				}
				break;
		}
		System.out.println(printCoordinate());
		return flag;
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
		almostAtCapacity = (getCurrentBagSize() >= 35);
		if(atCapacity){
			currStatus = "The Clean Sweep is out of space for dirt!";
		}
		else if(almostAtCapacity){
			currStatus = "The Clean Sweep's current bag size is: " + getCurrentBagSize();
		}
	}

	public boolean isAtCapacity() {
		return atCapacity;
	}

	public boolean isAlmostAtCapacity() { return almostAtCapacity; }
	
	public String getCleanerStatus () {
		return currStatus;
	}
	public Integer getCurrentBagSize(){
		return vacuumBag.stream().mapToInt(record -> {
			return record.getValue0();
		}).sum();
	}

	public String printCoordinate() {
		return "My coordinate is " + this.currNode._x +", "+ this.currNode._y;
	}

	public void changeHeading(char h){
		headingTowards = h;
	}
}