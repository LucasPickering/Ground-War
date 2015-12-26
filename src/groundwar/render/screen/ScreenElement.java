package groundwar.render.screen;

import groundwar.GroundWar;
import groundwar.render.Renderer;
import groundwar.util.Point;
import groundwar.render.event.EventHandler;
import groundwar.render.event.KeyEvent;
import groundwar.render.event.MouseButtonEvent;

/**
 * A {@code ScreenElement} is anything that be drawn into the game window. A {@code ScreenElement} can
 * hold children elements, and is responsible for calling {@link #draw} for those children. How each
 * {@code ScreenElement} contains and handles its children is up to each element.
 *
 * Subclasses can handle events in one of two ways: <ul> <li>Override {@link #onKey}, {@link
 * #onClick}, etc.</li> <li>Pass in a new handler to {@link #setKeyHandler}, {@link
 * #setMouseButtonHandler}, etc.</li> </ul>
 */
public abstract class ScreenElement {

  protected final long window;
  protected final Renderer renderer = GroundWar.groundWar.getRenderer();

  protected ScreenElement(long window) {
    this.window = window;
  }

  /**
   * The {@link EventHandler} that is called when a {@link KeyEvent} occurs while this element is
   * active.
   */
  private EventHandler<KeyEvent> keyHandler = this::onKey;

  /**
   * The {@link EventHandler} that is called when a {@link MouseButtonEvent} occurs while this element
   * is active and {@link #contains} returns true for the cursor's current position.
   */
  private EventHandler<MouseButtonEvent> mouseButtonHandler = this::onClick;

  /**
   * Draws this screen onto the window.
   *
   * @param mousePos the current position of the mouse cursor
   */
  public abstract void draw(Point mousePos);

  /**
   * Checks if this element contains the given point. Bounds checking is inclusive on all sides.
   *
   * @param p the point to be checked
   * @return true if p falls inside this element, false otherwise
   */
  public abstract boolean contains(Point p);

  /**
   * Sets the {@link EventHandler} that should be used to handle key events.
   *
   * @param keyHandler the handler to be used
   */
  public final void setKeyHandler(EventHandler<KeyEvent> keyHandler) {
    this.keyHandler = keyHandler;
  }

  /**
   * Called from {@link groundwar.GroundWar GroundWar} when a key event occurs while this element is
   * active.
   *
   * @param event the {@link KeyEvent} that occurred
   */
  public final void handleKey(KeyEvent event) {
    if (keyHandler != null) {
      keyHandler.handle(event);
    }
  }

  public void onKey(KeyEvent event) {
    // By default, nothing is done on key press
  }

  /**
   * Sets the {@link EventHandler} that should be used to handle mouse button events.
   *
   * @param mouseButtonHandler the handler to be used
   */
  public final void setMouseButtonHandler(EventHandler<MouseButtonEvent> mouseButtonHandler) {
    this.mouseButtonHandler = mouseButtonHandler;
  }

  /**
   * Called from {@link groundwar.GroundWar GroundWar} when a mouse button event occurs. This should
   * only be called if {@link #contains} returns true for the cursor's current position.
   *
   * @param event the event that occurred
   */
  public final void handleMouseButton(MouseButtonEvent event) {
    if (mouseButtonHandler != null) {
      mouseButtonHandler.handle(event);
    }
  }

  /**
   * Called when this element is clicked. Specifically, this method is called when a mouse event is
   * detected inside the window and {@link #contains} returns true for the coordinates at which the
   * click occurred.
   *
   * @param event the event that occurred
   */
  public void onClick(MouseButtonEvent event) {
    // By default, nothing is done on click
  }
}
