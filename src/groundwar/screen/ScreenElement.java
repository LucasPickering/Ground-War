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
   */
  void draw(long window);
}
