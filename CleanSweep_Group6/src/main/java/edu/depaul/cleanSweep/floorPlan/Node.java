package edu.depaul.cleanSweep.floorPlan;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Node implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Node north; 
	public Node south; 
	public Node east; 
	public Node west;
	public int _x;
	public int _y;
//	int surfaceType;
//	boolean dirt;
//	boolean visited;
//	boolean accessable;
//	boolean chargingStation;

	// Constructor 
	// will make floorplan a java object from xml, which will allow for these nodes to be inserted
	Node(int y, int x) 
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
//		accessable = false;
//		chargingStation = false;
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