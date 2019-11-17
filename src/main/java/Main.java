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

//        while(!cleaner.getCleaningComplete()){
//
//            int testX = (new Random()).nextInt(6);
//            int testY = (new Random()).nextInt(9);
//
//            cleaner.moveToLocation_UsingStack(testX, testY);
//        }

        List<FloorTile> map = cleaner.getTheMinPath(floorPlan.returnNode(8, 5));

        for (FloorTile cell : map) {
            cleaner.moveToLocation_UsingStack(cell._x, cell._y);
        }

        System.out.println(cleaner.getCurrentMapString());

        System.out.println(cleaner.getNotCleanMap());

        List<FloorTile> secondMap = cleaner.getTheMinPath(floorPlan.returnNode(2, 8));

        for (FloorTile cell : secondMap) {
            cleaner.moveToLocation_UsingStack(cell._x, cell._y);
        }

        System.out.println(cleaner.getCurrentMapString());

        System.out.println(cleaner.getNotCleanMap());


	}
}
