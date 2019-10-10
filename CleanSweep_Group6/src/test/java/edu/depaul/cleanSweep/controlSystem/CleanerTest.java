package edu.depaul.cleanSweep.controlSystem;

import static org.junit.jupiter.api.Assertions.*;

import edu.depaul.cleanSweep.cell.Cell;
import edu.depaul.cleanSweep.cell.SurfaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


class CleanerTest {

	private static final int MAX_DIRT_CAPACITY = 50;

	@Test
	void CleanerTest_DoesNotMaxCapacity(){
		Cleaner cleaner = new Cleaner();

		Cell cell1 = new Cell(10, SurfaceType.LOWPILE);

		assertEquals(10, cell1.dirtAmount);
		assertEquals(0, cleaner.getCurrentBagSize());

		cleaner.cleanSurface(cell1);

		assertEquals(0, cell1.dirtAmount);
		assertEquals(10, cleaner.getCurrentBagSize());
	}

	@Test
	void CleanerTest_PerfectlyMaxCapacity(){
		Cleaner cleaner = new Cleaner();

		List<Cell> board = new LinkedList<>(Arrays.asList(
				new Cell(5, SurfaceType.LOWPILE),
				new Cell(30, SurfaceType.HIGHPILE),
				new Cell(15, SurfaceType.HIGHPILE)
		));

		for (Cell cell : board) {
				cleaner.cleanSurface(cell);
		}
		for (Cell cell : board) {
			assertEquals(0, cell.dirtAmount);
		}

		assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

		Cell uncleanedCell = new Cell(12, SurfaceType.LOWPILE);

		cleaner.cleanSurface(uncleanedCell);

		// Assert bag has not grown
		assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

		// Assert the cell was NOT cleaned
		assertEquals(12, uncleanedCell.dirtAmount);
	}

	@Test
	void CleanerTest_TakesPartofDirt(){
		Cleaner cleaner = new Cleaner();

		List<Cell> board = new LinkedList<>(Arrays.asList(
				new Cell(5, SurfaceType.LOWPILE),
				new Cell(30, SurfaceType.HIGHPILE),
				new Cell(10, SurfaceType.HIGHPILE)
		));

		for (Cell cell : board) {
			cleaner.cleanSurface(cell);
		}
		for (Cell cell : board) {
			assertEquals(0, cell.dirtAmount);
		}

		// At this point in time, the bag can hold 5 more dirt
		assertEquals(45, cleaner.getCurrentBagSize());

		Cell uncleanedCell = new Cell(12, SurfaceType.LOWPILE);

		cleaner.cleanSurface(uncleanedCell);

		// Assert bag picked up the 5 dirt
		assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

		// Assert the cell was partially cleaned (5 dirt less)
		assertEquals(7, uncleanedCell.dirtAmount);
	}
}
