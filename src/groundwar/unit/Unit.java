package groundwar.unit;

import java.util.Objects;

import groundwar.GroundWar;
import groundwar.Player;

public class Unit {

  private UnitType type;
  private Player owner;
  private int movesPerTurn;
  private int movesRemaining;

  /**
   * Initializes the unit. This <i>must</i> be called immediately after the constructor, and can only
   * be called once per instance.
   *
   * @param type         the type of the unit (non-null)
   * @param owner        the owner of the unit (non-null)
   * @param movesPerTurn the amount of tiles that the unit can move per turn (positive)
   * @return this
   * @throws IllegalStateException    if the unit has already been initialized
   * @throws NullPointerException     if {@code type == null} or {@code owner == null}
   * @throws IllegalArgumentException if {@code movesPerTurn <= 0}
   */
  public Unit init(UnitType type, Player owner, int movesPerTurn) {
    // A unit can only be initliazed once
    if (this.type != null) {
      throw new IllegalStateException("This unit has already been initialized!");
    }
    Objects.requireNonNull(type);
    Objects.requireNonNull(owner);
    if (movesPerTurn <= 0) {
      throw new IllegalArgumentException("movesPerTurn must be positive");
    }

    this.type = type;
    this.owner = owner;
    this.movesPerTurn = movesPerTurn;
    resetMoves();
    GroundWar.groundWar.getTextureHandler().loadTexture(type.name);
    return this;
  }

  public UnitType getType() {
    return type;
  }

  public Player getOwner() {
    return owner;
  }

  public String getName() {
    return type.name;
  }

  public int getCost() {
    return type.cost;
  }

  public int getMovesRemaining() {
    return movesRemaining;
  }

  /**
   * Can this unit move the given distance? In other words, does this unit have enough movement points
   * remaining to move the given distance?
   *
   * @param distance the distance to move, in tiles (positive)
   * @return true if this unit has enough movement points remaining, false otherwise
   */
  public boolean canMove(int distance) {
    if (distance <= 0) {
      throw new IllegalArgumentException("distance must be positive");
    }
    return distance <= movesRemaining;
  }

  /**
   * Resets {@link #movesRemaining} to {@link #movesPerTurn}. Should be called at the end of each
   * turn.
   */
  public void resetMoves() {
    movesRemaining = movesPerTurn;
  }

  /**
   * Move this unit the given distance. This doesn't <i>actually</i> move the unit, instead it just
   * subtracts {@code distance} from {@link #movesRemaining}.
   */
  public void move(int distance) {
    if (!canMove(distance)) {
      throw new IllegalStateException("Not enough movement points to move!");
    }
    movesRemaining -= distance;
  }
}
