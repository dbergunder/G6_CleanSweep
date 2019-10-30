package edu.depaul.cleanSweep.floorPlan;

public enum TileType {
		BARE_FLOOR,
		LOW_PILE,
		HIGH_PILE;
		
		public static TileType fromInteger(int x) {
		    
			switch(x) {
			
		    case 1:
		        return TileType.BARE_FLOOR;
		    case 2:
		        return TileType.LOW_PILE;
		    case 3:
		    	return TileType.HIGH_PILE;
			}
		    return null;
		}
}



