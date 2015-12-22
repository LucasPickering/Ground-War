package groundwar.tile;

import groundwar.Player;
import groundwar.util.Point;
import groundwar.util.Colors;

public class ForwardFortTile extends Tile {

  public ForwardFortTile(Point pos) {
    super(pos, Colors.FORT_BG, Colors.FORT_OUTLINE);
  }

  @Override
  public void onSetAdjacents() {
    updateNeighbors();
  }

  @Override
  public void onUnitChange() {
    updateNeighbors();
  }

  private void updateNeighbors() {
    final Player owner;
    final int newBgColor;
    final int newOutlineColor;

    // If there's a unit on this tile now, set the owner to that unit's owner, and the color to the
    // owner's colors
    if (getUnit() != null) {
      owner = getUnit().getOwner();
      newBgColor = getUnit().getOwner().secondaryColor;
      newOutlineColor = getUnit().getOwner().primaryColor;
    } else { // There's no unit, set owner to null and colors to default
      owner = null;
      newBgColor = Colors.FORT_BG;
      newOutlineColor = Colors.FORT_OUTLINE;
    }

    // Set colors and owner for this tile
    setOwner(owner);
    setBackgroundColor(newBgColor);
    setOutlineColor(newOutlineColor);

    // Set colors and owner for all adjacent tiles
    for (Tile adjTile : getAdjacentTiles()) {
      if (adjTile != null) {
        adjTile.setOwner(owner);
        adjTile.setOutlineColor(newOutlineColor);
      }
    }
  }
}
