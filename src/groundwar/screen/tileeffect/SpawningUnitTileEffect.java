package groundwar.screen.tileeffect;

import groundwar.Constants;
import groundwar.unit.Unit;

public class SpawningUnitTileEffect extends TileOverlay {

  private static final int UNIT_MASK = 0xcc999999;
  private static final int VALID_COLOR = 0x4400ff00;
  private static final int INVALID_COLOR = 0x44ff0000;

  public SpawningUnitTileEffect(Unit unit, boolean valid) {
    super(new ColorTexture(unit.getName(), unit.getOwner().primaryColor & UNIT_MASK),
          new ColorTexture(Constants.TILE_BG_NAME, valid ? VALID_COLOR : INVALID_COLOR));
  }
}
