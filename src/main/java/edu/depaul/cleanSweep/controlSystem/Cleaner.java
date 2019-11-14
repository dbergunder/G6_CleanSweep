package edu.depaul.cleanSweep.controlSystem;

import edu.depaul.cleanSweep.diagnostics.PowerConsumptionLog;
import edu.depaul.cleanSweep.floorPlan.*;

import org.javatuples.Pair;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;

public class Cleaner {
	// variables related to battery consumption
	private static final double MAX_BATTERY_POWER = 250;
	private static final int MAX_DIRT_CAPACITY = 50;
	private double currBattery;
	private double lowBatteryThreshold = 35;
	private static PowerConsumptionLog pcl;
	// variables related to dirt capacity
	private int currDirtCapacity;
	private boolean atCapacity;
	private boolean almostAtCapacity; //about dirt size
	private List<Pair<Integer, TileType>> vacuumBag = new LinkedList<Pair<Integer, TileType>>();
	// variables related to movement
	public char headingTowards = 'N';
	private String currStatus = new String("No status yet");
	private FloorTile currNode;
	private FloorTile prevNode = null;
	private ArrayList<FloorTile> chargingStations = new ArrayList<FloorTile>();
	private ArrayList<FloorTile> cleanerHistory = new ArrayList<FloorTile>();
	private Stack<FloorTile> validTilesStack = new Stack<FloorTile>();
	private FloorTile[][] currentMap = new FloorTile[1000][1000];
	private CustomLinkedList sensorMap = null; // the cleaner stores it's own sensor map for checking completion
	private boolean cleaningComplete; //the cleaner can check if the map is complete

	// constructor
	public Cleaner() throws IOException{
		sensorMap = new CustomLinkedList();
		cleaningComplete = false;
		pcl = PowerConsumptionLog.getInstance();
		currBattery = MAX_BATTERY_POWER;
		currDirtCapacity = MAX_DIRT_CAPACITY;
	}
	
	// getters/setters
	public double getCurrBattery() {
		return currBattery;
	}
	
	public void setCurrBattery(double cb) {
		currBattery = cb;
	}

	public void setCurrDirtCapacity(int cdc) {
		currDirtCapacity = cdc;
	}

	public FloorTile getCurrNode() {
		return currNode != null ? currNode : new FloorTile(0, 0);
	}

	public void setCurrNode(FloorTile n) {
		currNode = n;
		this.currentMap[n.get_x()][n.get_y()] = copyFloorTile(n);
		cleanerHistory.add(n);
	}
	
	public CustomLinkedList getSensorMap() {
		//method used to retrieve private sensor map
		return this.sensorMap;
	}
	
	public void setSensorMap(CustomLinkedList testMap) {
		//method used to set a sensor map to the cleaner, useful for setting custom maps in tests
		this.sensorMap = testMap;
		setCurrNode(this.sensorMap.getHead());
	}

	public boolean isAtCapacity() {
		return atCapacity;
	}

	public boolean isAlmostAtCapacity() { 
		return almostAtCapacity; 
	}

	public String getCleanerStatus () {
		return currStatus;
	}
	
	// check if map is completely cleaned and visited
	private boolean checkMapCleaningComplete() {
		// this method checks if the sensor map is the same as the current map and that all nodes are cleaned and visited if possible
		FloorTile currSensorNode = this.sensorMap.getHead(); // get sensor map head
		boolean loopCheck = true; // loop checker to determine if all information is correct
		
		// check for nulls?
		
		while(currSensorNode != null) { // loop through the sensorNodes (Y-Axis)
			if (currSensorNode.get_y()%2 == 0) { // depending of row number move east if even, move west if odd 
				while(currSensorNode.east != null) { // loop through the sensorNodes (X-Axis) east if row is even
					loopCheck = checkNodeCleanAndVisited(currSensorNode);
					if (!loopCheck) {
						break;
					}
					currSensorNode = currSensorNode.east; // move checker east
				}
				loopCheck = checkNodeCleanAndVisited(currSensorNode);
			} else {
				while(currSensorNode.west != null) { // loop through the sensorNodes (X-Axis) west if row is odd
					loopCheck = checkNodeCleanAndVisited(currSensorNode);
					if (!loopCheck) {
						break;
					}
					currSensorNode = currSensorNode.west; // move checker west
				}
				loopCheck = checkNodeCleanAndVisited(currSensorNode);
			}
			
			if (!loopCheck) {
				break;
			}
			
			if (currSensorNode.south != null) { // after looping all the way east/west then move south 1 row if not empty
				currSensorNode = currSensorNode.south;
			} else {
				break;
			}
		}
		
		return loopCheck;
	}

	private boolean checkNodeCleanAndVisited(FloorTile sensor) {
		boolean visitedAndCleaned = false;
		int checkY = sensor.get_y();
		int checkX = sensor.get_x();
		if (sensor.getAccessable()) {
			if (this.sensorMap.returnNode(checkX, checkY).getClean() && cleanerHistory.contains(sensor)) {
				visitedAndCleaned = true;
			}
		}
		
		return visitedAndCleaned;
	}

	// only move, no clean.
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
	
	
	// for navigating away from obsticles
	public void moveToLocation_UsingStack(int targetX, int targetY){
		List<FloorTile> wrongNodes = new ArrayList<FloorTile>();

		while(! (currNode._x == targetX && currNode._y == targetY ) ){
			wrongNodes.add(currNode);
			addValidNodes(wrongNodes);
			teleportToNode(validTilesStack.pop());
		}
	}

