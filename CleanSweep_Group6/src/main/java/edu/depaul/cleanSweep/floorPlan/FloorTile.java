package edu.depaul.cleanSweep.floorPlan;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class FloorTile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public FloorTile north; 
	public FloorTile south; 
	public FloorTile east; 
	public FloorTile west;
	public int _x;
	public int _y;
//	int surfaceType;
//	boolean dirt;
//	boolean visited;
	boolean accessable;
//	boolean chargingStation;
	
	public FloorTile() {
		super();
	}
	
	// Constructor 
	// will make floorplan a java object from xml, which will allow for these nodes to be inserted
	public FloorTile(int y, int x) 
	{ 
		north = null;
		south = null; 
		east = null; 
		west = null;
		_x = x;
		_y = y;
//		surfaceType = 1;
//		dirt = false;
//		visited = false;
		accessable = false;
//		chargingStation = false;
	}
	
	public void setAccessable(boolean f) {
		accessable = f;
	}
	
	public boolean getAccessable() {
		return accessable;
	}
	
	public void print()
	{
		System.out.println("My coordinates are " + _x + " , "+ _y + "");
	}
	
	
	public int get_x() {
		return _x;
	}
	
	public int get_y() {
		return _y;
	}
	
	
}

//public class Employee implements Serializable {
// 
//    private static final long serialVersionUID = 1L;
//     
//    private Integer id;
//    private String firstName;
//    private String lastName;
//    private Department department;
//     
//    public Employee() {
//        super();
//    }
// 
//    public Employee(int id, String fName, String lName, Department department) {
//        super();
//        this.id = id;
//        this.firstName = fName;
//        this.lastName = lName;
//        this.department = department;
//    }
// 
//    //Setters and Getters
// 
//    @Override
//    public String toString() {
//        return "Employee [id=" + id + ", firstName=" + firstName + ",
//                        lastName=" + lastName + ", department="+ department + "]";
//    }
//}