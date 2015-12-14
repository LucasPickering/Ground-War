package groundwar.tile;

import groundwar.HexPoint;
import groundwar.Player;

public class FortTile extends Tile {

  private final Player owner;

  public FortTile(HexPoint pos, Player owner) {
    super(pos, owner.primaryColor, owner.secondaryColor);
    this.owner = owner;
  }
}
