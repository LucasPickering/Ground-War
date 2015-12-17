package groundwar.screen.tileoverlay;

import java.util.Arrays;

import groundwar.constants.Colors;
import groundwar.constants.Constants;
import groundwar.screen.TextureHandler;

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
  public static TileOverlay movable = new TileOverlay(Constants.TILE_OUTLINE_NAME, Colors.MOVEABLE);

  private final ColorTexture[] textures;

  TileOverlay(String texName, int color) {
    this(new ColorTexture(texName, color));
  }

  TileOverlay(ColorTexture... textures) {
    this.textures = textures;
  }

  public void draw(int x, int y, int width, int height) {
    Arrays.stream(textures)
        .forEach(tex -> TextureHandler.draw(tex.texName, x, y, width, height, tex.color));
  }
}
