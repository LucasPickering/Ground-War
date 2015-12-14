package groundwar;

import java.util.HashMap;
import java.util.Map;

import groundwar.tile.Tile;

public class Board {

  private final Map<HexPoint, Tile> tiles = new HashMap<>();

  public Map<HexPoint, Tile> getTiles() {
    return tiles;
  }
}
