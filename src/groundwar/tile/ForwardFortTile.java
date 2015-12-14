package groundwar.tile;

import groundwar.Constants;
import groundwar.HexPoint;

public class ForwardFortTile extends Tile {

  public ForwardFortTile(HexPoint pos){
    super(pos, Constants.FORT_BG_COLOR, Constants.FORT_OUTLINE_COLOR);
  }
}
