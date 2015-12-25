package groundwar.render;

import groundwar.GroundWar;
import groundwar.board.tile.Tile;
import groundwar.board.unit.UnitType;
import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Point;

public class TileOverlay {

  public static TileOverlay mouseOver = new TileOverlay(Constants.TILE_BG_NAME, Colors.MOUSE_OVER);
  public static TileOverlay selected = new TileOverlay(Constants.TILE_OUTLINE_NAME, Colors.SELECTED);
  public static TileOverlay moveable = new TileOverlay(Constants.TILE_OUTLINE_NAME, Colors.MOVEABLE);
  public static TileOverlay attackable = new TileOverlay(Constants.TILE_OUTLINE_NAME,
                                                         Colors.ATTACKABLE);

  public static TileOverlay[] spawningUnits = new TileOverlay[UnitType.values().length];
  public static TileOverlay invalidSpawning = new TileOverlay(Constants.TILE_BG_NAME,
                                                              Colors.UNIT_SPAWNING_INVALID);
  public static TileOverlay validSpawning = new TileOverlay(Constants.TILE_BG_NAME,
                                                            Colors.UNIT_SPAWNING_VALID);

  static {
    for (UnitType type : UnitType.values()) {
      spawningUnits[type.ordinal()] = new TileOverlay(type.textureName, Colors.UNIT_SPAWNING);
    }
  }

  private final String texName;
  private final int color;

  TileOverlay(String texName, int color) {
    this.texName = texName;
    this.color = color;
  }

  public void draw(Tile tile) {
    final Point p = tile.getScreenPos();
    // Draw the texture
    GroundWar.groundWar.getRenderer().drawTexture(texName, p.getX(), p.getY(),
                                                  Constants.TILE_WIDTH, Constants.TILE_HEIGHT, color);
  }
}
