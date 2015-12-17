package groundwar.tile;

import groundwar.HexPoint;
import groundwar.constants.Colors;

public class ForwardFortTile extends Tile {

  public ForwardFortTile(HexPoint pos) {
    super(pos, Colors.FORT_BG, Colors.FORT_OUTLINE);
  }

  @Override
  public void onSetAdjacents() {
    setColors();
  }

  @Override
  public void onUnitChange() {
    setColors();
  }

  private void setColors() {
    int newBgColor;
    int newOutlineColor;

    // If there's a unit on this tile now, set the colors to correspond to the unit's owner
    if (getUnit() != null) {
      newBgColor = getUnit().getOwner().secondaryColor;
      newOutlineColor = getUnit().getOwner().primaryColor;
    } else { // There's no unit, set colors to default
      newBgColor = Colors.FORT_BG;
      newOutlineColor = Colors.FORT_OUTLINE;
    }

    // Set both colors of this tile
    setBackgroundColor(newBgColor);
    setOutlineColor(newOutlineColor);

    // Set the outline color for adjacent tiles
    getAdjacentTiles().forEach(tile -> tile.setOutlineColor(newOutlineColor));
  }
}
