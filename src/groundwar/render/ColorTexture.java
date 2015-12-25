package groundwar.render;

import groundwar.GroundWar;
import groundwar.util.Colors;
import groundwar.util.Constants;

public class ColorTexture {

  // Tile overlays
  public static final ColorTexture mouseOver;
  public static final ColorTexture selected;
  public static final ColorTexture moveable;
  public static final ColorTexture attackable;
  public static final ColorTexture invalidSpawning;
  public static final ColorTexture validSpawning;

  static {
    mouseOver = new ColorTexture(Constants.TILE_BG_NAME, Colors.MOUSE_OVER);
    selected = new ColorTexture(Constants.TILE_OUTLINE_NAME, Colors.SELECTED);
    moveable = new ColorTexture(Constants.TILE_OUTLINE_NAME, Colors.MOVEABLE);
    attackable = new ColorTexture(Constants.TILE_OUTLINE_NAME, Colors.ATTACKABLE);
    invalidSpawning = new ColorTexture(Constants.TILE_BG_NAME, Colors.UNIT_SPAWNING_INVALID);
    validSpawning = new ColorTexture(Constants.TILE_BG_NAME, Colors.UNIT_SPAWNING_VALID);
  }

  private final String texName;
  private final int color;

  public ColorTexture(String texName, int color) {
    this.texName = texName;
    this.color = color;
  }

  public void draw(int x, int y, int width, int height) {
    GroundWar.groundWar.getRenderer().drawTexture(texName, x, y, width, height, color);
  }
}
