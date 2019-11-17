package edu.depaul.cleanSweep.cell;

import edu.depaul.cleanSweep.floorPlan.TileType;

public class Cell {

  public SideType sideN;
  public SideType sideS;
  public SideType sideW;
  public SideType sideE;

  private TileType surface;
  private int dirtAmount;

  public Cell(Integer dirtAmount, TileType surfaceType) {
    this.dirtAmount = dirtAmount;
    this.surface = surfaceType;
  }

  public int getDirtAmount() {
    return dirtAmount;
  }

  public TileType getSurface() {
    return surface;
  }

  public void decreaseDirt() {
    this.dirtAmount--;
  }
}
