package groundwar.board.tile;

import java.util.Objects;

import groundwar.board.Board;
import groundwar.board.Flag;
import groundwar.board.Player;
import groundwar.board.unit.Unit;
import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Direction;
import groundwar.util.Point;

public class Tile {

    /**
     * The position of this tile within the board. Non-null.
     */
    private final Point pos;

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
    private Flag flag;

    public Tile(Point pos) {
        this(pos, Colors.TILE_BG, Colors.TILE_OUTLINE);
    }

    public Tile(Point pos, int backgroundColor, int outlineColor) {
        this(pos, null, backgroundColor, outlineColor);
    }

    public Tile(Point pos, Player owner) {
        this(pos, owner, owner.getInfo().secondaryColor, owner.getInfo().primaryColor);
    }

    public Tile(Point pos, Player owner, int backgroundColor, int outlineColor) {
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

    public final Tile getAdjacentTile(Direction dir) {
        return adjacentTiles[dir.ordinal()];
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

    public final Point getPos() {
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
        if (unitCanGrabFlag()) {
            unit.grabFlag(flag);
            setFlag(null);
        }
        onUnitChange();
    }

    public final boolean hasUnit() {
        return unit != null;
    }

    public final Flag getFlag() {
        return flag;
    }

    public final void setFlag(Flag flag) {
        this.flag = flag;
    }

    private boolean unitCanGrabFlag() {
        return unit != null && flag != null && unit.canCarryFlag() && !unit.hasFlag()
               && unit.getOwner() != flag.getOwner();
    }

    /**
     * Should the game end? Some tiles have the power to end the game, i.e. fort tiles.
     *
     * @return true if the game should end, false if it should keep going
     */
    public boolean shouldGameEnd() {
        return false;
    }

    /**
     * Gets the distance between this tile and another tile located at the given point.
     *
     * @param p the other point
     * @return the distance between this tile and {@param p}
     */
    public final int distanceTo(Point p) {
        final int x1 = pos.getX();
        final int y1 = pos.getY();
        final int x2 = p.getX();
        final int y2 = p.getY();
        return (Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(-x1 - y1 + x2 + y2)) / 2;
    }

    /**
     * Convenice method for {@link #distanceTo(Point)}.
     *
     * @param tile the other tile
     * @return {@code distanceTo(tile.getPos()}
     */
    public final int distanceTo(Tile tile) {
        return distanceTo(tile.getPos());
    }

    /**
     * Is the given tile adjacent to this tile? Two tiles are adjacent if the distance between them
     * is exactly 1.
     *
     * @param tile the other tile (non-null)
     * @return true if this tile and the other are adjacent, false otherwise
     * @throws NullPointerException if {@code tile == null}
     */
    public boolean isAdjacentTo(Tile tile) {
        Objects.requireNonNull(tile);
        return distanceTo(tile) == 1;
    }

    /**
     * Does this tile contain the {@link Point} p? p is a point in screen-space, not in tile-space.
     * This is essentially used to check if the mouse is over this tile.
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
        return hasUnit() && unit.getOwner() == currentPlayer;
    }

    /**
     * Can the given unit be spawned on this tile?
     *
     * @param unit the unit to be spawned (non-null)
     * @return true if the unit can be spawned here, false otherwise
     * @throws NullPointerException if {@code unit == null}
     */
    public boolean isSpawnable(Unit unit) {
        Objects.requireNonNull(unit);
        return !hasUnit() && unit.getOwner() == getOwner();
    }

    /**
     * Can the given unit move to this tile?
     *
     * @param mover the unit to be moved (non-null)
     * @return true if the unit can move here, false otherwise
     * @throws NullPointerException if {@code mover == null}
     */
    public boolean isMoveable(Unit mover) {
        Objects.requireNonNull(mover);
        return !hasUnit();
    }

    /**
     * Checks if this tile is attackable by the given unit. In order to be attackable, this tile
     * must have a unit owned by another player than the owner of {@code attacker}.
     *
     * @param attacker the attacking unit (non-null)
     * @return true if this tile can be attacked, false otherwise
     * @throws NullPointerException if {@code attacker == null}
     */
    public boolean isAttackable(Unit attacker) {
        Objects.requireNonNull(attacker);
        return hasUnit() && unit.getOwner() != attacker.getOwner();
    }

    /**
     * Inflicts the given specified amount of damage to {@link #unit}.
     *
     * @param damage the amount of damage to inflict (non-negative)
     */
    public void hurtUnit(int damage) {
        unit.inflictDamage(damage);
        if (unit.isDead()) {
            killUnit();
        }
    }

    private void killUnit() {
        if (unit.hasFlag()) {
            unit.dropFlag();
            unit.onKilled();
        }
        unit = null;
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

    @Override
    public String toString() {
        return "Tile@" + pos.toString();
    }

    // Events

    /**
     * Called <i>directly after</i> {@link #adjacentTiles} is populated.
     */
    public void onSetAdjacents() {
    }

    /**
     * Called <i>directly after</i> the unit on this tile changes.
     */
    public void onUnitChange() {
    }

    /**
     * Called by {@link Board} at the end of each turn. This is called before the current player is
     * changed, and before any movement or other values are reset.
     */
    public void onEndTurn() {
    }
}
