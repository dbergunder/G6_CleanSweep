package edu.depaul.cleanSweep.floorPlanTest;

import static org.junit.jupiter.api.Assertions.*;

import edu.depaul.cleanSweep.floorPlan.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
		
		// test.printList(); this tests that the list prints out (more to come)
	}
}
