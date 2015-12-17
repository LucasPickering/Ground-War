package groundwar.screen.tileeffect;

import groundwar.Constants;
import groundwar.screen.TextureHandler;

public class TileEffect {

  public enum Layer {
    BG, FG, UNIT
  }

  public static TileEffect mouseOver = new TileEffect(Layer.BG, Constants.TILE_BG_NAME, 0xccbbbbbb);
  public static TileEffect selected = new TileEffect(Layer.BG, Constants.TILE_BG_NAME, 0xff00ff00);

  private final Layer layer;
  private final String texName;
  private final int color;

  TileEffect(Layer layer, String texName, int color) {
    this.layer = layer;
    this.texName = texName;
    this.color = color;
  }

  public final Layer getLayer() {
    return layer;
  }

  public void draw(int x, int y, int width, int height) {
    TextureHandler.draw(texName, x, y, width, height, color);
  }
}
