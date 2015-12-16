package groundwar.screen;

public enum TileEffect {

  MOUSE_OVER(0xccbbbbbb, 0), SELECTED(0xff00ff00, 0);

  public final int backgroundColor;
  public final int outlineColor;

  TileEffect(int backgroundColor, int outlineColor) {
    this.backgroundColor = backgroundColor;
    this.outlineColor = outlineColor;
  }
}
