package groundwar.screen.gui;

import org.lwjgl.opengl.GL11;

import groundwar.Point;
import groundwar.screen.Texture;
import groundwar.screen.TextureHandler;

public class Button extends GuiElement {

  private String text;

  public Button(long window, TextureHandler textureHandler, int x, int y, int width, int height,
                String text) {
    super(window, textureHandler, x, y, width, height);
    this.text = text;
  }

  @Override
  public void draw(Point mousePos) {
  }
}
