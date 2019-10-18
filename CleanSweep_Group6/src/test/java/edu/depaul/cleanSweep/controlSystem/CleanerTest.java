package edu.depaul.cleanSweep.controlSystem;

import static org.junit.jupiter.api.Assertions.*;

import edu.depaul.cleanSweep.cell.Cell;
import edu.depaul.cleanSweep.cell.SurfaceType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CleanerTest {

	private static final int MAX_DIRT_CAPACITY = 50;

	@Test
	void DirtIntake_DoesNotMaxCapacity(){
		Cleaner cleaner = new Cleaner();

		Cell cell1 = new Cell(10, SurfaceType.LOWPILE);

		assertEquals(10, cell1.getDirtAmount());
		assertEquals(0, cleaner.getCurrentBagSize());

		cleaner.cleanSurface(cell1);

		assertEquals(9, cell1.getDirtAmount());
		assertEquals(1, cleaner.getCurrentBagSize());
	}

	@Test
	void DirtIntake_PerfectlyMaxCapacity(){
		Cleaner cleaner = new Cleaner();

		for(int i: IntStream.range(1, 51).boxed().collect(Collectors.toList())){
			Cell cell = new Cell(10, SurfaceType.BARE);
			cleaner.cleanSurface(cell);
			assertEquals(9, cell.getDirtAmount());
			assertEquals(i, cleaner.getCurrentBagSize());
		}

		assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

		Cell uncleanedCell = new Cell(12, SurfaceType.LOWPILE);

		cleaner.cleanSurface(uncleanedCell);

		// Assert bag has not grown
		assertEquals(MAX_DIRT_CAPACITY, cleaner.getCurrentBagSize());

		// Assert the cell was NOT cleaned
		assertEquals(12, uncleanedCell.getDirtAmount());
	}

	// todo - remove unit test once/if ui is written
	@Test
	void CapacityNotifications_NotifiesWhenFull(){
		// redirect stdout
		PrintStream savedStdout = System.out;

		Cleaner cleaner = new Cleaner();

		for(int i: IntStream.range(1, 50).boxed().collect(Collectors.toList())){

			Cell cell = new Cell(10, SurfaceType.BARE);
			cleaner.cleanSurface(cell);
			assertEquals(9, cell.getDirtAmount());
			assertEquals(i, cleaner.getCurrentBagSize());
		}

		cleaner.cleanSurface(new Cell(10, SurfaceType.BARE));
        String string1 = new String("The Clean Sweep is out of space for dirt!");
        String string2 = cleaner.getCleanerStatus();
		assertEquals(string1, string2);
	}
}
