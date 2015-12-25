package groundwar.board;

import groundwar.util.Colors;

public enum PlayerColor {

  ORANGE(Colors.ORANGE, Colors.ORANGE2), BLUE(Colors.BLUE, Colors.BLUE2);

  public final int primaryColor;
  public final int secondaryColor;

  PlayerColor(int primaryColor, int secondaryColor) {
    this.primaryColor = primaryColor;
    this.secondaryColor = secondaryColor;
  }

  /**
   * Gets the other player.
   *
   * @return if this is ORANGE, return BLUE, if this is BLUE, return ORANGE
   */
  public PlayerColor other() {
    return values()[1 - ordinal()];
  }
}
