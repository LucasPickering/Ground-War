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

  public boolean canMove(int distance) {
    if (distance <= 0) {
      throw new IllegalArgumentException("distance must be positive");
    }
    return distance <= movementPoints;
  }

  public void resetMovementPoints() {
    movementPoints = movementPointsPerTurn;
  }

  public void move(int distance) {
    if (!canMove(distance)) {
      throw new IllegalStateException("Not enough movement points to move!");
    }
    movementPoints -= distance;
  }
}
