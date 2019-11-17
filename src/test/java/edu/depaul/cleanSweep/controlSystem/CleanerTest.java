package edu.depaul.cleanSweep.controlSystem;

import edu.depaul.cleanSweep.floorPlan.CustomLinkedList;
import edu.depaul.cleanSweep.floorPlan.FloorTile;
import edu.depaul.cleanSweep.floorPlan.TileType;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CleanerTest {

  private static final int MAX_DIRT_CAPACITY = 50;

  @Test
  void DirtIntake_DoesNotMaxCapacity() throws IOException {

    CustomLinkedList test = new CustomLinkedList();
    test.insertTile(0, 0, 10, true, false, 2);

    Cleaner cleaner = new Cleaner();
    cleaner.setSensorMap(test); // adjust only for the new added print{} in cleanSerface().

    assertEquals(10, cleaner.getCurrNode().getUnitsOfDirt());
    assertEquals(0, cleaner.getCurrentBagSize());

    cleaner.cleanSurface();

    assertEquals(9, cleaner.getCurrNode().getUnitsOfDirt());
    assertEquals(1, cleaner.getCurrentBagSize());
  }

  @Test
  void DirtIntake_PerfectlyMaxCapacity() throws IOException {
    CustomLinkedList test = new CustomLinkedList();
    test.insertTile(0, 0, 10, true, false, 2);

    Cleaner cleaner = new Cleaner();

    // add 50 nodes
    for (int i : IntStream.range(1, 51).boxed().collect(Collectors.toList())) {
      test.insertTile(0, i, 10, true, false, 1);
    }

    cleaner.setSensorMap(test);

    // move 50 nodes and clean each once
    for (int i : IntStream.range(1, 51).boxed().collect(Collectors.toList())) {
      cleaner.changeHeading('E');
      cleaner.moveAhead();
      cleaner.cleanSurface();
      assertEquals(9, cleaner.getCurrNode().getUnitsOfDirt());
      assertEquals(i, cleaner.getCurrentBagSize());
    }

    assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

    cleaner.setCurrNode(new FloorTile(0, 0, 12, TileType.LOWPILE));

    cleaner.cleanSurface();

    // Assert bag has not grown
    assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

    // Assert the cell was NOT cleaned
    assertEquals(12, cleaner.getCurrNode().getUnitsOfDirt());
  }

  // todo - remove unit test once/if ui is written
  @Test
  void CapacityNotifications_NotifiesWhenFull() throws IOException {
    CustomLinkedList test = new CustomLinkedList();
    test.insertTile(0, 0, 10, true, false, 2);

    Cleaner cleaner = new Cleaner();

    // add 50 nodes
    for (int i : IntStream.range(1, 51).boxed().collect(Collectors.toList())) {
      test.insertTile(0, i, 10, true, false, 1);
    }

    cleaner.setSensorMap(test);

    // move 50 nodes and clean each once
    for (int i : IntStream.range(1, 51).boxed().collect(Collectors.toList())) {
      cleaner.changeHeading('E');
      cleaner.moveAhead();
      cleaner.cleanSurface();
      assertEquals(9, cleaner.getCurrNode().getUnitsOfDirt());
      assertEquals(i, cleaner.getCurrentBagSize());
    }

    String string1 = new String("The Clean Sweep is out of space for dirt!");
    String string2 = cleaner.getCleanerStatus();
    assertEquals(string1, string2);
  }

  @Test
  void OneCellAtATimeTest() throws IOException {

    System.out.println("\nOneCellAtATimeTest");

    CustomLinkedList floorPlan = new CustomLinkedList();
    floorPlan.insert(0, 0);
    floorPlan.insert(0, 1);
    floorPlan.insert(1, 0);
    floorPlan.insert(1, 1);
    floorPlan.returnNode(0, 0).setAccessable(true);
    floorPlan.returnNode(0, 1).setAccessable(true);
    floorPlan.returnNode(1, 0).setAccessable(true);
    floorPlan.returnNode(1, 1).setAccessable(true);

    Cleaner cleaner = new Cleaner();
    cleaner.setCurrNode(floorPlan.returnNode(0, 0));
    System.out.println("origin location\n" + cleaner.printCoordinate());
    cleaner.changeHeading('E');
    assertEquals('E', cleaner.getHeading());
    System.out.println("make a move");
    cleaner.moveAhead();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
  }

  @Test
  public void MoveIn4AxesTest() throws IOException {
    System.out.println("\nMoveIn4AxesTest");

    CustomLinkedList test = new CustomLinkedList();
    test.insert(0, 0);
    test.insert(0, 1);
    test.insert(1, 0);
    test.insert(1, 1);
    test.returnNode(0, 0).setAccessable(true);
    test.returnNode(0, 1).setAccessable(true);
    test.returnNode(1, 0).setAccessable(true);
    test.returnNode(1, 1).setAccessable(true);

    Cleaner cleaner = new Cleaner();

    // move ahead
    cleaner.setCurrNode(test.getHead());
    cleaner.changeHeading('E');
    assertEquals('E', cleaner.getHeading());
    System.out.println("cleaner is towards east");
    System.out.println("current location\n" + cleaner.printCoordinate());
    assert (cleaner.getCurrNode()._x == 0);
    assert (cleaner.getCurrNode()._y == 0);

    System.out.println("\nturn ahead");
    cleaner.moveAhead();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    cleaner.printCoordinate();

    // move backward
    System.out.println("\nturn back");
    cleaner.moveBack();
    assertEquals(0, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    cleaner.printCoordinate();

    // move left
    System.out.println("\nturn left");
    cleaner.moveLeft();
    assertEquals(0, cleaner.getCurrNode().get_x());
    assertEquals(1, cleaner.getCurrNode().get_y());
    cleaner.printCoordinate();

    // move right
    cleaner.changeHeading('N');
    assertEquals('N', cleaner.getHeading());
    System.out.println("\ncleaner is towards north");
    System.out.println("turn right");
    cleaner.moveRight();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(1, cleaner.getCurrNode().get_y());
    cleaner.printCoordinate();
  }

  @Test
  void CleanerHistory_AccuratelyReportsHistory()
      throws IOException, SAXException, ParserConfigurationException {
    Cleaner cleaner = new Cleaner();

    CustomLinkedList floorPlan = new CustomLinkedList();

    floorPlan.convertXMLToCustomLinkedList(new File("files/SamplePlan.xml"));

    cleaner.setCurrNode(floorPlan.returnNode(0, 0));

    cleaner.changeHeading('S');

    // Assert cleaner is at the origin
    assertEquals(0, cleaner.getCurrNode()._x);
    assertEquals(0, cleaner.getCurrNode()._y);

    cleaner.moveAhead();

    // Assert cleaner x hasnt change, but the y has
    assertEquals(0, getFloorTile(cleaner, 1)._x);
    assertEquals(1, getFloorTile(cleaner, 1)._y);

    cleaner.moveAhead();

    assertEquals(0, getFloorTile(cleaner, 2)._x);
    assertEquals(2, getFloorTile(cleaner, 2)._y);

    assertEquals(3, cleaner.getCleanerHistory().size());
  }

  @Test
  void CleanerInteriorMap_AccuratlyKeepsTrackofVisitedLocations()
      throws IOException, SAXException, ParserConfigurationException {
    Cleaner cleaner = new Cleaner();

    CustomLinkedList floorPlan = new CustomLinkedList();

    floorPlan.convertXMLToCustomLinkedList(new File("files/SamplePlanWithAttributes.xml"));

    cleaner.setCurrNode(floorPlan.returnNode(0, 0));

    cleaner.changeHeading('S');
    cleaner.moveAhead();

    // check that the cleaner has a history of being at x: 0 y: 1
    assertNotNull(cleaner.getCurrentMap()[0][1]);
    cleaner.moveAhead();

    // check that the cleaner has a history of being at x: 0 y: 2
    assertNotNull(cleaner.getCurrentMap()[0][2]);

    cleaner.changeHeading('E');
    cleaner.moveAhead();
    cleaner.moveAhead();

    // check that the cleaner has a history of being at x: 2 y: 2
    assertNotNull(cleaner.getCurrentMap()[2][2]);

    cleaner.changeHeading('N');
    cleaner.moveAhead();
    cleaner.moveAhead();

    // check that the cleaner has a history of being at x: 2 y: 0
    assertNotNull(cleaner.getCurrentMap()[2][0]);
  }

  private FloorTile getFloorTile(Cleaner cleaner, int index) {
    return (FloorTile) cleaner.getCleanerHistory().toArray()[index];
  }

  @Test
  void CleanerMovementOnAllFloorResultsInBatteryConsumptionTest() throws IOException {
    System.out.println("\nCleanerMovementOnAllFloorResultsInBatteryConsumptionTest");

    CustomLinkedList test = new CustomLinkedList();
    test.insertTile(0, 0, 1, true, false, 1);
    test.insertTile(0, 1, 1, true, false, 2);
    test.insertTile(1, 0, 1, true, false, 3);
    test.insertTile(1, 1, 1, true, false, 2);

    Cleaner cleaner = new Cleaner();

    // move ahead
    cleaner.setCurrNode(test.getHead());
    cleaner.changeHeading('E');
    System.out.println("cleaner is towards east");
    System.out.println("current location\n" + cleaner.printCoordinate());
    assert (cleaner.getCurrNode()._x == 0);
    assert (cleaner.getCurrNode()._y == 0);
    assertEquals(250.00, cleaner.getCurrBattery());

    System.out.println("\nturn ahead");
    cleaner.moveAhead();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    assertEquals(248.50, cleaner.getCurrBattery());
    cleaner.printCoordinate();

    // move backward
    System.out.println("\nturn back");
    cleaner.moveBack();
    assertEquals(0, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    assertEquals(247.00, cleaner.getCurrBattery());
    cleaner.printCoordinate();

    // move left
    System.out.println("\nturn left");
    cleaner.moveLeft();
    assertEquals(0, cleaner.getCurrNode().get_x());
    assertEquals(1, cleaner.getCurrNode().get_y());
    assertEquals(245.00, cleaner.getCurrBattery());
    cleaner.printCoordinate();

    // move right
    cleaner.changeHeading('N');
    assertEquals('N', cleaner.getHeading());
    System.out.println("\ncleaner is towards north");
    System.out.println("turn right");
    cleaner.moveRight();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(1, cleaner.getCurrNode().get_y());
    assertEquals(242.50, cleaner.getCurrBattery());
    cleaner.printCoordinate();
  }

  @Test
  void CleanerCleaningResultsInBatteryConsumptionTest() throws IOException {
    System.out.println("\nCleanerCleaningResultsInBatteryConsumptionTest");

    CustomLinkedList test = new CustomLinkedList();
    test.insertTile(0, 0, 5, true, false, 1);
    test.insertTile(0, 1, 5, true, false, 2);
    test.insertTile(1, 0, 5, true, false, 3);
    test.insertTile(1, 1, 5, true, false, 2);

    Cleaner cleaner = new Cleaner();

    // move ahead
    cleaner.setSensorMap(test);
    cleaner.changeHeading('E');
    System.out.println("cleaner is towards east");
    System.out.println("current location\n" + cleaner.printCoordinate());
    assert (cleaner.getCurrNode()._x == 0);
    assert (cleaner.getCurrNode()._y == 0);
    assertEquals(250.00, cleaner.getCurrBattery());
    cleaner.cleanSurface();
    assertEquals(249.00, cleaner.getCurrBattery());

    System.out.println("\nturn ahead");
    cleaner.moveAhead();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    assertEquals(247.50, cleaner.getCurrBattery());
    cleaner.cleanSurface();
    assertEquals(245.50, cleaner.getCurrBattery());
    cleaner.printCoordinate();

    // move backward
    System.out.println("\nturn back");
    cleaner.moveBack();
    assertEquals(0, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    assertEquals(244.00, cleaner.getCurrBattery());
    cleaner.cleanSurface();
    assertEquals(243.00, cleaner.getCurrBattery());
    cleaner.printCoordinate();

    // move left
    System.out.println("\nturn left");
    cleaner.moveLeft();
    assertEquals(0, cleaner.getCurrNode().get_x());
    assertEquals(1, cleaner.getCurrNode().get_y());
    assertEquals(241.00, cleaner.getCurrBattery());
    cleaner.cleanSurface();
    assertEquals(238.00, cleaner.getCurrBattery());
    cleaner.printCoordinate();

    // move right
    cleaner.changeHeading('N');
    assertEquals('N', cleaner.getHeading());
    System.out.println("\ncleaner is towards north");
    System.out.println("turn right");
    cleaner.moveRight();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(1, cleaner.getCurrNode().get_y());
    assertEquals(235.50, cleaner.getCurrBattery());
    cleaner.cleanSurface();
    assertEquals(233.50, cleaner.getCurrBattery());
    cleaner.printCoordinate();
  }

  @Test
  void CleanerMovingToChargingStationStationResultsInBatteryRefillTest() throws IOException {
    System.out.println("\nCleanerMovingToChargingStationStationResultsInBatteryRefillTest");

    CustomLinkedList test = new CustomLinkedList();
    test.insertTile(0, 0, 1, true, false, 1);
    test.insertTile(0, 1, 1, true, false, 2);
    test.insertTile(1, 0, 1, true, false, 3);
    test.insertTile(1, 1, 0, true, true, 2);

    Cleaner cleaner = new Cleaner();

    // move ahead
    cleaner.setSensorMap(test);
    cleaner.changeHeading('E');
    System.out.println("cleaner is towards east");
    System.out.println("current location\n" + cleaner.printCoordinate());
    assert (cleaner.getCurrNode()._x == 0);
    assert (cleaner.getCurrNode()._y == 0);
    assertEquals(250.00, cleaner.getCurrBattery());
    cleaner.cleanSurface();
    assertEquals(249.00, cleaner.getCurrBattery());

    System.out.println("\nturn ahead");
    cleaner.moveAhead();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    assertEquals(247.50, cleaner.getCurrBattery());
    cleaner.cleanSurface();
    assertEquals(245.50, cleaner.getCurrBattery());
    cleaner.printCoordinate();

    // move backward
    System.out.println("\nturn back");
    cleaner.moveBack();
    assertEquals(0, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    assertEquals(244.00, cleaner.getCurrBattery());
    cleaner.cleanSurface();
    assertEquals(244.00, cleaner.getCurrBattery());
    cleaner.printCoordinate();

    // move left
    System.out.println("\nturn left");
    cleaner.moveLeft();
    assertEquals(0, cleaner.getCurrNode().get_x());
    assertEquals(1, cleaner.getCurrNode().get_y());
    assertEquals(242.00, cleaner.getCurrBattery());
    cleaner.cleanSurface();
    assertEquals(239.00, cleaner.getCurrBattery());
    cleaner.printCoordinate();

    // move right
    cleaner.changeHeading('N');
    assertEquals('N', cleaner.getHeading());
    System.out.println("\ncleaner is towards north");
    System.out.println("turn right");
    cleaner.moveRight();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(1, cleaner.getCurrNode().get_y());
    assertEquals(250.00, cleaner.getCurrBattery());
    cleaner.printCoordinate();
  }

  /*
   * create a grid with x=5 * y = 4 .
   * According to CustomLinkedList, the 0 of y axis starts at North.
   * set the location of cleaner as (2, 2).
   * set (0, 0), (1, 1), (0, 3) as charging stations.
   * the cleaner should calculate that (1, 1) is the closest one.
   */
  @Test
  void getClosestChargingTest() throws IOException {
    System.out.println("\nTest if the cleaner can find the closest charging station.");

    CustomLinkedList test = new CustomLinkedList();
    // Note: the 1st argument of insetTile() is y.
    test.insertTile(0, 0, 1, true, true, 1);
    test.insertTile(0, 1, 1, true, false, 1);
    test.insertTile(0, 2, 2, true, false, 1);
    test.insertTile(0, 3, 1, true, false, 1);
    test.insertTile(0, 4, 1, true, false, 1);

    test.insertTile(1, 0, 1, true, false, 1);
    test.insertTile(1, 1, 0, true, true, 1);
    test.insertTile(1, 2, 1, true, false, 1);
    test.insertTile(1, 3, 2, true, false, 1);
    test.insertTile(1, 4, 2, true, false, 1);

    test.insertTile(2, 0, 1, true, false, 1);
    test.insertTile(2, 1, 1, true, false, 1);
    test.insertTile(2, 2, 3, true, false, 1);
    test.insertTile(2, 3, 1, true, false, 1);
    test.insertTile(2, 4, 1, true, false, 1);

    test.insertTile(3, 0, 1, true, true, 1);
    test.insertTile(3, 1, 1, true, false, 1);
    test.insertTile(3, 2, 1, true, false, 1);
    test.insertTile(3, 3, 2, true, false, 1);
    test.insertTile(3, 4, 2, true, false, 1);

    assertTrue(test.getChargingList().contains(test.returnNode(0, 0)));
    assertTrue(test.getChargingList().contains(test.returnNode(1, 1)));
    assertTrue(test.getChargingList().contains(test.returnNode(0, 3)));
    assertTrue(test.getChargingList().size() == 3);

    Cleaner cleaner = new Cleaner();
    cleaner.fillChargingStations(test);
    cleaner.setCurrNode(test.returnNode(2, 2));
    FloorTile cc = cleaner.getClosestCharging();
    System.out.printf("The closest charging station is (%d, %d).", cc.get_x(), cc.get_y());
    assertEquals(test.returnNode(1, 1), cc);
  }

  @Test
  void getTheMinPathTest() throws IOException {
    System.out.println("\nTest the map to a spacific position when obstacles exit");

    CustomLinkedList test = new CustomLinkedList();
    // Note: the 1st argument of insetTile() is y.
    test.insertTile(0, 0, 1, true, false, 1);
    test.insertTile(0, 1, 1, true, false, 1);
    test.insertTile(0, 2, 2, true, false, 1);
    test.insertTile(0, 3, 1, true, false, 1);

    test.insertTile(1, 0, 1, true, false, 1);
    test.insertTile(1, 1, 0, true, false, 1);
    test.insertTile(1, 2, 1, true, false, 1);
    test.insertTile(1, 3, 2, true, false, 1);

    test.insertTile(2, 0, 1, true, false, 1);
    test.insertTile(2, 1, 1, true, false, 1);
    test.insertTile(2, 2, 3, false, false, 1); // obstacle
    test.insertTile(2, 3, 1, true, false, 1);

    test.insertTile(3, 0, 1, true, false, 1);
    test.insertTile(3, 1, 1, true, false, 1);
    test.insertTile(3, 2, 1, true, false, 1);
    test.insertTile(3, 3, 2, true, true, 1);

    assertTrue(test.getChargingList().contains(test.returnNode(3, 3)));
    assertTrue(test.getChargingList().size() == 1);

    Cleaner cleaner = new Cleaner();
    cleaner.setSensorMap(test);
    cleaner.fillChargingStations(test);
    cleaner.setCurrNode(test.returnNode(0, 0)); // start point
    FloorTile cc = cleaner.getClosestCharging();
    System.out.printf("The closest charging station is (%d, %d).", cc.get_x(), cc.get_y());
    List<FloorTile> map = cleaner.getTheMinPath(cc);
    for (FloorTile ft : map) {
      System.out.println(
          ft._x
              + " "
              + ft._y
              + " "
              + ft.G
              + " "
              + ft.H
              + " "
              + ft.F
              + " "
              + ft.parent._x
              + " "
              + ft.parent._y);
    }
    assertTrue(map.contains(cc));
    assertTrue(!map.contains(test.returnNode(2, 2))); // do not contain obstacles
    FloorTile tmp = cc;
    while (tmp != test.returnNode(0, 0)) {
      tmp = tmp.parent;
    }
    assertEquals(tmp, test.returnNode(0, 0)); // can reverse to start point
  }

  @Test
  void reversePathTest() throws IOException {
    System.out.println("\nTest the reversePath method can obtain a correct path from Min map");
    // based on the list returned by getTheMinPath(),
    // reversePath() gets the path from start points to destination(closest charging station)

    CustomLinkedList test = new CustomLinkedList();
    // Note: the 1st argument of insetTile() is y.
    test.insertTile(0, 0, 1, true, false, 1);
    test.insertTile(0, 1, 1, true, false, 1);
    test.insertTile(0, 2, 2, true, false, 1);
    test.insertTile(0, 3, 1, true, false, 1);

    test.insertTile(1, 0, 1, true, false, 1);
    test.insertTile(1, 1, 0, true, false, 1);
    test.insertTile(1, 2, 1, true, false, 1);
    test.insertTile(1, 3, 2, true, false, 1);

    test.insertTile(2, 0, 1, true, false, 1);
    test.insertTile(2, 1, 1, true, false, 1);
    test.insertTile(2, 2, 3, false, false, 1); // obstacle
    test.insertTile(2, 3, 1, true, false, 1);

    test.insertTile(3, 0, 1, true, false, 1);
    test.insertTile(3, 1, 1, true, false, 1);
    test.insertTile(3, 2, 1, true, false, 1);
    test.insertTile(3, 3, 2, true, true, 1);

    assertTrue(test.getChargingList().contains(test.returnNode(3, 3)));
    assertTrue(test.getChargingList().size() == 1);

    Cleaner cleaner = new Cleaner();
    cleaner.setSensorMap(test);
    cleaner.fillChargingStations(test);
    cleaner.setCurrNode(test.returnNode(0, 0)); // start point
    FloorTile cc = cleaner.getClosestCharging();
    List<FloorTile> map = cleaner.getTheMinPath(cc);
    List<FloorTile> path = cleaner.reversePath(map);
    for (FloorTile ft : path) {
      System.out.println(
          ft._x
              + " "
              + ft._y
              + " "
              + ft.G
              + " "
              + ft.H
              + " "
              + ft.F
              + " "
              + ft.parent._x
              + " "
              + ft.parent._y);
    }
    int len = path.size();
    for (int i = len - 1; i > 0; i--) {
      assertEquals(path.get(i).parent, path.get(i - 1)); // follow the path
    }
    assertEquals(path.get(len - 1), cc);
    assertEquals(path.get(0), test.returnNode(0, 0));
  }

  @Test
  void calculateUnitTest() throws IOException {
    System.out.println(
        "\nTest if the cleaner is able "
            + "to calculate how many battery consumption it needs to go to the closest charging station");
    // based on the list returned by getTheMinPath(),

    CustomLinkedList test = new CustomLinkedList();
    // Note: the 1st argument of insetTile() is y.
    test.insertTile(0, 0, 1, true, false, 1);
    test.insertTile(0, 1, 1, true, false, 1);
    test.insertTile(0, 2, 2, true, false, 1);
    test.insertTile(0, 3, 1, true, false, 1);

    test.insertTile(1, 0, 1, true, false, 1);
    test.insertTile(1, 1, 0, true, false, 1);
    test.insertTile(1, 2, 1, true, false, 1);
    test.insertTile(1, 3, 2, true, false, 1);

    test.insertTile(2, 0, 1, true, false, 1);
    test.insertTile(2, 1, 1, true, false, 1);
    test.insertTile(2, 2, 3, true, false, 1);
    test.insertTile(2, 3, 1, true, false, 1);

    test.insertTile(3, 0, 1, true, false, 1);
    test.insertTile(3, 1, 1, true, false, 1);
    test.insertTile(3, 2, 1, true, false, 1);
    test.insertTile(3, 3, 2, true, true, 1); // charging station

    Cleaner cleaner = new Cleaner();
    cleaner.setSensorMap(test);
    cleaner.fillChargingStations(test);

    cleaner.setCurrNode(test.returnNode(0, 0)); // start point
    cleaner.setCurrBattery(50); // will use 6 units of power

    assertEquals(6, cleaner.calculateUnit());
    /* calculateUnit() obtains the closest charging station itself,
    at this time, (3, 3).
    The path will be the same as the above test.
    (0,0)->(1,0)->(1,1)->(2,1)->(3,1)->(3,2)->(3,3)
    Therefore, it uses 6 units of battery.
    */

    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 1, 0});
    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 1, 1});
    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 2, 1});
    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 3, 1});
    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 3, 2});
    assertEquals(50 - 6 + 1, cleaner.getCurrBattery());
    // now the position is adjacent of charging station (3, 2).
    // if go to station, the battery will be full.

  }

  @Test
  void go2ChargingStation_and_goBackTest() throws IOException {
    System.out.println(
        "\nTest if the cleaner can go to station and go back to the position it lefts.");

    CustomLinkedList test = new CustomLinkedList();
    // Note: the 1st argument of insetTile() is y.
    test.insertTile(0, 0, 1, true, false, 1);
    test.insertTile(0, 1, 1, true, false, 1);
    test.insertTile(0, 2, 2, true, false, 1);
    test.insertTile(0, 3, 1, true, false, 1);

    test.insertTile(1, 0, 1, true, false, 1);
    test.insertTile(1, 1, 0, true, false, 1);
    test.insertTile(1, 2, 1, true, false, 1);
    test.insertTile(1, 3, 2, true, false, 1);

    test.insertTile(2, 0, 1, true, false, 1);
    test.insertTile(2, 1, 1, true, false, 1);
    test.insertTile(2, 2, 3, false, false, 1); // obstacle
    test.insertTile(2, 3, 1, true, false, 1);

    test.insertTile(3, 0, 1, true, false, 1);
    test.insertTile(3, 1, 1, true, false, 1);
    test.insertTile(3, 2, 1, true, false, 1);
    test.insertTile(3, 3, 2, true, true, 1); // charging station

    Cleaner cleaner = new Cleaner();
    cleaner.setSensorMap(test);
    cleaner.fillChargingStations(test);
    cleaner.setCurrNode(test.returnNode(0, 0)); // start point

    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 0, 1});
    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 0, 2});
    List<FloorTile> path = cleaner.go2ChargingStation();
    assertEquals(test.returnNode(3, 3), cleaner.getCurrNode());

    System.out.println("check the path");
    for (FloorTile ft : path) {
      System.out.println(
          ft._x
              + " "
              + ft._y
              + " "
              + ft.G
              + " "
              + ft.H
              + " "
              + ft.F
              + " "
              + ft.parent._x
              + " "
              + ft.parent._y);
    }

    cleaner.goBack2whereLeft(path);
    assertEquals(test.returnNode(0, 2), cleaner.getCurrNode());
    // need to set heading towards manually.
  }

  @Test
  void move2ALocationTest() throws IOException {
    System.out.println("\nTest if the cleaner can move to a specific location.");

    // same as above
    CustomLinkedList test = new CustomLinkedList();
    // Note: the 1st argument of insetTile() is y.
    test.insertTile(0, 0, 1, true, true, 1);
    test.insertTile(0, 1, 1, true, false, 1);
    test.insertTile(0, 2, 2, true, false, 1);
    test.insertTile(0, 3, 1, true, false, 1);
    test.insertTile(0, 4, 1, true, false, 1);

    test.insertTile(1, 0, 1, true, false, 1);
    test.insertTile(1, 1, 0, true, true, 1);
    test.insertTile(1, 2, 1, true, false, 1);
    test.insertTile(1, 3, 2, true, false, 1);
    test.insertTile(1, 4, 2, true, false, 1);

    test.insertTile(2, 0, 1, true, false, 1);
    test.insertTile(2, 1, 1, true, false, 1);
    test.insertTile(2, 2, 3, true, false, 1);
    test.insertTile(2, 3, 1, true, false, 1);
    test.insertTile(2, 4, 1, true, false, 1);

    test.insertTile(3, 0, 1, true, true, 1);
    test.insertTile(3, 1, 1, true, false, 1);
    test.insertTile(3, 2, 1, true, false, 1);
    test.insertTile(3, 3, 2, true, false, 1);
    test.insertTile(3, 4, 2, true, false, 1);
    // same as above

    Cleaner cleaner = new Cleaner();

    // move to north-west
    cleaner.setCurrNode(test.returnNode(2, 2));
    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 0, 0});
    System.out.printf(
        "move to north-west: from (2, 2) to (%d, %d).\n",
        cleaner.getCurrNode().get_x(), cleaner.getCurrNode().get_y());
    assertEquals(test.returnNode(0, 0), cleaner.getCurrNode());

    // move to north-east
    cleaner.setCurrNode(test.returnNode(2, 2));
    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 4, 0});
    System.out.printf(
        "move to north-east: from (2, 2) to (%d, %d).\n",
        cleaner.getCurrNode().get_x(), cleaner.getCurrNode().get_y());
    assertEquals(test.returnNode(4, 0), cleaner.getCurrNode());

    // move to south-west
    cleaner.setCurrNode(test.returnNode(2, 2));
    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 0, 3});
    System.out.printf(
        "move to south-west: from (2, 2) to (%d, %d).\n",
        cleaner.getCurrNode().get_x(), cleaner.getCurrNode().get_y());
    assertEquals(test.returnNode(0, 3), cleaner.getCurrNode());

    // move to south-east
    cleaner.setCurrNode(test.returnNode(2, 2));
    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 4, 3});
    System.out.printf(
        "move to south-east: from (2, 2) to (%d, %d).\n",
        cleaner.getCurrNode().get_x(), cleaner.getCurrNode().get_y());
    assertEquals(test.returnNode(4, 3), cleaner.getCurrNode());
  }

  @Test
  void CleanerObstacleTraversal_MovesAroundObjects() throws Exception {
    Cleaner cleaner = new Cleaner();

    CustomLinkedList floorPlan = new CustomLinkedList();

    floorPlan.convertXMLToCustomLinkedList(new File("files/SamplePlanWithAttributes.xml"));

    floorPlan.printSuccintMap();
    //		cleaner.setCurrNode(floorPlan.returnNode(0, 0));
    //		cleaner.moveToLocation_UsingStack(4, 3);
    //		assertEquals(floorPlan.returnNode(4, 3), cleaner.getCurrNode());
    //		assertNotNull(cleaner.getCurrentMap()[4][3]);

    System.out.println(cleaner.getCurrentMapString());
    for (int i = 0; i <= 8; i++) {
      for (int j = 0; j <= 5; j++) {
        if (!floorPlan.returnNode(i, j).getAccessable()) continue;
        System.out.println("Trying to go to: x: " + i + " y: " + j);
        cleaner.setCurrNode(floorPlan.returnNode(0, 0));
        cleaner.moveToLocation_UsingStack(i, j);
        assertEquals(floorPlan.returnNode(i, j), cleaner.getCurrNode());
        assertNotNull(cleaner.getCurrentMap()[i][j]);
      }
    }
  }

  @Test
  void CleanerCanDetermineIfFloorplanCleaningCompleteTest() throws IOException {
    System.out.println("\nTest if the cleaner can determine that cleaning is complete.");

    CustomLinkedList test = new CustomLinkedList();
    // Note: the 1st argument of insetTile() is y.
    test.insertTile(0, 0, 0, true, true, 1);
    test.insertTile(0, 1, 1, true, false, 1);
    test.insertTile(0, 2, 1, true, false, 1);
    test.insertTile(0, 3, 1, true, false, 1);
    test.insertTile(0, 4, 1, true, false, 1);

    Cleaner cleaner = new Cleaner();
    cleaner.setSensorMap(test);
    cleaner.fillChargingStations(test);

    cleaner.changeHeading('E');

    cleaner.moveAhead();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    cleaner.cleanSurface();

    cleaner.moveAhead();
    assertEquals(2, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    cleaner.cleanSurface();

    cleaner.moveAhead();
    assertEquals(3, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    cleaner.cleanSurface();

    cleaner.moveAhead();
    assertEquals(4, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    cleaner.cleanSurface();

    assertEquals(true, cleaner.getCleaningComplete());
  }

  @Test
  void CleanerReturnsToChargingStationIfFloorplanCleaningCompleteTest() throws IOException {
    System.out.println(
        "\nTest if the cleaner can move back to charging station once cleaning is complete.");

    CustomLinkedList test = new CustomLinkedList();
    // Note: the 1st argument of insetTile() is y.
    test.insertTile(0, 0, 0, true, true, 1);
    test.insertTile(0, 1, 1, true, false, 1);
    test.insertTile(0, 2, 1, true, false, 1);
    test.insertTile(0, 3, 1, true, false, 1);
    test.insertTile(0, 4, 1, true, false, 1);

    Cleaner cleaner = new Cleaner();
    cleaner.setSensorMap(test);
    cleaner.fillChargingStations(test);

    cleaner.changeHeading('E');

    cleaner.moveAhead();
    assertEquals(1, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    cleaner.cleanSurface();

    cleaner.moveAhead();
    assertEquals(2, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    cleaner.cleanSurface();

    cleaner.moveAhead();
    assertEquals(3, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    cleaner.cleanSurface();

    cleaner.moveAhead();
    assertEquals(4, cleaner.getCurrNode().get_x());
    assertEquals(0, cleaner.getCurrNode().get_y());
    cleaner.cleanSurface();

    assertEquals(cleaner.getSensorMap().getHead(), cleaner.getCurrNode());
    assertEquals(true, cleaner.getCurrNode().getChargeStation());
  }

  @Test
  void CleanerBlockedTest() throws IOException, SAXException, ParserConfigurationException {
    Cleaner cleaner = new Cleaner();
    System.out.printf(
        "Testing blocking status with a floor plan, that blocks when moving to a location.\n");
    CustomLinkedList floorPlan = new CustomLinkedList();

    floorPlan.createFloorFromXML(new File("files/SamplePlanWithAttributesBlocked.xml"));
    floorPlan.printList();
    cleaner.setCurrNode(floorPlan.returnNode(2, 2));

    cleaner.move2ALocation(new int[] {cleaner.headingTowards, 0, 0});
    System.out.printf("move to north-west: from (2, 2) to (0, 0)");
    assertEquals(cleaner.getCleanerStatus(), "Blocked");
    assertEquals(floorPlan.returnNode(2, 2), cleaner.getCurrNode());

    cleaner.setCleanerStatus("reset");

    cleaner.moveToLocation_UsingStack(0, 0);
    System.out.printf("move to north-west: from (2, 2) to (0, 0)");
    assertEquals(cleaner.getCleanerStatus(), "Blocked");
    assertEquals(floorPlan.returnNode(2, 2), cleaner.getCurrNode());
  }
}
