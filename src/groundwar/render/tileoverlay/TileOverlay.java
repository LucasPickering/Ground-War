package groundwar.render.tileoverlay;

import java.util.Arrays;

import groundwar.GroundWar;
import groundwar.tile.Tile;
import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Point;

public class TileOverlay {

  protected static class ColorTexture {

    private final String texName;
    private final int color;

    protected ColorTexture(String texName, int color) {
      this.texName = texName;
      this.color = color;
    }
  }

  public static TileOverlay mouseOver = new TileOverlay(Constants.TILE_BG_NAME, Colors.MOUSE_OVER);
  public static TileOverlay selected = new TileOverlay(Constants.TILE_OUTLINE_NAME,
                                                       Colors.SELECTED);
  public static TileOverlay moveable = new TileOverlay(Constants.TILE_OUTLINE_NAME, Colors.MOVEABLE);
  public static TileOverlay attackable = new TileOverlay(Constants.TILE_OUTLINE_NAME,
                                                         Colors.ATTACKABLE);

  private final ColorTexture[] textures;

  TileOverlay(String texName, int color) {
    this(new ColorTexture(texName, color));
  }

  TileOverlay(ColorTexture... textures) {
    this.textures = textures;
  }

  public void draw(Tile tile) {
    final Point p = tile.getScreenPos();

    // Draw each texture
    Arrays.stream(textures).forEach(tex -> GroundWar.groundWar.getRenderer()
        .drawTexture(tex.texName, p.getX(), p.getY(),
                     Constants.TILE_WIDTH, Constants.TILE_HEIGHT, tex.color));
  }
}
