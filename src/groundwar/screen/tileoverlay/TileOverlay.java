package groundwar.screen.tileoverlay;

import groundwar.GroundWar;
import groundwar.constants.Colors;
import groundwar.constants.Constants;

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

  public void draw(int x, int y, int width, int height) {
    // Draw each texture
    for (ColorTexture tex : textures) {
      GroundWar.groundWar.getTextureHandler().draw(tex.texName, x, y, width, height, tex.color);
    }
  }
}
