package groundwar.render.gui;

import groundwar.util.Point;
import groundwar.render.ScreenElement;

public abstract class GuiElement extends ScreenElement {

  private int x;
  private int y;
  private int width;
  private int height;

  /**
   * Constructs a new {@code GuiElement} with the given coordinates and size.
   *
   * @param x      the x position of the element
   * @param y      the y position of the element
   * @param width  the width of the element (positive)
   * @param height the height of the element (positive)
   * @throws IllegalArgumentException if x or y is negative, or width or height is non-positive
   */
  protected GuiElement(long window, int x, int y, int width, int height) {
    super(window);
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Width or height out of bounds");
    }
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public boolean contains(Point p) {
    return x <= p.getX() && p.getX() <= x + width && y <= p.getY() && p.getY() <= y + height;
  }
}
