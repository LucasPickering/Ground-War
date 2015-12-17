package groundwar.unit;

import groundwar.Player;
import groundwar.screen.TextureHandler;

public class Unit {

  private final UnitType type;
  private final Player owner;
  private final int movementPointsPerTurn;
  private int movementPoints;

  public Unit(UnitType type, Player owner, int movementPointsPerTurn) {
    this.type = type;
    this.owner = owner;
    this.movementPointsPerTurn = movementPointsPerTurn;
    resetMovementPoints();
    TextureHandler.loadTexture(type.name);
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
