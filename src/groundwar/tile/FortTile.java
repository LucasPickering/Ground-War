package groundwar.tile;

import groundwar.HexPoint;
import groundwar.Player;
import groundwar.unit.Unit;

public class FortTile extends Tile {

  private final Player owner;

  public FortTile(HexPoint pos, Player owner) {
    super(pos, owner.secondaryColor, owner.primaryColor);
    this.owner = owner;
  }

  @Override
  public boolean isSpawnable(Unit unit) {
    return getUnit() == null && unit.getOwner() == owner;
  }
}
