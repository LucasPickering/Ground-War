package groundwar.tile;

import groundwar.Constants;
import groundwar.HexPoint;

public class GoldTile extends Tile {

  public GoldTile(HexPoint pos) {
    super(pos, Constants.GOLD_BG_COLOR, Constants.GOLD_OUTLINE_COLOR);
  }
}
