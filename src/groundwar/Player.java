package groundwar;

import groundwar.util.Colors;
import groundwar.util.Constants;

public enum Player {

  ORANGE(Colors.ORANGE, Colors.ORANGE2), BLUE(Colors.BLUE, Colors.BLUE2);

  public final int primaryColor;
  public final int secondaryColor;
  private int money = Constants.START_MONEY;

  Player(int primaryColor, int secondaryColor) {
    this.primaryColor = primaryColor;
    this.secondaryColor = secondaryColor;
  }

  public int getMoney() {
    return money;
  }

  /**
   * Increases this player's money by the given amount
   *
   * @param amt the amount to be added, (non-negative)
   * @throws IllegalStateException if {@code amt > money}
   */
  public void incrMoney(int amt) {
    if (amt < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    money += amt;
  }

  /**
   * Decreases this player's money by the given amount.
   *
   * @param amt the amount to be taken away, must be <= money (non-negative)
   * @throws IllegalArgumentException if {@code amt < 0}
   * @throws IllegalStateException    if {@code amt > money}
   */
  public void decrMoney(int amt) {
    if (amt < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    if (amt > money) {
      throw new IllegalStateException("Amount must be <= money");
    }
    money -= amt;
  }

  /**
   * Gets the other player.
   *
   * @return if this is ORANGE, return BLUE, if this is BLUE, return ORANGE
   */
  public Player other() {
    return values()[1 - ordinal()];
  }
}
