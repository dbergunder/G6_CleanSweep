package edu.depaul.cleanSweep.cell;

public class CellNode {

  private SideType sideN;
  private SideType sideS;
  private SideType sideW;
  private SideType sideE;

  private CellNode nodeN;
  private CellNode nodeS;
  private CellNode nodeW;
  private CellNode nodeE;

  private SurfaceType surface;
  private int dirtAmount;
  private int[] coordinate = new int[2];

  public CellNode(
      int x, int y, SurfaceType sur, int dirt, SideType n, SideType s, SideType w, SideType e) {
    coordinate[0] = x;
    coordinate[1] = y;
    surface = sur;
    dirtAmount = dirt;
    sideN = n;
    sideS = s;
    sideW = w;
    sideE = e;
  }

  /*
   * get (x, y)
   */
  public int getCoordinateX() {
    return coordinate[0];
  }

  public int getCoordinateY() {
    return coordinate[1];
  }

  /*
   * set adjacent Nodes
   */
  public void setNodeN(CellNode nodeN) {
    this.nodeN = nodeN;
  }

  public void setNodeS(CellNode nodeS) {
    this.nodeS = nodeS;
  }

  public void setNodeW(CellNode nodeW) {
    this.nodeW = nodeW;
  }

  public void setNodeE(CellNode nodeE) {
    this.nodeE = nodeE;
  }

  /*
   * get types of one of four sides
   */
  public SideType getSideN() {
    return sideN;
  }

  public SideType getSideS() {
    return sideS;
  }

  public SideType getSideW() {
    return sideW;
  }

  public SideType getSideE() {
    return sideE;
  }

  /*
   * get adjacent Nodes
   */
  public CellNode getCellN() {
    return nodeN;
  }

  public CellNode getCellS() {
    return nodeS;
  }

  public CellNode getCellW() {
    return nodeW;
  }

  public CellNode getCellE() {
    return nodeE;
  }
}
