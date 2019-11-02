package edu.depaul.cleanSweep.floorPlanTest;

import static org.junit.jupiter.api.Assertions.*;

import edu.depaul.cleanSweep.controlSystem.Cleaner;
import edu.depaul.cleanSweep.floorPlan.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class FloorPlanTest {
	private CustomLinkedList test = new CustomLinkedList();
	
	@Test
	void FloorPlanTest_InsertsHeadNode(){
		test.insert(0, 0);

		assertEquals(0, test.getHead()._y);
		assertEquals(0, test.getHead()._x);
		assertEquals(0, test.getTail()._y);
		assertEquals(0, test.getTail()._x);
		assertEquals(0, test.getCurrRowHead()._y);
		assertEquals(0, test.getCurrRowHead()._x);
		assertEquals(0, test.getTmpNodeHolder()._y);
		assertEquals(0, test.getTmpNodeHolder()._x);
	}
	
	@Test
	void FloorPlanTest_InsertsRowOfThreeNodes(){
		test.insert(0, 0);
		test.insert(0, 1);
		test.insert(0, 2);

		assertEquals(0, test.getHead()._y);
		assertEquals(0, test.getHead()._x);
		assertEquals(0, test.getTail()._y);
		assertEquals(2, test.getTail()._x);
		assertEquals(0, test.getCurrRowHead()._y);
		assertEquals(0, test.getCurrRowHead()._x);
		assertEquals(0, test.getTmpNodeHolder()._y);
		assertEquals(0, test.getTmpNodeHolder()._x);
	}
	
	@Test
	void FloorPlanTest_InsertsTwoRowsOfThreeNodes(){
		test.insert(0, 0);
		test.insert(0, 1);
		test.insert(0, 2);
		test.insert(1, 0);
		test.insert(1, 1);
		test.insert(1, 2);

		assertEquals(0, test.getHead()._y);
		assertEquals(0, test.getHead()._x);
		assertEquals(1, test.getTail()._y);
		assertEquals(2, test.getTail()._x);
		assertEquals(1, test.getCurrRowHead()._y);
		assertEquals(0, test.getCurrRowHead()._x);
		assertEquals(0, test.getTmpNodeHolder()._y);
		assertEquals(2, test.getTmpNodeHolder()._x);
		
	}
	
	@Test
	void FloorPlanTest_ConvertsXMLFileToCustomLinkedList() throws ParserConfigurationException, SAXException, IOException{
		File xmlFile = new File("files/SamplePlan.xml");
		
		test.convertXMLToCustomLinkedList(xmlFile);

		assertEquals(0, test.getHead()._y);
		assertEquals(0, test.getHead()._x);
		assertEquals(2, test.getTail()._y);
		assertEquals(2, test.getTail()._x);
		assertEquals(2, test.getCurrRowHead()._y);
		assertEquals(0, test.getCurrRowHead()._x);
		assertEquals(1, test.getTmpNodeHolder()._y);
		assertEquals(2, test.getTmpNodeHolder()._x);
	}
	
	@Test
	void FloorPlanTest_TestAttributes() throws ParserConfigurationException, SAXException, IOException{
		File xmlFile = new File("files/SamplePlanWithAttributes.xml");
		
		test.createFloorFromXML(xmlFile);
		test.printList();
		
		//Tile by tile confirm amount of dirt, floor type, accessiblity, and if it was a charge station
		//Charging stations
		FloorTile currTile = test.returnNode(0, 3);
		assertEquals(true, currTile.getChargeStation());
		
		currTile = test.returnNode(0, 4);
		assertEquals(true, currTile.getChargeStation());
		
		currTile = test.returnNode(1, 3);
		assertEquals(true, currTile.getChargeStation());
		
		currTile = test.returnNode(1, 4);
		assertEquals(true, currTile.getChargeStation());
		
		//Door
		currTile = test.returnNode(3, 0);
		assertEquals(false, currTile.getAccessable());
		//couch
		currTile = test.returnNode(5, 2);
		assertEquals(false, currTile.getAccessable());
		currTile = test.returnNode(6, 2);
		assertEquals(false, currTile.getAccessable());
		currTile = test.returnNode(7, 2);
		assertEquals(false, currTile.getAccessable());
		currTile = test.returnNode(5, 3);
		assertEquals(false, currTile.getAccessable());
		currTile = test.returnNode(6, 3);
		assertEquals(false, currTile.getAccessable());
		currTile = test.returnNode(7, 3);
		assertEquals(false, currTile.getAccessable());
		
		//Low Pile
		currTile = test.returnNode(3, 1);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.LOWPILE, currTile.getSurfaceType());
		
		currTile = test.returnNode(3, 2);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.LOWPILE, currTile.getSurfaceType());
		
		currTile = test.returnNode(3, 3);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.LOWPILE, currTile.getSurfaceType());
		
		currTile = test.returnNode(3, 4);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.LOWPILE, currTile.getSurfaceType());
		
		currTile = test.returnNode(3, 5);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.LOWPILE, currTile.getSurfaceType());
		
		//HIGH PILE
		currTile = test.returnNode(5, 4);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.HIGHPILE, currTile.getSurfaceType());
		
		currTile = test.returnNode(6, 4);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.HIGHPILE, currTile.getSurfaceType());
		
		currTile = test.returnNode(7, 4);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.HIGHPILE, currTile.getSurfaceType());
		
		currTile = test.returnNode(5, 5);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.HIGHPILE, currTile.getSurfaceType());
		
		currTile = test.returnNode(6, 5);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.HIGHPILE, currTile.getSurfaceType());
		
		currTile = test.returnNode(7, 5);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.HIGHPILE, currTile.getSurfaceType());
		
		
		//These tiles are all 1 unit of dirt, bare floor, accessible and not a charging station
		currTile = test.returnNode(0, 0);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(1, 0);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(2, 0);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(4, 0);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(5, 0);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(6, 0);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(7, 0);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(8, 0);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(0, 1);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(1, 1);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(2, 1);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(4, 1);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(5, 1);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(6, 1);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(7, 1);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(8, 1);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(0, 2);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(1, 2);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(2, 2);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(4, 2);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(8, 2);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(2, 3);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(4, 3);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(8, 3);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(2, 4);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(4, 4);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(8, 4);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(2, 5);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(4, 5);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
		
		currTile = test.returnNode(8, 5);
		assertEquals(1, currTile.getUnitsOfDirt());
		assertEquals(TileType.BARE, currTile.getSurfaceType());
		assertEquals(true, currTile.getAccessable());
		assertEquals(false, currTile.getChargeStation());
	}
	
	// TODO test.printList()
}
