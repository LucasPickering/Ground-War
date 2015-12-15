package groundwar.screen.gui;

import org.lwjgl.opengl.GL11;

import groundwar.Point;
import groundwar.screen.Texture;
import groundwar.screen.TextureHandler;

public class Button extends GuiElement {

  private String text;
  private final Texture texture;

  public Button(long window, int x, int y, int width, int height, String text) {
    super(window, x, y, width, height);
    this.text = text;
    texture = TextureHandler.loadTexture("/textures/tile_background.png");
  }

  @Override
  public void draw(Point mousePos) {
  }
}
