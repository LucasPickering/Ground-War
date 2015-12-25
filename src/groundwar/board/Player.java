package groundwar.board;

import groundwar.util.Constants;

public class Player {

  private final PlayerColor playerColor;
  private int gold = Constants.STARTING_GOLD;

  public Player(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public int getPrimaryColor() {
    return playerColor.primaryColor;
  }

  public int getSecondaryColor() {
    return playerColor.secondaryColor;
  }

  public int getGold() {
    return gold;
  }

  /**
   * Increases this player's gold by the given amount.
   *
   * @param amt the amount to be added, (non-negative)
   * @throws IllegalStateException if {@code amt > {@link #gold}}
   */
  public void incrGold(int amt) {
    if (amt < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    gold += amt;
  }

  /**
   * Decreases this player's gold by the given amount.
   *
   * @param amt the amount to be taken away, must be <= {@link #gold} (non-negative)
   * @throws IllegalArgumentException if {@code amt < 0}
   * @throws IllegalStateException    if {@code amt > {@link #gold}}
   */
  public void decrGold(int amt) {
    if (amt < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    if (amt > gold) {
      throw new IllegalStateException("Amount must be <= gold");
    }
    gold -= amt;
  }

  @Override
  public String toString() {
    return playerColor.displayName + " Player";
  }
}
