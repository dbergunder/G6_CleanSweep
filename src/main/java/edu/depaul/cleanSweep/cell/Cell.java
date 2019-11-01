package edu.depaul.cleanSweep.cell;

public class Cell {

	public SideType sideN;
	public SideType sideS;
	public SideType sideW;
	public SideType sideE;
	
	private SurfaceType surface;
	private int dirtAmount;

	public Cell(Integer dirtAmount, SurfaceType surfaceType){
		this.dirtAmount = dirtAmount;
		this.surface = surfaceType;
	}

	public int getDirtAmount() {
		return dirtAmount;
	}

	public SurfaceType getSurface() {
		return surface;
	}

	public void decreaseDirt(){
		this.dirtAmount--;
	}
}
