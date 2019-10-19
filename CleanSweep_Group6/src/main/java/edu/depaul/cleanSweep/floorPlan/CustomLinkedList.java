package edu.depaul.cleanSweep.floorPlan;

public class CustomLinkedList {
	private Node head; // head of list 
	private Node tail; // tail of list
	private Node currRowHead; // place saver for the current row's head node
	private Node tmpNodeHolder; // place saver for node connections 

	// constructor
	public CustomLinkedList() {
		this.head = null;
		this.tail = null;
		this.currRowHead = null;
		this.tmpNodeHolder = null;
	}
	
	// Method to insert a new node 
	public void insert(int y, int x) 
	{ 
		// Create a new node with given data 
		Node new_node = new Node(y, x); 

		// if the custom linked list is empty then make the new node as head 
		if (this.head == null) { 
			
			this.head = new_node;
			this.tail = new_node;
			this.currRowHead = new_node;
			this.tmpNodeHolder = new_node; // this is not necessary in this context but removes errors
		
		// else the linked list is not empty
		} else {  
			Node last = this.tail; // get the most recent tail
			
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
	} 
	
	public Node getHead() {
		return this.head;
	}
	
	public Node getTail() {
		return this.tail;
	}
	
	public Node getCurrRowHead() {
		return this.currRowHead;
	}
	
	public Node getTmpNodeHolder() {
		return this.tmpNodeHolder;
	}

	// Method to print the LinkedList. 
	public void printList() { 
		Node currRowHead = this.head; // used to increment the row south for printing
		Node currNode = this.head; // used to increment the node east for printing
		int counter = 0; // used to tell what the current row is
		
		while (currRowHead != null) { // start on the first row then print what the current row is
			System.out.printf("LinkedList Row %d: ", counter); 

			// start at the head of the row and increment east while printing
			while (currNode != null) { 
				// Print the coordinates at current node 
				System.out.print("(" + currNode._y + ", " + currNode._x + "); "); 
				System.out.print("accessibility : " + currNode.getAccessable());
				// Go to next node
				currNode = currNode.east; 
			}
			
			System.out.print("\n");
			counter++; // increment counter after row is finished printing
			currRowHead = currRowHead.south; // advance to the next row
			currNode = currRowHead; // reset the current node to the head of the next row
		} 
	}
	
	public Node returnNode(int x, int y)
	{
		Node currRowHead = this.head; // used to increment the row south
		Node currNode = this.head; // used to increment the node east
		
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
}
