package edu.depaul.cleanSweep.cell;

public enum SurfaceType {
	BARE(1), LOWPILE(2), HIGHPILE(3);
	
	private int abbr;
	
	SurfaceType(int abb) {
		abbr = abb;
	}
	 
	public int getAbbr() {
		return abbr;
	}
	
}
