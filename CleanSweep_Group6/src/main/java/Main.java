import edu.depaul.cleanSweep.controlSystem.*;
import edu.depaul.cleanSweep.floorPlan.*;

public class Main {
	
	private static CustomLinkedList myFloor = new CustomLinkedList();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Create cleaner, and create floor plan of 100 tiles. 
		//Started moving around floor, should run into obstacles.
		Cleaner myCleaner = new Cleaner();
		createFloor();
		myCleaner.setCurrNode(myFloor.returnNode(1, 1)); 
		for (int i = 0; i < 10;i++)
		{
			//TODO update when navigation is done
			if(myCleaner.moveAhead() == false)
			{
				myCleaner.moveLeft();
			}
			System.out.println(myCleaner.printCoordinate());
		}
		
		System.out.println(myCleaner.getCleanerStatus());
	}
	
	private static void createFloor()
	{
		int odds = 0;

		for (int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				myFloor.insert(i, j);
			}
		}
		for (int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
		    	if(odds % 2 == 0) 
				{
					if(myFloor.returnNode(i, j) != null)
					{
						myFloor.returnNode(i, j).setAccessable(false);
					}
				}
				else 
				{
					if(myFloor.returnNode(i,j) != null)
					{
						myFloor.returnNode(i, j).setAccessable(true);	
					}
				}
			}
		odds = odds + 1;
		}
		myFloor.printList();
	}

}
