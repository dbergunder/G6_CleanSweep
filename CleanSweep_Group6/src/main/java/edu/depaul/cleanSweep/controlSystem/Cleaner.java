package edu.depaul.cleanSweep.controlSystem;

import edu.depaul.cleanSweep.cell.CellNode;
import edu.depaul.cleanSweep.cell.SideType;

public class Cleaner {

	private static final int MAX_BATTERY_POWER = 250;
	private static final int MAX_DIRT_CAPACITY = 50; 
	//a random number I chose. cannot find a specific number in the instruction

	private int currBattery;
	private int currDirtCapacity;
	private CellNode currCell;
	private char headingTowards = 'N';


	public Cleaner(int battery, int dirtCapacity, CellNode node) {
		currBattery = battery;
		currDirtCapacity = dirtCapacity;
		currCell = node;
	}

	public void changeHeading(char h) {
		headingTowards = h;
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

	private String printCoordinate() {
		return "My coordinate is " + this.currCell.getCoordinateX() +", "+ this.currCell.getCoordinateY();
	}
	
}
