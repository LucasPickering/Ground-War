package groundwar.screen.tileeffect;

import java.util.Arrays;

import groundwar.Constants;
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

  public static TileOverlay mouseOver = new TileOverlay(Constants.TILE_BG_NAME, 0x80bbbbbb);
  public static TileOverlay selected = new TileOverlay(Constants.TILE_OUTLINE_NAME, 0xff00ff00);

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
