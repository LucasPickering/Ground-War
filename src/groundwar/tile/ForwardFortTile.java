package groundwar.tile;

import groundwar.Constants;
import groundwar.HexPoint;

public class ForwardFortTile extends Tile {

  public ForwardFortTile(HexPoint pos) {
    super(pos, Constants.FORT_BG_COLOR, Constants.FORT_OUTLINE_COLOR);
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
      newBgColor = Constants.FORT_BG_COLOR;
      newOutlineColor = Constants.FORT_OUTLINE_COLOR;
    }

    // Set both colors of this tile
    setBackgroundColor(newBgColor);
    setOutlineColor(newOutlineColor);

    // Set the outline color for adjacent tiles
    getAdjacentTiles().forEach(tile -> tile.setOutlineColor(newOutlineColor));
  }
}
