package edu.depaul.cleanSweep.controlSystem;

import static org.junit.jupiter.api.Assertions.*;
import edu.depaul.cleanSweep.controlSystem.*;
import edu.depaul.cleanSweep.floorPlan.*;

import edu.depaul.cleanSweep.cell.Cell;
import edu.depaul.cleanSweep.cell.SurfaceType;
import edu.depaul.cleanSweep.floorPlan.CustomLinkedList;
import edu.depaul.cleanSweep.floorPlan.FloorTile;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CleanerTest {

	private static final int MAX_DIRT_CAPACITY = 50;

	@Test
	void DirtIntake_DoesNotMaxCapacity(){
		Cleaner cleaner = new Cleaner();

		FloorTile cell1 = new FloorTile(0,0,10,TileType.LOW_PILE);

		assertEquals(10, cell1.getUnitsOfDirt());
		assertEquals(0, cleaner.getCurrentBagSize());

		cleaner.cleanSurface(cell1);

		assertEquals(9, cell1.getUnitsOfDirt());
		assertEquals(1, cleaner.getCurrentBagSize());
	}

	@Test
	void DirtIntake_PerfectlyMaxCapacity(){
		Cleaner cleaner = new Cleaner();

		for(int i: IntStream.range(1, 51).boxed().collect(Collectors.toList())){
			FloorTile cell = new FloorTile(0,i,10,TileType.BARE_FLOOR);
			cleaner.cleanSurface(cell);
			assertEquals(9, cell.getUnitsOfDirt());
			assertEquals(i, cleaner.getCurrentBagSize());
		}

		assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

		FloorTile uncleanedCell = new FloorTile(0,0,12,TileType.LOW_PILE);

		cleaner.cleanSurface(uncleanedCell);

		// Assert bag has not grown
		assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

		// Assert the cell was NOT cleaned
		assertEquals(12, uncleanedCell.getUnitsOfDirt());
	}

	// todo - remove unit test once/if ui is written
	@Test
	void CapacityNotifications_NotifiesWhenFull(){
		Cleaner cleaner = new Cleaner();

		for(int i: IntStream.range(1, 50).boxed().collect(Collectors.toList())){
			FloorTile cell = new FloorTile(0,i,10,TileType.BARE_FLOOR);
			cleaner.cleanSurface(cell);
			assertEquals(9, cell.getUnitsOfDirt());
			assertEquals(i, cleaner.getCurrentBagSize());
		}

		FloorTile uncleanedCell = new FloorTile(0,0,10,TileType.BARE_FLOOR);
		cleaner.cleanSurface(uncleanedCell);
        String string1 = new String("The Clean Sweep is out of space for dirt!");
        String string2 = cleaner.getCleanerStatus();
		assertEquals(string1, string2);
	}
	
	
	@Test
	void OneCellAtATimeTest() {
		
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
		assertEquals('E', cleaner.getHeadingTowards());
		System.out.println("make a move");
		cleaner.moveAhead();
		assertEquals(1, cleaner.getCurrNode().get_x());
		assertEquals(0, cleaner.getCurrNode().get_y());
	}
	
	@Test
	public void MoveIn4AxesTest() {
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
		assertEquals('E', cleaner.getHeadingTowards());
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
		assertEquals('N', cleaner.getHeadingTowards());
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

		var floorPlan = new CustomLinkedList();

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

    private FloorTile getFloorTile(Cleaner cleaner, int index){
	    return (FloorTile) cleaner.getCleanerHistory().toArray()[index];
    }
}
