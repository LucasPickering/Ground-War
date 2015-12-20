package groundwar.tile;

import java.util.Objects;

import groundwar.HexPoint;
import groundwar.Player;
import groundwar.Point;
import groundwar.constants.Colors;
import groundwar.constants.Constants;
import groundwar.unit.Unit;

public class Tile {

  /**
   * The position of this tile within the board. Non-null.
   */
  private final HexPoint pos;

  /**
   * The player who owns this tile. In most cases, the tile is unowned and this is {@code null}.
   */
  private Player owner;

  /**
   * The position of the top-left corner of the texture of this tile on the screen.
   */
  private final Point screenPos;
  private int backgroundColor;
  private int outlineColor;
  private final Tile[] adjacentTiles = new Tile[Constants.NUM_SIDES];
  private Unit unit;

  public Tile(HexPoint pos) {
    this(pos, Colors.TILE_BG, Colors.TILE_OUTLINE);
  }

  public Tile(HexPoint pos, int backgroundColor, int outlineColor) {
    this(pos, null, backgroundColor, outlineColor);
  }

  public Tile(HexPoint pos, Player owner) {
    this(pos, owner, owner.secondaryColor, owner.primaryColor);
  }

  public Tile(HexPoint pos, Player owner, int backgroundColor, int outlineColor) {
    Objects.nonNull(pos);
    this.pos = pos;
    this.owner = owner;
    this.screenPos = Constants.BOARD_CENTER.plus(
        (int) (Constants.TILE_WIDTH * pos.getX() * 0.75f),
        (int) (-Constants.TILE_HEIGHT * (pos.getX() / 2.0f + pos.getY())));
    this.backgroundColor = backgroundColor;
    this.outlineColor = outlineColor;
  }

  public final Tile[] getAdjacentTiles() {
    return adjacentTiles;
  }

  /**
   * Copies the contents of {@code adjTiles} into {@link #adjacentTiles}.
   *
   * @param adjTiles the array to be copied from
   * @throws NullPointerException     if {@code adjTiles == null}
   * @throws IllegalArgumentException if {@code adjTiles.length != {@link Constants#NUM_SIDES}}
   */
  public final void setAdjacentTiles(Tile[] adjTiles) {
    Objects.requireNonNull(adjTiles);
    if (adjTiles.length != Constants.NUM_SIDES) {
      throw new IllegalArgumentException("I need " + Constants.NUM_SIDES + " sides!");
    }
    System.arraycopy(adjTiles, 0, adjacentTiles, 0, Constants.NUM_SIDES);
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

  public Player getOwner() {
    return owner;
  }

  public void setOwner(Player owner) {
    this.owner = owner;
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
   *
   * @param unit the unit to be spawned
   * @return true if the unit can be spawned here, false otherwise
   */
  public boolean isSpawnable(Unit unit) {
    return getUnit() == null && unit.getOwner() == getOwner();
  }

  /**
   * Can the given unit move to this tile?
   *
   * @param unit the unit to be moved (non-null)
   * @return true if the unit can move here, false otherwise
   * @throws NullPointerException if {@code unit == null}
   */
  public boolean isMoveable(Unit unit) {
    Objects.requireNonNull(unit); // Check if the new unit isn't null
    return this.unit == null; // True if this tile is empty (this.unit is the unit on this tile)
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof Tile)) {
      return false;
    }

    Tile tile = (Tile) o;
    return pos.equals(tile.pos);
  }

  @Override
  public int hashCode() {
    return pos.hashCode();
  }
}
