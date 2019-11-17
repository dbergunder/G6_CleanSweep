package edu.depaul.cleanSweep.cell;

public enum SideType {
  FLOORCELL(0),
  OPENDOOR(1),
  CLOSEDOOR(2),
  WALL(3),
  STAIRS(4);

  private int abbr;

  SideType(int abb) {
    abbr = abb;
  }

  public int getAbbr() {
    return abbr;
  }
}
