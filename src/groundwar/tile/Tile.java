package groundwar.tile;

import java.util.LinkedList;
import java.util.List;

import groundwar.Constants;
import groundwar.HexPoint;
import groundwar.Player;
import groundwar.Point;
import groundwar.unit.Unit;

public class Tile {

  /**
   * The position of this tile within the board.
   */
  private final HexPoint pos;

  /**
   * The position of the top-left corner of the texture of this tile on the screen.
   */
  private final Point screenPos;
  private int backgroundColor;
  private int outlineColor;
  private final List<Tile> adjacentTiles = new LinkedList<>();
  private Unit unit;

  public Tile(HexPoint pos) {
    this(pos, Constants.TILE_BG_COLOR, Constants.TILE_OUTLINE_COLOR);
  }

  public Tile(HexPoint pos, int backgroundColor, int outlineColor) {
    this.pos = pos;
    this.screenPos = Constants.BOARD_CENTER.plus(
        (int) (Constants.TILE_WIDTH * pos.getX() * 0.75f),
        (int) (-Constants.TILE_HEIGHT * (pos.getX() / 2.0f + pos.getY())));
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

  public final Point getScreenPos() {
    return screenPos;
  }

  public final Point getCenterPos() {
    return screenPos.plus(Constants.TILE_WIDTH / 2, Constants.TILE_HEIGHT / 2);
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

  public final void spawnUnit(Unit unit) {
    this.unit = unit;
    onUnitChange();
  }

  public final void killUnit() {
    unit = null;
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

  /**
   * Does this tile contain the {@link Point} p? p is a point in screen-space, not in tile-space. This
   * is essentially used to check if the mouse is over this tile.
   *
   * @param p the point
   * @return true if this tile contains p, false otherwise
   */
  public final boolean contains(Point p) {
    return getCenterPos().distanceTo(p) <= Constants.TILE_RADIUS;
  }

  /**
   * Can this tile be selected by the current player?
   *
   * @param currentPlayer the player whose turn it currently is
   * @return true if this tile can be selected, false otherwise
   */
  public boolean isSelectable(Player currentPlayer) {
    return unit != null && unit.getOwner() == currentPlayer;
  }

  /**
   * Can the given unit be spawned on this tile?
   * @param unit the unit to be spawned
   * @return true if the unit can be spawned here, false otherwise
   */
  public boolean isSpawnable(Unit unit) {
    return false;
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
