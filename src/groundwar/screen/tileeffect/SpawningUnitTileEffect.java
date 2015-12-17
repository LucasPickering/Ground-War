package groundwar.screen.tileeffect;

import groundwar.unit.Unit;

public class SpawningUnitTileEffect extends TileEffect {

  public SpawningUnitTileEffect(Unit unit, boolean valid) {
    super(Layer.UNIT, unit.getName(),
          unit.getOwner().primaryColor & (valid ? 0xeeffffff : 0x66ffffff));
  }
}
