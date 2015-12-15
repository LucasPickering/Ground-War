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
  private final List<Tile> adjacentTiles = new LinkedList<>();
  private Unit unit;

  public Tile(HexPoint pos) {
    this(pos, Constants.TILE_BG_COLOR, Constants.TILE_OUTLINE_COLOR);
  }

  public Tile(HexPoint pos, int backgroundColor, int outlineColor) {
    this.pos = pos;
    this.backgroundColor = backgroundColor;
    this.outlineColor = outlineColor;
  }

  public final List<Tile> getAdjacentTiles() {
    return adjacentTiles;
  }

  public final void setAdjacentTiles(List<Tile> adjTiles) {
    adjacentTiles.clear();
    adjacentTiles.addAll(adjTiles);
    onSetAdjacents();
  }

  public final HexPoint getPos() {
    return pos;
  }

  public final int getBackgroundColor() {
    return backgroundColor;
  }

  public final void setBackgroundColor(int backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public final int getOutlineColor() {
    return outlineColor;
  }

  public final void setOutlineColor(int outlineColor) {
    this.outlineColor = outlineColor;
  }

  public final Unit getUnit() {
    return unit;
  }

  public final void setUnit(Unit unit) {
    this.unit = unit;
    onUnitChange();
  }

  /**
   * Gets the distance between this tile and another tile located at the given point.
   *
   * @param p the other point
   * @return the distance between this tile and {@param p}
   */
  public final int distanceTo(HexPoint p) {
    return pos.distanceTo(p);
  }

  /**
   * Convenice method for {@link #distanceTo(HexPoint)}.
   *
   * @param tile the other tile
   * @return {@code distanceTo(tile.getPos()}
   */
  public final int distanceTo(Tile tile) {
    return distanceTo(tile.getPos());
  }

  /**
   * Is this tile directly adjacent to the tile at the given position? In other words, is the distance
   * between this tile and the given point strictly equal to 1?
   *
   * @param p the point to be checked for adjacency
   * @return {@code distanceTo(p) == 1}
   */
  public final boolean isAdjacentTo(HexPoint p) {
    return distanceTo(p) == 1;
  }

  /**
   * Convenience method for {@link #isAdjacentTo(HexPoint)}.
   *
   * @param tile the tile to be checked for adjacency
   * @return {@code isAdjacentTo(tile.getPos())}
   */
  public final boolean isAdjacentTo(Tile tile) {
    return isAdjacentTo(tile.getPos());
  }

  // Events

  /**
   * Called <i>directly after</i> {@link #adjacentTiles} is populated.
   */
  protected void onSetAdjacents() {
  }

  /**
   * Called <i>directly after</i> the unit on this tile changes.
   */
  protected void onUnitChange() {
  }
}
