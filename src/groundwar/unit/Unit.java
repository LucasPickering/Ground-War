package groundwar.unit;

import java.util.Objects;

import groundwar.Player;
import groundwar.screen.TextureHandler;

public class Unit {

  private UnitType type;
  private Player owner;
  private int movementPointsPerTurn;
  private int movementPoints;

  /**
   * Initializes the unit. This <i>must</i> be called immediately after the constructor, and can only
   * be called once per instance.
   *
   * @param type                  the type of the unit (non-null)
   * @param owner                 the owner of the unit (non-null)
   * @param movementPointsPerTurn the amount of tiles that the unit can move per turn (positive)
   * @return this
   * @throws IllegalStateException    if the unit has already been initialized
   * @throws NullPointerException     if {@code type == null} or {@code owner == null}
   * @throws IllegalArgumentException if {@code movementPointsPerTurn <= 0}
   */
  public Unit init(UnitType type, Player owner, int movementPointsPerTurn) {
    // A unit can only be initliazed once
    if (this.type != null) {
      throw new IllegalStateException("This unit has already been initialized!");
    }
    Objects.requireNonNull(type);
    Objects.requireNonNull(owner);
    if (movementPointsPerTurn <= 0) {
      throw new IllegalArgumentException("movementPointsPerTurn must be positive");
    }

    this.type = type;
    this.owner = owner;
    this.movementPointsPerTurn = movementPointsPerTurn;
    resetMovementPoints();
    TextureHandler.loadTexture(type.name);
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

  public int getMovementPoints() {
    return movementPoints;
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
    return distance <= movementPoints;
  }

  /**
   * Resets {@link #movementPoints} to {@link #movementPointsPerTurn}. Should be called at the end of
   * each turn.
   */
  public void resetMovementPoints() {
    movementPoints = movementPointsPerTurn;
  }

  /**
   * Move this unit the given distance. This doesn't <i>actually</i> move the unit, instead it just
   * subtracts {@code distance} from {@link #movementPoints}.
   */
  public void move(int distance) {
    if (!canMove(distance)) {
      throw new IllegalStateException("Not enough movement points to move!");
    }
    movementPoints -= distance;
  }
}
