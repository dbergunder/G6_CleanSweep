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

	@Test
	void test() {
		//fail("Not yet implemented");
	}

	@Test
	void CleanerTest_DoesNotMaxCapacity(){
		Cleaner cleaner = new Cleaner();

		Cell cell1 = new Cell(10, SurfaceType.LOWPILE);

		assertTrue(cell1.surface == SurfaceType.LOWPILE);
		assertTrue(cleaner.getCurrentBagSize() == 0);

		cleaner.cleanSurface(cell1);

		assertTrue(cell1.surface == SurfaceType.BARE);
		assertTrue(cleaner.getCurrentBagSize() == 10);
	}

	@Test
	void CleanerTest_MaxCapacity(){
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
			assertTrue(cell.dirtAmount == 0);
			assertTrue(cell.surface == SurfaceType.BARE);
		}

		assertTrue(cleaner.getCurrentBagSize() == 50);

		Cell uncleanedCell = new Cell(12, SurfaceType.LOWPILE);

		cleaner.cleanSurface(uncleanedCell);

		// Assert bag has not grown
		assertTrue(cleaner.getCurrentBagSize() == 50);

		// Assert the cell was NOT cleaned
		assertTrue(uncleanedCell.dirtAmount == 12);
		assertTrue(uncleanedCell.surface == SurfaceType.LOWPILE);
		
	}
}
