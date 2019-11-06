package edu.depaul.cleanSweep.controlSystem;

import edu.depaul.cleanSweep.cell.SurfaceType;
import edu.depaul.cleanSweep.diagnostics.PowerConsumptionLog;
import edu.depaul.cleanSweep.floorPlan.*;

import org.javatuples.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cleaner {

	private static final double MAX_BATTERY_POWER = 250;
	private static final int MAX_DIRT_CAPACITY = 50;

	private double currBattery;
	private double lowBatteryThreshold = 35;

	private int currDirtCapacity;
	private boolean atCapacity;
	private boolean almostAtCapacity; //about dirt size

	public char headingTowards = 'N';
	private String currStatus = new String("No status yet");
	private static PowerConsumptionLog pcl;
	private FloorTile currNode;
	private FloorTile prevNode = null;

	private ArrayList<FloorTile> chargingStations = new ArrayList<FloorTile>();

	// The vacuumbag is a list, with each node representing a "cleaning" of a tile
	// Each clean appends a Pair representing the amount of dirt cleaned, as well the surface type
	// In order to traverse through the history, start at the head, and work downward
	private List<Pair<Integer, TileType>> vacuumBag = new LinkedList<Pair<Integer, TileType>>();

	private ArrayList<FloorTile> cleanerHistory = new ArrayList<FloorTile>();

	// Todo - add better methods to custom linked list to allow for more dynamic insertion, searching, and deletion
	private FloorTile[][] currentMap = new FloorTile[1000][1000];

	public Cleaner() throws IOException{
		pcl = PowerConsumptionLog.getInstance();
		currBattery = MAX_BATTERY_POWER;
		currDirtCapacity = MAX_DIRT_CAPACITY;
	}

	public Cleaner(double battery, int dirtCapacity, FloorTile node) {
		currBattery = battery;
		currDirtCapacity = dirtCapacity;
		currNode = node;
	}

	public void setCurrBattery(double cb) {
		currBattery = cb;
	}

	public void setCurrDirtCapacity(int cdc) {
		currDirtCapacity = cdc;
	}

	public double getCurrBattery() {
		return currBattery;
	}

	public void setCurrNode(FloorTile n) {
		currNode = n;
		currentMap[n._x][n._y] = copyFloorTile(n);
		cleanerHistory.add(copyFloorTile(currNode));
	}


	public FloorTile getCurrNode() {
		return currNode != null ? currNode : new FloorTile(0, 0);
	}

	//only move, no clean.
	public int[] move2ALocation(int[] dest) {
		FloorTile locationLeft = this.getCurrNode();
		int ax = locationLeft.get_x();
		int ay = locationLeft.get_y();
		char ch = this.headingTowards;
		
		int bx = dest[1];
		int by = dest[2];

		if(this.getCurrNode().get_x() < bx) {
			this.changeHeading('E');
		}
		if(this.getCurrNode().get_x() > bx) {
			this.changeHeading('W');
		}
		while(this.getCurrNode().get_x() != bx) {
			moveAhead();
		}

		if(this.getCurrNode().get_y() < by) {
			this.changeHeading('S');
		}
		if(this.getCurrNode().get_y() > by) {
			this.changeHeading('N');
		}
		while(this.getCurrNode().get_y() != by) {
			moveAhead();
		}
		this.changeHeading((char) dest[0]);
		//System.out.println("^^^^^^^^^heading"+(char) dest[0]); //for test
		return new int[] {ch, ax, ay};
	}




	public FloorTile getClosestCharging() {
		int x = this.getCurrNode().get_x();
		int y = this.getCurrNode().get_y();
		FloorTile closestCharging = chargingStations.get(0);
		double min = getDistance(x, y, chargingStations.get(0));
		for(int i = 1; i < chargingStations.size(); i++) {
			double b = getDistance(x, y, chargingStations.get(i));
			if(b < min) {
				closestCharging = chargingStations.get(i);
				min = b;
			}
		}
		return closestCharging;
	}

	private double getDistance(int x, int y, FloorTile ft) {
		int bx = ft.get_x();
		int by = ft.get_y();
		return Math.sqrt((x-bx)*(x-bx)+(y-by)*(y-by));
	}



	public void fillChargingStations(CustomLinkedList path) {
		chargingStations = path.getChargingList();
	}


	/*
	 * According to the current heading direction, first check if the corresponding side is blocked,
	 * and then move to the next cell.
	 */
	public boolean moveAhead() {
		boolean flag = false;
		double averagePowerCost;

		switch(this.headingTowards) {
		case 'N':
			if(this.currNode.north != null && this.currNode.north.getAccessable()) {
				this.prevNode = currNode;
				this.currNode = currNode.north;
				currentMap[currNode._x][currNode._y] = copyFloorTile(currNode);
				cleanerHistory.add(copyFloorTile(currNode));
				flag = true;
			}
			break;
		case 'S':
			if(this.currNode.south != null && this.currNode.south.getAccessable()) {
				this.prevNode = currNode;
				this.currNode = currNode.south;
				currentMap[currNode._x][currNode._y] = copyFloorTile(currNode);
				cleanerHistory.add(copyFloorTile(currNode));
				flag = true;
			}
			break;
		case 'W':
			if(this.currNode.west != null && this.currNode.west.getAccessable()) {
				this.prevNode = currNode;
				this.currNode = currNode.west;
				currentMap[currNode._x][currNode._y] = copyFloorTile(currNode);
				cleanerHistory.add(copyFloorTile(currNode));
				flag = true;
			}
			break;
		case 'E':
			if(this.currNode.east != null && this.currNode.east.getAccessable()) {
				this.prevNode = currNode;
				this.currNode = currNode.east;
				currentMap[currNode._x][currNode._y] = copyFloorTile(currNode);
				cleanerHistory.add(copyFloorTile(currNode));
				flag = true;
			}
			break;
		}
		System.out.println("Moving to " + printCoordinate());
		// get average battery cost, log it, and subtract from battery total
		averagePowerCost = (this.prevNode.getBatteryConsumption() + this.currNode.getBatteryConsumption()) / 2;

		pcl.logPowerUsed("Movement", prevNode, currNode, currBattery, averagePowerCost);
		this.currBattery -= averagePowerCost;


		if (this.currNode.getChargeStation()) {
			this.currBattery = MAX_BATTERY_POWER;
			System.out.println("************************");
			System.out.println("Arrive charging station.");
			System.out.println("************************");
			System.out.println("........CHARGING........");
			System.out.println("************************");
			System.out.println("The battery is full.");
			System.out.println("************************");
			pcl.logPowerUsed("Charging", prevNode, currNode, currBattery, 0);
		}


		return flag;
	}

	public void ifLowBtrGoChargingNBack(double currBattery) {
		if(currBattery <= this.lowBatteryThreshold) {
			FloorTile closest = this.getClosestCharging();
			int x = closest.get_x();
			int y = closest.get_y();
			System.out.println("***************");
			System.out.println("Battery is low.");
			System.out.println("***************");
			System.out.printf("The closest charging station locates in (%d, %d).\n", x, y);
			int[] dest = new int[] {this.headingTowards, closest.get_x(), closest.get_y()};
			
			int[] abc = move2ALocation(dest);
			
			move2ALocation(abc);
		}
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
	public void cleanSurface(FloorTile currentTile) {
		TileType surfacedCleaned = currentTile.getSurfaceType();

		// Cell is currently clean. No need to do anything
		if(currentTile.getClean() == true) {
			return;
		}

		// Cell is not clean, clean it, update bag, and change cell state
		Integer spaceLeft = MAX_DIRT_CAPACITY - getCurrentBagSize();
		// Check for space
		if(spaceLeft <= 0 || atCapacity) {
			// Can't hold any more. Do not clean cell
			return;
		}
		else {
			// Add to vaccumbag
			currentTile.decreaseDirtAmount(); //enforces 1 unit at a time
			System.out.println("Cleaning 1 unit of dirt at " + this.printCoordinate());
			pcl.logPowerUsed("Cleaning", currentTile, currentTile, currBattery, currentTile.getBatteryConsumption());
			currBattery -= currentTile.getBatteryConsumption();

			ifLowBtrGoChargingNBack(this.currBattery); //only check battery when cleaning

			vacuumBag.add(new Pair<Integer, TileType>(1, currentTile.getSurfaceType()));
			checkBagSize();
		}
	}

	private void checkBagSize() {
		assert(getCurrentBagSize() <= MAX_DIRT_CAPACITY);
		atCapacity = (getCurrentBagSize() == MAX_DIRT_CAPACITY);
		almostAtCapacity = (getCurrentBagSize() >= 35);
		if(atCapacity) {
			currStatus = "The Clean Sweep is out of space for dirt!";
		}
		else if(almostAtCapacity) {
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
		return String.format("( %d , %d )", this.currNode._x ,this.currNode._y);
	}

	public void changeHeading(char h){
		headingTowards = h;
	}

	public char getHeading() {return this.headingTowards;}

	public double getBatteryPower() {
		return currBattery;
	}

	public ArrayList<FloorTile> getCleanerHistory() {
		return cleanerHistory;
	}

	public FloorTile[][] getCurrentMap() {
		return currentMap;
	}

	private FloorTile copyFloorTile(FloorTile tile) {
		FloorTile temp = new FloorTile(tile._y, tile._x, tile.getUnitsOfDirt(), tile.getSurfaceType());
		return temp;
	}
}