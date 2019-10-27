package edu.depaul.cleanSweep.floorPlanTest;

import static org.junit.jupiter.api.Assertions.*;

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
	}
	
	// TODO test.printList()
}
