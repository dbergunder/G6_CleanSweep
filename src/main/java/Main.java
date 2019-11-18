import edu.depaul.cleanSweep.controlSystem.*;
import edu.depaul.cleanSweep.floorPlan.*;

import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.List;
import java.util.Random;

public class Main{

//	private static CustomLinkedList xmlFloorPlan = new CustomLinkedList();
//	private static CustomLinkedList myFloor = new CustomLinkedList();

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {


        CustomLinkedList floorPlan = new CustomLinkedList();
        floorPlan.convertXMLToCustomLinkedList(new File("files/SamplePlanWithAttributes.xml"));

        Cleaner cleaner = new Cleaner();
        cleaner.setSensorMap(floorPlan);
        System.out.println("Hello! My start point is " + cleaner.printCoordinate());
        cleaner.fillChargingStations(floorPlan);
        cleaner.setCurrBattery(20);
        cleaner.setCurrDirtCapacity(40);

        floorPlan.printSuccintMap();

        while(!cleaner.getCleaningComplete()){
                List<FloorTile> map = cleaner.getTheMinPath(floorPlan.returnNode(8, 5));

                for(FloorTile cell: map) {
                        cleaner.moveToLocation_UsingStack(cell._x, cell._y);
                }

                System.out.println(cleaner.getCurrentMapString());

                List<FloorTile> reversePath = cleaner.reversePath(map);

                for (FloorTile cell : reversePath) {
                        cleaner.moveToLocation_UsingStack(cell._x, cell._y);
                }

                System.out.println(cleaner.getCurrentMapString());

                // returning null pointer exception here
                List<FloorTile> secondMap = cleaner.getTheMinPath(floorPlan.returnNode(8, 5));

                for(FloorTile cell: secondMap) {
                        cleaner.moveToLocation_UsingStack(cell._x, cell._y);
                }
        }


	}
}
