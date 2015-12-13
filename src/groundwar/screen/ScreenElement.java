package groundwar.screen;

/**
 * A {@code ScreenElement} is anything that be drawn into the game window. A {@code ScreenElement} can
 * hold children elements, and is responsible for calling {@link #draw} for those children. How each
 * {@code ScreenElement} contains and handles its children is up to each element.
 */
public interface ScreenElement {

  /**
   * Draws this screen onto the window.
   *
   * @param window the window to be drawn onto
   * @param mouseX the current x position of the mouse
   * @param mouseY the current y position of the mouse
   */
  void draw(long window, int mouseX, int mouseY);

  /**
   * Checks if the given x and y coordinates fall inside this element.
   *
   * @param x the x coordinate to be checked
   * @param y the y coordinate to be checked
   * @return true if (x, y) falls inside this element, false otherwise
   */
  boolean insideElement(int x, int y);

  /**
   * Called when this element is clicked. Specifically, this method is called when a mouse event is
   * detected inside the window and {@link #insideElement} returns true for the coordinates at which
   * the click occurred.
   *
   * @param mouseX the x coordinate of the mouse click
   * @param mouseY the y coordinate of the mouse click
   * @param button the mouse button that was clicked
   * @param mods   bitfield describing which modifier keys were held down
   */
  void onClicked(int mouseX, int mouseY, int button, int mods);
}
