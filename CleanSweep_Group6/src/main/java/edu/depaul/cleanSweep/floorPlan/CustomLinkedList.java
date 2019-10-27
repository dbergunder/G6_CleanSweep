package edu.depaul.cleanSweep.floorPlan;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CustomLinkedList {
	private FloorTile head; // head of list 
	private FloorTile tail; // tail of list
	private FloorTile currRowHead; // place saver for the current row's head node
	private FloorTile tmpNodeHolder; // place saver for node connections 

	// constructor
	public CustomLinkedList() {
		this.head = null;
		this.tail = null;
		this.currRowHead = null;
		this.tmpNodeHolder = null;
	}
	
	public void insertIntoList(FloorTile new_node)
	{
		// if the custom linked list is empty then make the new node as head 
		if (this.head == null) { 
			
			this.head = new_node;
			this.tail = new_node;
			this.currRowHead = new_node;
			this.tmpNodeHolder = new_node; // this is not necessary in this context but removes errors
		
		// else the linked list is not empty
		} else {  
			FloorTile last = this.tail; // get the most recent tail
			
			// if the new node is in the same row as the head 
			if (this.head._y == new_node._y) {
//				should already have the tail, stored in "last"
				
				// connect the new node east of the tail
				last.east = new_node;
				new_node.west = last;
				this.tail = new_node; // update list.tail
			
			// else if the new node is on a new row
			} else if (last._y < new_node._y) {
				// grab the currRowHead, and connect south
				last = this.currRowHead;
				
				last.south = new_node; // connect the new node south of the currRowHead
				new_node.north = last;
				
				this.tmpNodeHolder = this.currRowHead; // update the tmpNodeHolder with the previous row's head
				this.currRowHead = new_node; // update the currRowHead
				this.tail = new_node; // update the tail
				
			// else the new node is not on the same row as the head	and will be inserted to the east of the tail
			} else {
//				should already have the tail, stored in "last"
				// update the tmpNodeHolder with the node "north-east" of the tail, if it exists
				if (this.tmpNodeHolder.east != null) {
					this.tmpNodeHolder = this.tmpNodeHolder.east;
				}
				
				// connect the new node east of the tail
				last.east = new_node;
				new_node.west = last;
				
				// connect the new node south of the tmpNodeHolder
				this.tmpNodeHolder.south = new_node;
				new_node.north = this.tmpNodeHolder;
				
				// update tail
				this.tail = new_node;
			}
		} 
//		System.out.println("inserted node (" + y + ", " + x + ")"); // used for testing	
	}
	
	// Method to insert a new node 
	public void insert(int y, int x) 
	{ 
		// Create a new node with given data 
		FloorTile new_node = new FloorTile(y, x); 
		insertIntoList(new_node);
	} 
	
	public FloorTile getHead() {
		return this.head;
	}
	
	public FloorTile getTail() {
		return this.tail;
	}
	
	public FloorTile getCurrRowHead() {
		return this.currRowHead;
	}
	
	public FloorTile getTmpNodeHolder() {
		return this.tmpNodeHolder;
	}

	// Method to print the LinkedList. 
	public void printList() { 
		FloorTile currRowHead = this.head; // used to increment the row south for printing
		FloorTile currNode = this.head; // used to increment the node east for printing
		int counter = 0; // used to tell what the current row is
		
		while (currRowHead != null) { // start on the first row then print what the current row is
			System.out.printf("LinkedList Row %d: ", counter); 

			// start at the head of the row and increment east while printing
			while (currNode != null) { 
				// Print the coordinates at current node 
				System.out.print("(" + currNode._y + ", " + currNode._x + "); "); 
				System.out.print("accessibility : " + currNode.getAccessable() + " || ");
				// Go to next node
				currNode = currNode.east; 
			}
			
			System.out.print("\n");
			counter++; // increment counter after row is finished printing
			currRowHead = currRowHead.south; // advance to the next row
			currNode = currRowHead; // reset the current node to the head of the next row
		} 
	}
	
	public FloorTile returnNode(int x, int y)
	{
		FloorTile currRowHead = this.head; // used to increment the row south
		FloorTile currNode = this.head; // used to increment the node east
		
		while (currRowHead != null ) { //search through nodes to find specific node
			while (currNode != null) { 
				if(currNode._y == y && currNode._x == x)
				{
					return currNode;
				}
				// Go to next node
				currNode = currNode.east; 
			}
			currRowHead = currRowHead.south; // advance to the next row
			currNode = currRowHead; // reset the current node to the head of the next row
		}
		return null;
	}
	
	public void convertXMLToCustomLinkedList(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		 
		//Build Document
		Document document = builder.parse(xmlFile);
		 
		//Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
		 
		//Get the root node "floor" with tiles inside
//		Element root = document.getDocumentElement(); // used for testing
//		System.out.println(root.getNodeName()); // used for testing
		 
		//Get all floor tiles
		NodeList nList = document.getElementsByTagName("tile");
//		System.out.println("============================"); // used for testing

		// loop through the listed floor tiles and convert them into FloorTile objects
		for (int count = 0; count < nList.getLength(); count++) {
			Node node = nList.item(count); // create a temporary node full of tile information as string
//			System.out.println("");    //Just a separator
			if (node != null) {
				//Print each node's detail
				Element eElement = (Element) node; // used to access tile's elements, if node exists convert to element format
				int y = Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent()); // get Y
				int x = Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent()); // get X
//				System.out.println("Y : " + y); // used for testing
//				System.out.println("X : " + x); // used for testing
				this.insert(y, x);
//				System.out.println("Y : "  + eElement.getElementsByTagName("y").item(0).getTextContent()); // used for testing
//				System.out.println("X : "  + eElement.getElementsByTagName("x").item(0).getTextContent()); // used for testing
			}
		}
		
		this.printList(); // used to test that a floor plan has been converted from xml to customlinkedlist
		System.out.println(); // used as a separator
	}
	public void createFloorFromXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		 
		//Build Document
		Document document = builder.parse(xmlFile);
		 
		//Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
		 
		//Get all floor tiles
		NodeList nList = document.getElementsByTagName("tile");

		// loop through the listed floor tiles and convert them into FloorTile objects
		for (int count = 0; count < nList.getLength(); count++) {
			Node node = nList.item(count); // create a temporary node full of tile information as string
			if (node != null) {
				//Print each node's detail
				Element eElement = (Element) node; // used to access tile's elements, if node exists convert to element format
				int y = Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent()); // get Y
				int x = Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent()); // get X
				int type = Integer.parseInt(eElement.getElementsByTagName("surfaceType").item(0).getTextContent()); // get surfacetype
				boolean accessible = Boolean.parseBoolean(eElement.getElementsByTagName("accessible").item(0).getTextContent()); // get accessible
				int dirt = Integer.parseInt(eElement.getElementsByTagName("dirt").item(0).getTextContent()); // get dirt
				boolean chargingStation =  Boolean.parseBoolean(eElement.getElementsByTagName("chargingStation").item(0).getTextContent()); // get chargingStation
				this.insertTile(y, x, dirt, accessible, chargingStation, type);
				}
		}
		
		this.printList(); // used to test that a floor plan has been converted from xml to customlinkedlist
		System.out.println(); // used as a separator
	}
	
	public void insertTile(int y, int x, int dirtAmount, boolean accessible, boolean chargingStation, int floortype) 
	{ 
		TileType type = TileType.fromInteger(floortype);
		// Create a new node with given data 
		FloorTile new_node = new FloorTile(y, x, dirtAmount, type);
		new_node.setChargeStation(chargingStation);
		new_node.setAccessable(accessible);
		insertIntoList(new_node);
	} 
}
