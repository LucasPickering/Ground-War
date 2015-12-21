package groundwar.tile;

import groundwar.Point;
import groundwar.constants.Colors;

public class GoldTile extends Tile {

  public GoldTile(Point pos) {
    super(pos, Colors.GOLD_BG, Colors.GOLD_OUTLINE);
  }
}
