package groundwar.board;

import groundwar.util.Colors;

public enum PlayerColor {

  ORANGE("Orangge", Colors.ORANGE, Colors.ORANGE2), BLUE("Blue", Colors.BLUE, Colors.BLUE2);

  public final String displayName;
  public final int primaryColor;
  public final int secondaryColor;

  PlayerColor(String displayName, int primaryColor, int secondaryColor) {
    this.displayName = displayName;
    this.primaryColor = primaryColor;
    this.secondaryColor = secondaryColor;
  }
}
