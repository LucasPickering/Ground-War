package groundwar.screen;

public enum TileEffect {

  DEFAULT(-1, -1), MOUSE_OVER(0xffffff, -1);

  private final int backgroundColor;
  private final int outlineColor;

  TileEffect(int backgroundColor, int outlineColor) {
    this.backgroundColor = backgroundColor;
    this.outlineColor = outlineColor;
  }

  public int getBackgroundColor(int defaultColor) {
    return backgroundColor < 0 ? defaultColor : backgroundColor;
  }

  public int getOutlineColor(int defaultColor) {
    return outlineColor < 0 ? defaultColor : outlineColor;
  }
}
