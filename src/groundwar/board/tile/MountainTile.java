package groundwar.board.tile;

import groundwar.util.Point;
import groundwar.util.Colors;
import groundwar.board.unit.Unit;

public class MountainTile extends Tile {

  public MountainTile(Point pos) {
    super(pos, Colors.MOUNTAIN_BG, Colors.TILE_OUTLINE);
  }

  @Override
  public boolean isMoveable(Unit mover) {
    return false;
  }
}
