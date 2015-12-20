package groundwar.tile;

import groundwar.HexPoint;
import groundwar.constants.Colors;
import groundwar.unit.Unit;

public class MountainTile extends Tile {

  public MountainTile(HexPoint pos) {
    super(pos, Colors.MOUNTAIN_BG, Colors.TILE_OUTLINE);
  }

  @Override
  public boolean isMoveable(Unit unit) {
    return false;
  }
}
