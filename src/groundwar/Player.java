package groundwar;

public enum Player {

  RED(Constants.RED_COLOR, Constants.RED2_COLOR), BLUE(Constants.BLUE_COLOR, Constants.BLUE2_COLOR);

  public final int primaryColor;
  public final int secondaryColor;

  Player(int primaryColor, int secondaryColor) {
    this.primaryColor = primaryColor;
    this.secondaryColor = secondaryColor;
  }
}
