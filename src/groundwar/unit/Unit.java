package groundwar.unit;

import java.util.Objects;

import groundwar.GroundWar;
import groundwar.Player;

public class Unit {

  private UnitType type;
  private Player owner;
  private int movesRemaining;
  private int health;

  /**
   * Initializes the unit. This <i>must</i> be called immediately after the constructor, and can only
   * be called once per instance.
   *
   * @param type  the type of the unit (non-null)
   * @param owner the owner of the unit (non-null)
   * @return this
   * @throws IllegalStateException if the unit has already been initialized
   * @throws NullPointerException  if {@code type == null} or {@code owner == null}
   */
  public Unit init(UnitType type, Player owner) {
    // A unit can only be initliazed once
    if (this.type != null) {
      throw new IllegalStateException("This unit has already been initialized!");
    }
    Objects.requireNonNull(type);
    Objects.requireNonNull(owner);

    this.type = type;
    this.owner = owner;
    resetMoves();
    health = type.maxHealth;
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
   * Subtracts the given distance from {@link #movesRemaining}.
   */
  public void useMoves(int distance) {
    if (!canMove(distance)) {
      throw new IllegalStateException("Not enough movement points to move!");
    }
    movesRemaining -= distance;
  }

  public int getHealth() {
    return health;
  }

  /**
   * Inflicts the given amount of damage to this unit. Returns whether or not this unit is still
   * alive. If the return value is false, this unit is dead and should be removed from the board.
   *
   * @param damage the amount of damage to inflict
   * @return true if this unit is alive {@code damage > 0}, false otherwise
   */
  public boolean inflictDamage(int damage) {
    health -= damage;
    return health > 0;
  }

  /**
   * Resets {@link #movesRemaining}. Should be called at the end of each turn.
   */
  public void resetMoves() {
    movesRemaining = type.movesPerTurn;
  }
}
