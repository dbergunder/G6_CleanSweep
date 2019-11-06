package edu.depaul.cleanSweep.controlSystem;

import static org.junit.jupiter.api.Assertions.*;
import edu.depaul.cleanSweep.controlSystem.*;
import edu.depaul.cleanSweep.floorPlan.*;

import edu.depaul.cleanSweep.cell.Cell;
import edu.depaul.cleanSweep.cell.SurfaceType;
import edu.depaul.cleanSweep.floorPlan.CustomLinkedList;
import edu.depaul.cleanSweep.floorPlan.FloorTile;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CleanersTest {

	private static final int MAX_DIRT_CAPACITY = 50;

	@Test
	void DirtIntake_DoesNotMaxCapacity() throws IOException{
		Cleaner cleaner = new Cleaner();
		cleaner.setCurrNode(new FloorTile(0,0,10,TileType.LOWPILE)); // adjust only for the new added print{} in cleanSerface().
		FloorTile cell1 = new FloorTile(0,0,10,TileType.LOWPILE);

		assertEquals(10, cell1.getUnitsOfDirt());
		assertEquals(0, cleaner.getCurrentBagSize());

		cleaner.cleanSurface(cell1);

		assertEquals(9, cell1.getUnitsOfDirt());
		assertEquals(1, cleaner.getCurrentBagSize());
	}

	@Test
	void DirtIntake_PerfectlyMaxCapacity() throws IOException{
		Cleaner cleaner = new Cleaner();
		cleaner.setCurrNode(new FloorTile(0,0,10,TileType.LOWPILE)); 
		// adjust only for the new added print{} in cleanSerface().
		for(int i: IntStream.range(1, 51).boxed().collect(Collectors.toList())){
			FloorTile cell = new FloorTile(0,i,10,TileType.BARE);
			cleaner.cleanSurface(cell);
			assertEquals(9, cell.getUnitsOfDirt());
			assertEquals(i, cleaner.getCurrentBagSize());
		}

		assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

		FloorTile uncleanedCell = new FloorTile(0,0,12,TileType.LOWPILE);

		cleaner.cleanSurface(uncleanedCell);

		// Assert bag has not grown
		assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

		// Assert the cell was NOT cleaned
		assertEquals(12, uncleanedCell.getUnitsOfDirt());
	}

	// todo - remove unit test once/if ui is written
	@Test
	void CapacityNotifications_NotifiesWhenFull() throws IOException{
		Cleaner cleaner = new Cleaner();
		cleaner.setCurrNode(new FloorTile(0,0,10,TileType.LOWPILE)); 
		// adjust only for the new added print{} in cleanSerface().
		for(int i: IntStream.range(1, 50).boxed().collect(Collectors.toList())){
			FloorTile cell = new FloorTile(0,i,10,TileType.BARE);
			cleaner.cleanSurface(cell);
			assertEquals(9, cell.getUnitsOfDirt());
			assertEquals(i, cleaner.getCurrentBagSize());
		}

		FloorTile uncleanedCell = new FloorTile(0,0,10,TileType.BARE);
		cleaner.cleanSurface(uncleanedCell);
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

		//move ahead
		cleaner.setCurrNode(test.getHead());
		cleaner.changeHeading('E');
		assertEquals('E', cleaner.getHeading());
		System.out.println("cleaner is towards east");
		System.out.println("current location\n" + cleaner.printCoordinate());
		assert(cleaner.getCurrNode()._x == 0);
		assert(cleaner.getCurrNode()._y == 0);

		System.out.println("\nturn ahead");
		cleaner.moveAhead();
		assertEquals(1, cleaner.getCurrNode().get_x());
		assertEquals(0, cleaner.getCurrNode().get_y());
		cleaner.printCoordinate();

		//move backward
		System.out.println("\nturn back");
		cleaner.moveBack();
		assertEquals(0, cleaner.getCurrNode().get_x());
		assertEquals(0, cleaner.getCurrNode().get_y());
		cleaner.printCoordinate();

		//move left
		System.out.println("\nturn left");
		cleaner.moveLeft();
		assertEquals(0, cleaner.getCurrNode().get_x());
		assertEquals(1, cleaner.getCurrNode().get_y());
		cleaner.printCoordinate();

		//move right
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
	void CleanerHistory_AccuratelyReportsHistory() throws IOException, SAXException, ParserConfigurationException {
		Cleaner cleaner = new Cleaner();

		CustomLinkedList floorPlan = new CustomLinkedList();

		floorPlan.convertXMLToCustomLinkedList(
		        new File("files/SamplePlan.xml"));

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

        cleaner.moveAhead();

        assertEquals(3, cleaner.getCleanerHistory().size());
    }

	@Test
	void CleanerInteriorMap_AccuratlyKeepsTrackofVisitedLocations() throws IOException, SAXException, ParserConfigurationException {
		Cleaner cleaner = new Cleaner();

		CustomLinkedList floorPlan = new CustomLinkedList();

		floorPlan.convertXMLToCustomLinkedList(
				new File("files/SamplePlanWithAttributes.xml"));

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

    private FloorTile getFloorTile(Cleaner cleaner, int index){
	    return (FloorTile) cleaner.getCleanerHistory().toArray()[index];
    }

	@Test
	void CleanerMovementOnAllFloorResultsInBatteryConsumptionTest() throws IOException {
		System.out.println("\nCleanerMovementOnAllFloorResultsInBatteryConsumptionTest");

		CustomLinkedList test = new CustomLinkedList();
		test.insertTile(0, 0, 1, true,  false,  1);
		test.insertTile(0, 1, 1, true,  false,  2);
		test.insertTile(1, 0, 1, true,  false,  3);
		test.insertTile(1, 1, 1, true,  false,  2);

		Cleaner cleaner = new Cleaner();

		//move ahead
		cleaner.setCurrNode(test.getHead());
		cleaner.changeHeading('E');
		System.out.println("cleaner is towards east");
		System.out.println("current location\n" + cleaner.printCoordinate());
		assert(cleaner.getCurrNode()._x == 0);
		assert(cleaner.getCurrNode()._y == 0);
		assertEquals(250.00, cleaner.getBatteryPower());

		System.out.println("\nturn ahead");
		cleaner.moveAhead();
		assertEquals(1, cleaner.getCurrNode().get_x());
		assertEquals(0, cleaner.getCurrNode().get_y());
		assertEquals(248.50, cleaner.getBatteryPower());
		cleaner.printCoordinate();

		//move backward
		System.out.println("\nturn back");
		cleaner.moveBack();
		assertEquals(0, cleaner.getCurrNode().get_x());
		assertEquals(0, cleaner.getCurrNode().get_y());
		assertEquals(247.00, cleaner.getBatteryPower());
		cleaner.printCoordinate();

		//move left
		System.out.println("\nturn left");
		cleaner.moveLeft();
		assertEquals(0, cleaner.getCurrNode().get_x());
		assertEquals(1, cleaner.getCurrNode().get_y());
		assertEquals(245.00, cleaner.getBatteryPower());
		cleaner.printCoordinate();

		//move right
		cleaner.changeHeading('N');
		assertEquals('N', cleaner.getHeading());
		System.out.println("\ncleaner is towards north");
		System.out.println("turn right");
		cleaner.moveRight();
		assertEquals(1, cleaner.getCurrNode().get_x());
		assertEquals(1, cleaner.getCurrNode().get_y());
		assertEquals(242.50, cleaner.getBatteryPower());
		cleaner.printCoordinate();
	}

	@Test
	void CleanerCleaningResultsInBatteryConsumptionTest() throws IOException {
		System.out.println("\nCleanerCleaningResultsInBatteryConsumptionTest");

		CustomLinkedList test = new CustomLinkedList();
		test.insertTile(0, 0, 1, true,  false,  1);
		test.insertTile(0, 1, 1, true,  false,  2);
		test.insertTile(1, 0, 1, true,  false,  3);
		test.insertTile(1, 1, 1, true,  false,  2);

		Cleaner cleaner = new Cleaner();

		//move ahead
		cleaner.setCurrNode(test.getHead());
		cleaner.changeHeading('E');
		System.out.println("cleaner is towards east");
		System.out.println("current location\n" + cleaner.printCoordinate());
		assert(cleaner.getCurrNode()._x == 0);
		assert(cleaner.getCurrNode()._y == 0);
		assertEquals(250.00, cleaner.getBatteryPower());
		cleaner.cleanSurface(cleaner.getCurrNode());
		assertEquals(249.00, cleaner.getBatteryPower());

		System.out.println("\nturn ahead");
		cleaner.moveAhead();
		assertEquals(1, cleaner.getCurrNode().get_x());
		assertEquals(0, cleaner.getCurrNode().get_y());
		assertEquals(247.50, cleaner.getBatteryPower());
		cleaner.cleanSurface(cleaner.getCurrNode());
		assertEquals(245.50, cleaner.getBatteryPower());
		cleaner.printCoordinate();

		//move backward
		System.out.println("\nturn back");
		cleaner.moveBack();
		assertEquals(0, cleaner.getCurrNode().get_x());
		assertEquals(0, cleaner.getCurrNode().get_y());
		assertEquals(244.00, cleaner.getBatteryPower());
		cleaner.cleanSurface(cleaner.getCurrNode());
		assertEquals(244.00, cleaner.getBatteryPower());
		cleaner.printCoordinate();

		//move left
		System.out.println("\nturn left");
		cleaner.moveLeft();
		assertEquals(0, cleaner.getCurrNode().get_x());
		assertEquals(1, cleaner.getCurrNode().get_y());
		assertEquals(242.00, cleaner.getBatteryPower());
		cleaner.cleanSurface(cleaner.getCurrNode());
		assertEquals(239.00, cleaner.getBatteryPower());
		cleaner.printCoordinate();

		//move right
		cleaner.changeHeading('N');
		assertEquals('N', cleaner.getHeading());
		System.out.println("\ncleaner is towards north");
		System.out.println("turn right");
		cleaner.moveRight();
		assertEquals(1, cleaner.getCurrNode().get_x());
		assertEquals(1, cleaner.getCurrNode().get_y());
		assertEquals(236.50, cleaner.getBatteryPower());
		cleaner.cleanSurface(cleaner.getCurrNode());
		assertEquals(234.50, cleaner.getBatteryPower());
		cleaner.printCoordinate();
	}

	@Test
	void CleanerMovingToChargingStationStationResultsInBatteryRefillTest() throws IOException {
		System.out.println("\nCleanerMovingToChargingStationStationResultsInBatteryRefillTest");

		CustomLinkedList test = new CustomLinkedList();
		test.insertTile(0, 0, 1, true,  false,  1);
		test.insertTile(0, 1, 1, true,  false,  2);
		test.insertTile(1, 0, 1, true,  false,  3);
		test.insertTile(1, 1, 0, true,  true,  2);

		Cleaner cleaner = new Cleaner();

		//move ahead
		cleaner.setCurrNode(test.getHead());
		cleaner.changeHeading('E');
		System.out.println("cleaner is towards east");
		System.out.println("current location\n" + cleaner.printCoordinate());
		assert(cleaner.getCurrNode()._x == 0);
		assert(cleaner.getCurrNode()._y == 0);
		assertEquals(250.00, cleaner.getBatteryPower());
		cleaner.cleanSurface(cleaner.getCurrNode());
		assertEquals(249.00, cleaner.getBatteryPower());

		System.out.println("\nturn ahead");
		cleaner.moveAhead();
		assertEquals(1, cleaner.getCurrNode().get_x());
		assertEquals(0, cleaner.getCurrNode().get_y());
		assertEquals(247.50, cleaner.getBatteryPower());
		cleaner.cleanSurface(cleaner.getCurrNode());
		assertEquals(245.50, cleaner.getBatteryPower());
		cleaner.printCoordinate();

		//move backward
		System.out.println("\nturn back");
		cleaner.moveBack();
		assertEquals(0, cleaner.getCurrNode().get_x());
		assertEquals(0, cleaner.getCurrNode().get_y());
		assertEquals(244.00, cleaner.getBatteryPower());
		cleaner.cleanSurface(cleaner.getCurrNode());
		assertEquals(244.00, cleaner.getBatteryPower());
		cleaner.printCoordinate();

		//move left
		System.out.println("\nturn left");
		cleaner.moveLeft();
		assertEquals(0, cleaner.getCurrNode().get_x());
		assertEquals(1, cleaner.getCurrNode().get_y());
		assertEquals(242.00, cleaner.getBatteryPower());
		cleaner.cleanSurface(cleaner.getCurrNode());
		assertEquals(239.00, cleaner.getBatteryPower());
		cleaner.printCoordinate();

		//move right
		cleaner.changeHeading('N');
		assertEquals('N', cleaner.getHeading());
		System.out.println("\ncleaner is towards north");
		System.out.println("turn right");
		cleaner.moveRight();
		assertEquals(1, cleaner.getCurrNode().get_x());
		assertEquals(1, cleaner.getCurrNode().get_y());
		assertEquals(250.00, cleaner.getBatteryPower());
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
		//Note: the 1st argument of insetTile() is y.
		test.insertTile(0, 0, 1, true,  true,  1);
		test.insertTile(0, 1, 1, true,  false,  1);
		test.insertTile(0, 2, 2, true,  false,  1);
		test.insertTile(0, 3, 1, true,  false,  1);
		test.insertTile(0, 4, 1, true,  false,  1);

		test.insertTile(1, 0, 1, true,  false,  1);
		test.insertTile(1, 1, 0, true,  true,  1);
		test.insertTile(1, 2, 1, true,  false,  1);
		test.insertTile(1, 3, 2, true,  false,  1);
		test.insertTile(1, 4, 2, true,  false,  1);

		test.insertTile(2, 0, 1, true,  false,  1);
		test.insertTile(2, 1, 1, true,  false,  1);
		test.insertTile(2, 2, 3, true,  false,  1);
		test.insertTile(2, 3, 1, true,  false,  1);
		test.insertTile(2, 4, 1, true,  false,  1);

		test.insertTile(3, 0, 1, true,  true,  1);
		test.insertTile(3, 1, 1, true,  false,  1);
		test.insertTile(3, 2, 1, true,  false,  1);
		test.insertTile(3, 3, 2, true,  false,  1);
		test.insertTile(3, 4, 2, true,  false,  1);

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
	void move2ALocationTest() throws IOException {
		System.out.println("\nTest if the cleaner can move to a specific location.");

		//same as above
		CustomLinkedList test = new CustomLinkedList();
		//Note: the 1st argument of insetTile() is y.
		test.insertTile(0, 0, 1, true,  true,  1);
		test.insertTile(0, 1, 1, true,  false,  1);
		test.insertTile(0, 2, 2, true,  false,  1);
		test.insertTile(0, 3, 1, true,  false,  1);
		test.insertTile(0, 4, 1, true,  false,  1);

		test.insertTile(1, 0, 1, true,  false,  1);
		test.insertTile(1, 1, 0, true,  true,  1);
		test.insertTile(1, 2, 1, true,  false,  1);
		test.insertTile(1, 3, 2, true,  false,  1);
		test.insertTile(1, 4, 2, true,  false,  1);

		test.insertTile(2, 0, 1, true,  false,  1);
		test.insertTile(2, 1, 1, true,  false,  1);
		test.insertTile(2, 2, 3, true,  false,  1);
		test.insertTile(2, 3, 1, true,  false,  1);
		test.insertTile(2, 4, 1, true,  false,  1);

		test.insertTile(3, 0, 1, true,  true,  1);
		test.insertTile(3, 1, 1, true,  false,  1);
		test.insertTile(3, 2, 1, true,  false,  1);
		test.insertTile(3, 3, 2, true,  false,  1);
		test.insertTile(3, 4, 2, true,  false,  1);
		//same as above

		Cleaner cleaner = new Cleaner();

		//move to north-west
		cleaner.setCurrNode(test.returnNode(2, 2));
		cleaner.move2ALocation(new int[] {cleaner.headingTowards, 0, 0});
		System.out.printf("move to north-west: from (2, 2) to (%d, %d).\n", 
				cleaner.getCurrNode().get_x(), cleaner.getCurrNode().get_y());
		assertEquals(test.returnNode(0, 0), cleaner.getCurrNode());

		//move to north-east
		cleaner.setCurrNode(test.returnNode(2, 2));
		cleaner.move2ALocation(new int[] {cleaner.headingTowards, 4, 0});
		System.out.printf("move to north-east: from (2, 2) to (%d, %d).\n",
				cleaner.getCurrNode().get_x(), cleaner.getCurrNode().get_y());
		assertEquals(test.returnNode(4, 0), cleaner.getCurrNode());

		//move to south-west
		cleaner.setCurrNode(test.returnNode(2, 2));
		cleaner.move2ALocation(new int[] {cleaner.headingTowards, 0, 3});
		System.out.printf("move to south-west: from (2, 2) to (%d, %d).\n", 
				cleaner.getCurrNode().get_x(), cleaner.getCurrNode().get_y());
		assertEquals(test.returnNode(0, 3), cleaner.getCurrNode());

		//move to south-east
		cleaner.setCurrNode(test.returnNode(2, 2));
		cleaner.move2ALocation(new int[] {cleaner.headingTowards, 4, 3});
		System.out.printf("move to south-east: from (2, 2) to (%d, %d).\n", 
				cleaner.getCurrNode().get_x(), cleaner.getCurrNode().get_y());
		assertEquals(test.returnNode(4, 3), cleaner.getCurrNode());
	}

	@Test
	void CleanerObstacleTraversal_MovesAroundObjects() throws IOException, SAXException, ParserConfigurationException {
		Cleaner cleaner = new Cleaner();

		CustomLinkedList floorPlan = new CustomLinkedList();

		floorPlan.convertXMLToCustomLinkedList(
				new File("files/SamplePlanWithAttributes.xml"));

		floorPlan.printSuccintMap();

		cleaner.setCurrNode(floorPlan.returnNode(0, 0));

		cleaner.move2ALocation(new int[] {cleaner.headingTowards, 2, 0});
		assertEquals(floorPlan.returnNode(2, 0), cleaner.getCurrNode());

		System.out.println(cleaner.getCurrentMap()[2][0]);

	}
}
