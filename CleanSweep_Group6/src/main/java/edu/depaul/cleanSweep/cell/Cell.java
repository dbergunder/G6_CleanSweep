package edu.depaul.cleanSweep.cell;

public class Cell {

	public SideType sideN;
	public SideType sideS;
	public SideType sideW;
	public SideType sideE;
	
	public SurfaceType surface;
	public int dirtAmount;

	public Cell(Integer dirtAmount, SurfaceType surfaceType){
		this.dirtAmount = dirtAmount;
		this.surface = surfaceType;
	}

}
