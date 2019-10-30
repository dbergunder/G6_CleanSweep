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
	TileType surfaceType;
	boolean isClean;
	boolean accessable;
	boolean chargingStation;
	int unitsOfDirt;
	
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
		
		// Default values, will be an easy tile. 
		surfaceType = TileType.BARE_FLOOR;
		isClean = true;
		accessable = true;
		chargingStation = false;
		unitsOfDirt = 0;
	}

	public FloorTile(int y, int x , boolean isAccessable){
		north = null;
		south = null;
		east = null;
		west = null;
		_x = x;
		_y = y;
		accessable = isAccessable;
	}
	
	public FloorTile(int y, int x, int dirtAmount, TileType type) 
	{
		north = null;
		south = null; 
		east = null; 
		west = null;
		_x = x;
		_y = y;
		surfaceType = type;
		unitsOfDirt = dirtAmount;
		isClean = true;
		if (unitsOfDirt > 0)
		{
			isClean = false;
		}
		
		// Default values, will be an easy tile. 
		accessable = true;
		chargingStation = false;

	}

	public void setAccessable(boolean f) {
		accessable = f;
	}
	
	public boolean getAccessable() {
		return accessable;
	}
	
	public void setSurfaceType(TileType type){
		surfaceType = type;
	}
	
	public TileType getSurfaceType() {
		return surfaceType;
	}
	
	public void setClean(boolean clean) {
		isClean = clean;
	}
	
	public boolean getClean() {
		return isClean;
	}
	
	public void setUnitsOfDirt(int units) {
		if (unitsOfDirt > 0)
		{
			isClean = false;
		}
		unitsOfDirt = units;
	}
	
	public int getUnitsOfDirt() {
		return unitsOfDirt;
	}
	
	public void setChargeStation(boolean chargeStation) {
		chargingStation = chargeStation;
	}
	
	public boolean getChargeStation() {
		return chargingStation;
	}
	
	public void print()
	{
		System.out.println("My coordinates are " + _x + " , "+ _y + "");
		System.out.println("isClean " + isClean + " isChargingStation " + chargingStation);
		System.out.println(" Units of Dirt " + unitsOfDirt + " surfaceType " + surfaceType);
	}
	
	
	public int get_x() {
		return _x;
	}
	
	public int get_y() {
		return _y;
	}
	
	public void decreaseDirtAmount() {
		if (unitsOfDirt <= 1){
			isClean = true;
			unitsOfDirt = 0;
			return;
		}
		unitsOfDirt = unitsOfDirt -1;
		return;
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