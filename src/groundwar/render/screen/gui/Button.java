package groundwar.render.screen.gui;

import org.lwjgl.opengl.GL11;

import groundwar.render.TextAlignment;
import groundwar.util.Constants;
import groundwar.util.Point;

public class Button extends GuiElement {

  private String text;

  public Button(long window, int x, int y, int width, int height, String text) {
    super(window, x, y, width, height);
    this.text = text;
  }

  @Override
  public void draw(Point mousePos) {
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    renderer.drawRect(0, 0, getWidth(), getHeight(), 0xff000000);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    renderer.drawText(Constants.FONT_SIZE_UI, text, getWidth() / 2, 0, TextAlignment.CENTER);
  }
}
