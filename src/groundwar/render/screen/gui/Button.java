package groundwar.render.screen.gui;

import org.lwjgl.opengl.GL11;

import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Point;

public class Button extends GuiElement {

  private String text;

  public Button(long window, int x, int y, String text) {
    this(window, x, y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, text);
  }

  public Button(long window, int x, int y, int width, int height, String text) {
    super(window, x, y, width, height);
    this.text = text;
  }

  @Override
  public void draw(Point mousePos) {
    final boolean mouseOver = contains(mousePos);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    renderer.drawRect(0, 0, getWidth(), getHeight(),
                      mouseOver ? Colors.BUTTON_HIGHLIGHT : Colors.BUTTON_NORMAL);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    renderer.drawText(Constants.FONT_SIZE_UI, text, getWidth() / 2, getHeight() / 2,
                      mouseOver ? Colors.BUTTON_TEXT_HIGHLIGHT : Colors.BUTTON_TEXT_NORMAL,
                      HorizAlignment.CENTER, VertAlignment.CENTER);
  }
}
