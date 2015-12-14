package groundwar.tile;

import groundwar.Constants;
import groundwar.HexPoint;

public class Tile {

  private final HexPoint pos;
  private int backgroundColor;
  private int outlineColor;

  public Tile(HexPoint pos) {
    this(pos, Constants.TILE_BG_COLOR, Constants.TILE_OUTLINE_COLOR);
  }

  public Tile(HexPoint pos, int backgroundColor, int outlineColor) {
    this.pos = pos;
    this.backgroundColor = backgroundColor;
    this.outlineColor = outlineColor;
  }

  public HexPoint getPos() {
    return pos;
  }

  public int getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(int backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public int getOutlineColor() {
    return outlineColor;
  }

  public void setOutlineColor(int outlineColor) {
    this.outlineColor = outlineColor;
  }

  /**
   * Gets the distance between this tile and another tile located at the given point.
   *
   * @param p the other point
   * @return the distance between this tile and {@param p}
   */
  public int distanceTo(HexPoint p) {
    return pos.distanceTo(p);
  }

  /**
   * Gets the distance between this tile and another tile.
   *
   * @param tile the other tile
   * @return the distance between this tile and {@param tile}
   */
  public int distanceTo(Tile tile) {
    return distanceTo(tile.getPos());
  }
}
