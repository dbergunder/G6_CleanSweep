package edu.depaul.cleanSweep.floorPlan;

import java.util.HashMap;

public enum TileType {
  BARE(1),
  LOWPILE(2),
  HIGHPILE(3);

  // abbr indicates how many units of charge this surface will consume.
  private int value;
  private static HashMap<Object, Object> map = new HashMap<>();

  private TileType(int abb) {
    value = abb;
  }

  static {
    for (TileType tileType : TileType.values()) {
      map.put(tileType.value, tileType);
    }
  }

  public int getValue() {
    return value;
  }

  public static TileType valueOf(int tileType) {
    return (TileType) map.get(tileType);
  }
}
