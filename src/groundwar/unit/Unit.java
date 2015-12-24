package groundwar.unit;

import java.util.Objects;

import groundwar.GroundWar;
import groundwar.Player;

public abstract class Unit {

  private final UnitType type;
  private Player owner;
  private int movesRemaining;
  private int health;

  /**
   * Constructs a new unit.
   *
   * @param type  the type of the unit (non-null)
   * @param owner the owner of the unit (non-null)
   * @throws NullPointerException if {@code type == null} or {@code owner == null}
   */
  protected Unit(UnitType type, Player owner) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(owner);

    this.type = type;
    this.owner = owner;
    resetMoves();
    health = type.maxHealth;
    GroundWar.groundWar.getRenderer().loadTexture(type.textureName);
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

  /**
   * Gets the strength of this unit relative to the given defending unit, as a float [0, 1]. 0 means
   * that this unit does no damage and receives lots of damage. 1 means that this unit receives no
   * damage and does lots of damage. 0.5 means that the units receive equal damage.
   *
   * This should always be called on the <i>attacking</i> unit, as it may provide bias towards the
   * attacking unit.
   *
   * @param defender the defending unit
   * @return a float in the range [0, 1] representing the relative strength
   */
  public abstract float getStrengthVs(Unit defender);

  protected void onKilled() {
  }
}
