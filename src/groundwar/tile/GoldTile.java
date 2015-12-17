package groundwar.tile;

import groundwar.HexPoint;
import groundwar.constants.Colors;

public class GoldTile extends Tile {

  public GoldTile(HexPoint pos) {
    super(pos, Colors.GOLD_BG, Colors.GOLD_OUTLINE);
  }
}
