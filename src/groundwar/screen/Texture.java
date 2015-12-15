package groundwar.screen;

import java.io.IOException;

public class Texture {

  private final int textureID;

  public Texture(int textureID) throws IOException {
    this.textureID = textureID;
  }

  public int getTextureID() {
    return textureID;
  }

  /**
   * Convenience method for {@link #draw(int, int, int, int, int)} with white as the color.
   *
   * @param x      the x-location of the top-left of the texture
   * @param y      the y-location of the top-left of the texture
   * @param width  the width of the texture
   * @param height the height of the texture
   */
  public void draw(int x, int y, int width, int height) {
    draw(x, y, width, height, 0xffffff);
  }

  /**
   * Convenience method for {@link TextureHandler#draw}.
   *
   * @param x      the x-location of the top-left of the texture
   * @param y      the y-location of the top-left of the texture
   * @param width  the width of the texture
   * @param height the height of the texture
   * @param color  the color of the texture
   */
  public void draw(int x, int y, int width, int height, int color) {
    TextureHandler.draw(this, x, y, width, height, color);
  }
}
