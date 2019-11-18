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

        while(!cleaner.moreCleaningToDo()){
                for(int i = 0; i<=5; i++){
                        for(int j = 0; j<=8; j++){
                                if(!floorPlan.returnNode(j, i).getAccessable())
                                        continue;
                                cleaner.moveToLocation_UsingStack(j, i);
                        }
                }
        }
        System.out.println("***************");
        System.out.println("CLEANING IS COMPLETE.");
        System.out.println("***************");
    }
}
