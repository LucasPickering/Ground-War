package groundwar.render.gui;

import groundwar.util.Point;

public class Button extends GuiElement {

  private String text;

  public Button(long window, int x, int y, int width, int height, String text) {
    super(window, x, y, width, height);
    this.text = text;
  }

  @Override
  public void draw(Point mousePos) {
  }
}
