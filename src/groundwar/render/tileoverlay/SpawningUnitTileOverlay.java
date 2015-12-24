package groundwar.render.tileoverlay;

import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.unit.Unit;

public class SpawningUnitTileOverlay extends TileOverlay {

  public SpawningUnitTileOverlay(Unit unit, boolean valid) {
    super(new ColorTexture(Constants.TILE_BG_NAME,
                           valid ? Colors.UNIT_SPAWNING_VALID : Colors.UNIT_SPAWNING_INVALID),
          new ColorTexture(unit.getType().textureName, Colors.UNIT_SPAWNING));
  }
}
