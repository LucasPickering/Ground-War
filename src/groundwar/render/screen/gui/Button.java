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
    renderer.drawRect(getX(), getY(), getWidth(), getHeight(), 0xff00ffff);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    renderer.drawText(Constants.FONT_SIZE_UI, text, getX(), getY(), TextAlignment.CENTER);
  }
}
