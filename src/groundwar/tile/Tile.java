package groundwar.tile;

import java.util.LinkedList;
import java.util.List;

import groundwar.Constants;
import groundwar.HexPoint;
import groundwar.unit.Unit;

public class Tile {

  private final HexPoint pos;
  private int backgroundColor;
  private int outlineColor;
  protected final List<Tile> adjacentTiles = new LinkedList<>();
  private Unit unit;

  public Tile(HexPoint pos) {
    this(pos, Constants.TILE_BG_COLOR, Constants.TILE_OUTLINE_COLOR);
  }

  public Tile(HexPoint pos, int backgroundColor, int outlineColor) {
    this.pos = pos;
    this.backgroundColor = backgroundColor;
    this.outlineColor = outlineColor;
  }

  public void setAdjacentTiles(List<Tile> adjTiles) {
    adjacentTiles.addAll(adjTiles);
  }

  public HexPoint getPos() {
    return pos;
  }

  public int getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(int backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public int getOutlineColor() {
    return outlineColor;
  }

  public void setOutlineColor(int outlineColor) {
    this.outlineColor = outlineColor;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
    onUnitChange();
  }

  /**
   * Gets the distance between this tile and another tile located at the given point.
   *
   * @param p the other point
   * @return the distance between this tile and {@param p}
   */
  public int distanceTo(HexPoint p) {
    return pos.distanceTo(p);
  }

  /**
   * Gets the distance between this tile and another tile.
   *
   * @param tile the other tile
   * @return the distance between this tile and {@param tile}
   */
  public int distanceTo(Tile tile) {
    return distanceTo(tile.getPos());
  }

  /**
   * Is this tile directly adjacent to the given one? In other words, is the distance between this
   * tile and the given one strictly equal to 1?
   *
   * @param tile the tile to be checked for adjacency
   * @return {@code distanceTo(tile) == 1}
   */
  public boolean isAdjacentTo(Tile tile) {
    return distanceTo(tile) == 1;
  }

  // Events

  /**
   * Called <i>directly after</i> the unit on this tile changes.
   */
  protected void onUnitChange() {
  }
}
