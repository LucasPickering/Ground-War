package groundwar.screen;

import groundwar.screen.event.EventHandler;
import groundwar.screen.event.KeyEvent;
import groundwar.screen.event.MouseButtonEvent;

/**
 * A {@code ScreenElement} is anything that be drawn into the game window. A {@code ScreenElement} can
 * hold children elements, and is responsible for calling {@link #draw} for those children. How each
 * {@code ScreenElement} contains and handles its children is up to each element.
 */
public abstract class ScreenElement {

  /**
   * The {@link EventHandler} that is called when a {@link KeyEvent} occurs while this element is
   * active.
   */
  private EventHandler<KeyEvent> keyHandler = this::onKey;

  /**
   * The {@link EventHandler} that is called when a {@link MouseButtonEvent} occurs while this element
   * is active and {@link #contains} returns true for the cursor's current position.
   */
  private EventHandler<MouseButtonEvent> mouseButtonHandler = this::onClicked;

  /**
   * Draws this screen onto the window.
   *
   * @param window the window to be drawn onto
   * @param mouseX the current x position of the mouse
   * @param mouseY the current y position of the mouse
   */
  public abstract void draw(long window, int mouseX, int mouseY);

  /**
   * Checks if this element contains the given x and y coordinates. Bounds checking is inclusive on
   * all sides.
   *
   * @param x the x coordinate to be checked
   * @param y the y coordinate to be checked
   * @return true if (x, y) falls inside this element, false otherwise
   */
  public abstract boolean contains(int x, int y);

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
    keyHandler.handle(event);
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
    mouseButtonHandler.handle(event);
  }

  /**
   * Called when this element is clicked. Specifically, this method is called when a mouse event is
   * detected inside the window and {@link #contains} returns true for the coordinates at which the
   * click occurred.
   *
   * @param event the event that occurred
   */
  public void onClicked(MouseButtonEvent event) {
    // By default, nothing is done on click
  }
}
