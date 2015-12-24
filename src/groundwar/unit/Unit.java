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
    GroundWar.groundWar.getRenderer().loadTexture(type.textureName);
    return this;
  }

  public final UnitType getType() {
    return type;
  }

  public final Player getOwner() {
    return owner;
  }

  public final int getMovesRemaining() {
    return movesRemaining;
  }

  /**
   * Can this unit move the given distance? In other words, does this unit have enough movement points
   * remaining to move the given distance?
   *
   * @param distance the distance to move, in tiles (positive)
   * @return true if this unit has enough movement points remaining, false otherwise
   */
  public final boolean canMove(int distance) {
    if (distance <= 0) {
      throw new IllegalArgumentException("distance must be positive");
    }
    return distance <= movesRemaining;
  }

  /**
   * Subtracts the given distance from {@link #movesRemaining}.
   */
  public final void useMoves(int distance) {
    if (!canMove(distance)) {
      throw new IllegalStateException("Not enough movement points to move!");
    }
    movesRemaining -= distance;
  }

  public final int getHealth() {
    return health;
  }

  /**
   * Inflicts the given amount of damage to this unit.
   *
   * @param damage the amount of damage to inflict (non-negative)
   * @return true if the unit is still alive, false if it is now dead
   * @throws IllegalArgumentException if {@code damage < 0}
   */
  public final boolean inflictDamage(int damage) {
    if (damage < 0) {
      throw new IllegalArgumentException("Can't inflict negative damage!");
    }
    health -= damage;
    if (isDead()) {
      onKilled();
      return false;
    }
    return true;
  }

  /**
   * Is this unit dead?
   *
   * @return {@code health <= 0}
   */
  public final boolean isDead() {
    return health <= 0;
  }

  /**
   * Resets {@link #movesRemaining}. Should be called at the end of each turn.
   */
  public final void resetMoves() {
    movesRemaining = type.movesPerTurn;
  }

  protected void onKilled() {
  }
}
