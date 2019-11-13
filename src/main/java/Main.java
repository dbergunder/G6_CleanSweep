import edu.depaul.cleanSweep.controlSystem.*;
import edu.depaul.cleanSweep.floorPlan.*;

import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*; 

public class Main{

//	private static CustomLinkedList xmlFloorPlan = new CustomLinkedList();
//	private static CustomLinkedList myFloor = new CustomLinkedList();

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {


		CustomLinkedList test = new CustomLinkedList();
		//Note: the 1st argument of insetTile() is y.
		test.insertTile(0, 0, 1, true,  false,  1);
		test.insertTile(0, 1, 3, true,  false,  1);
		test.insertTile(0, 2, 2, true,  false,  2);
		test.insertTile(0, 3, 1, true,  false,  1);
		test.insertTile(0, 4, 1, true,  false,  3);

		test.insertTile(1, 0, 2, true,  false,  1);
		test.insertTile(1, 1, 0, true,  false,  3);
		test.insertTile(1, 2, 1, true,  false,  1);
		test.insertTile(1, 3, 5, true,  false,  2);
		test.insertTile(1, 4, 2, true,  false,  1);

		test.insertTile(2, 0, 3, true,  false,  1);
		test.insertTile(2, 1, 1, true,  false,  1);
		test.insertTile(2, 2, 3, true,  false,  2);
		test.insertTile(2, 3, 1, true,  true,  1);
		test.insertTile(2, 4, 1, true,  false,  3);

		test.insertTile(3, 0, 1, true,  false,  1);
		test.insertTile(3, 1, 5, true,  false,  1);
		test.insertTile(3, 2, 1, true,  false,  2);
		test.insertTile(3, 3, 2, true,  false,  2);
		test.insertTile(3, 4, 2, true,  false,  1);

		Cleaner cleaner = new Cleaner();
		cleaner.setCurrNode(test.getHead());
		System.out.println("Hello! My start point is " + cleaner.printCoordinate());
		cleaner.fillChargingStations(test);
		cleaner.setCurrBattery(70);
		cleaner.setCurrDirtCapacity(40);
		
		cleaner.changeHeading('E');
		cleaner.moveAhead();
		
		while(!cleaner.getCurrNode().getClean()) {
			cleaner.cleanSurface(cleaner.getCurrNode());	
		}
		
		for(int i=0;i<4;i++) {
			for(int j=0;j<3;j++) {
				cleaner.moveAhead();
				
				while(!cleaner.getCurrNode().getClean()) {
					cleaner.cleanSurface(cleaner.getCurrNode());	
				}
			}
			
			if(i != 3) {
				if(i % 2 == 0) {
					cleaner.moveRight();
					while(!cleaner.getCurrNode().getClean()) {
						cleaner.cleanSurface(cleaner.getCurrNode());	
					}
					
					cleaner.moveRight();
					while(!cleaner.getCurrNode().getClean()) {
						cleaner.cleanSurface(cleaner.getCurrNode());	
					}
					
				} else {
					cleaner.moveLeft();
					while(!cleaner.getCurrNode().getClean()) {
						cleaner.cleanSurface(cleaner.getCurrNode());	
					}
					
					cleaner.moveLeft();
					while(!cleaner.getCurrNode().getClean()) {
						cleaner.cleanSurface(cleaner.getCurrNode());	
					}
				}
			}
		}
	}
}