	private void teleportToNode(FloorTile node){
		this.prevNode = this.currNode;
		this.currNode = node;

		currentMap[this.currNode._x][this.currNode._y] = copyFloorTile(this.currNode);
		cleanerHistory.add(this.currNode);

		System.out.println("Moving to " + printCoordinate());
		// get average battery cost, log it, and subtract from battery total
		double averagePowerCost = (this.prevNode.getBatteryConsumption() + this.currNode.getBatteryConsumption()) / 2;

		pcl.logPowerUsed("Movement", this.prevNode, this.currNode, this.currBattery, averagePowerCost);
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
			pcl.logPowerUsed("Charging", this.prevNode, this.currNode, this.currBattery, 0);
		}
	}

	private void addValidNodes(List<FloorTile> wrongNodes){
		List<FloorTile> neighboringNodes = getNeighboringNodes(this.currNode);
		for (FloorTile neighbor: neighboringNodes) {
			if(neighbor != null && neighbor.getAccessable() && !wrongNodes.contains(neighbor)){
				validTilesStack.push(neighbor);
			}
		}
	}

	private List<FloorTile> getNeighboringNodes(FloorTile node){
		return new ArrayList<FloorTile>(
				Arrays.asList(new FloorTile[]{node.north, node.south, node.east, node.west}));
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


	// According to the current heading direction, first check if the corresponding side is blocked, and then move to the next cell.
	public boolean moveAhead() {
		boolean flag = false;
		double averagePowerCost;
		this.prevNode = this.currNode;

		switch(this.headingTowards) {
		case 'N':
			if(this.currNode.north != null && this.currNode.north.getAccessable()) {
				this.currNode = this.currNode.north;
				flag = true;
			}
			break;
		case 'S':
			if(this.currNode.south != null && this.currNode.south.getAccessable()) {
				this.currNode = this.currNode.south;
				flag = true;
			}
			break;
		case 'W':
			if(this.currNode.west != null && this.currNode.west.getAccessable()) {
				this.currNode = this.currNode.west;
				flag = true;
			}
			break;
		case 'E':
			if(this.currNode.east != null && this.currNode.east.getAccessable()) {
				this.currNode = this.currNode.east;
				flag = true;
			}
			break;
		}
		
		currentMap[this.currNode._x][this.currNode._y] = copyFloorTile(this.currNode); // copy sensor node to the cleaner's map
		cleanerHistory.add(this.currNode); // add the sensor node to the cleaner's history
		
		System.out.println("Moving to " + printCoordinate());
		
		// get average battery cost, log it, and subtract from battery total
		averagePowerCost = (this.prevNode.getBatteryConsumption() + this.currNode.getBatteryConsumption()) / 2;
		pcl.logPowerUsed("Movement", this.prevNode, this.currNode, this.currBattery, averagePowerCost);
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
			pcl.logPowerUsed("Charging", this.prevNode, this.currNode, this.currBattery, 0);
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

	
	//For moving left, right or back, the heading direction will change correspondingly, and then move forward.
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
	public void cleanSurface() {

		// Cell is currently clean. No need to do anything
		if(this.currNode.getClean()) {
			return;
		}

		// Cell is not clean, clean it, update bag, and change cell state
		Integer spaceLeft = MAX_DIRT_CAPACITY - getCurrentBagSize();
		
		// Check for space
		if(spaceLeft <= 0 || atCapacity) {
			// Can't hold any more. Do not clean cell
			return;
		} else {
			// Add to vaccumbag
			this.currNode.decreaseDirtAmount(); // enforces 1 unit at a time
			System.out.println("Cleaning 1 unit of dirt at " + this.printCoordinate());
			pcl.logPowerUsed("Cleaning", this.currNode, this.currNode, this.currBattery, this.currNode.getBatteryConsumption());
			currBattery -= this.currNode.getBatteryConsumption();

			ifLowBtrGoChargingNBack(this.currBattery); //only check battery when cleaning

			vacuumBag.add(new Pair<Integer, TileType>(1, this.currNode.getSurfaceType()));
			checkBagSize();
		}
		
		// check if the map is completely visited and cleaned, if so move to nearest charging station
		if (checkMapCleaningComplete()) {
			//TODO Code below is correct, do not erase
			FloorTile closest = this.getClosestCharging();
			if (closest != null) {
				int x = closest.get_x();
				int y = closest.get_y();
				System.out.println("***************");
				System.out.println("CLEANING IS COMPLETE.");
				System.out.println("***************");
				System.out.printf("The closest charging station locates in (%d, %d).\n", x, y);
				int[] dest = new int[] {this.headingTowards, closest.get_x(), closest.get_y()};
				
				move2ALocation(dest);
			}
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

	public Integer getCurrentBagSize(){
		return vacuumBag.stream().mapToInt(record -> {
			return record.getValue0();
		}).sum();
	}

	public String printCoordinate() {
		return String.format("( %d , %d )", this.currNode._y ,this.currNode._x);
	}

	public void changeHeading(char h){
		headingTowards = h;
	}

	public char getHeading() {
		return this.headingTowards;
	}
	public ArrayList<FloorTile> getCleanerHistory() {
		return cleanerHistory;
	}

	public FloorTile[][] getCurrentMap() {
		return currentMap;
	}

	public String getCurrentMapString(){
		String s = "";
		for(int i = 0; i<=8; i++){
			for(int j = 0; j<=5; j++){
				if(currentMap[i][j] != null)
					s += String.format("( %d , %d )", currentMap[i][j]._x ,currentMap[i][j]._y);
				else
					s += "(   ,   )";
			}
			s += "\n";
		}
		return s;
	}

	private FloorTile copyFloorTile(FloorTile tile) {
		FloorTile temp = new FloorTile(tile._y, tile._x, tile.getUnitsOfDirt(), tile.getSurfaceType());
		return temp;
	}
}