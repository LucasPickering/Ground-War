package groundwar.render.screen;

import groundwar.GroundWar;
import groundwar.render.Renderer;
import groundwar.util.Point;

/**
 * A {@code ScreenElement} is anything that be drawn into the game window. A {@code ScreenElement} can
 * hold children elements, and is responsible for calling {@link #draw} for those children. How each
 * {@code ScreenElement} contains and handles its children is up to each element.
 */
public interface ScreenElement {

  /**
   * Draws this screen onto the window.
   *
   * @param mousePos the current position of the mouse cursor
   */
  void draw(Point mousePos);

  /**
   * Checks if this element contains the given point. Bounds checking is inclusive on all sides.
   *
   * @param p the point to be checked
   * @return true if p falls inside this element, false otherwise
   */
  boolean contains(Point p);

  default Renderer renderer() {
    return GroundWar.groundWar.getRenderer();
  }
}
