package groundwar.tile;

import groundwar.Constants;
import groundwar.HexPoint;

public class MountainTile extends Tile {

  public MountainTile(HexPoint pos) {
    super(pos, Constants.MOUNTAIN_BG_COLOR, Constants.TILE_OUTLINE_COLOR);
  }
}
