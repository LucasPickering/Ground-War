package groundwar.render.screen;

import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

import groundwar.render.event.KeyEvent;
import groundwar.util.Constants;
import groundwar.util.Point;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.screen.gui.GuiElement;

/**
 * A {@code MainScreen} is a type of {@link ScreenElement} that is meant to be a top-level element. A
 * {@code MainScreen} has no parent {@link ScreenElement} and there can only ever be one active {@code
 * MainScreen} at a time. Examples of a {@code MainScreen} include the main menu screen and the
 * in-game screen.
 */
public abstract class MainScreen implements ScreenElement {

  protected final Point center = new Point(Constants.NATIVE_WINDOW_WIDTH / 2,
                                           Constants.NATIVE_WINDOW_HEIGHT / 2);
  private List<GuiElement> guiElements = new LinkedList<>();

  @Override
  public void draw(Point mousePos) {
    // Draw all the GUI elements
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    for (GuiElement element : guiElements) {
      GL11.glPushMatrix();
      GL11.glTranslatef(element.getX(), element.getY(), 0f);
      element.draw(mousePos);
      GL11.glPopMatrix();
    }
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);
  }

  /**
   * Called each frame by the main game loop, after {@link #draw}. To keep this screen as the current
   * screen, return {@code null}. To change to another screen, return that screen. To keep this
   * screen, return {@code this}.
   *
   * @return the screen to change to, or {@code this} to keep this screen
   */
  public abstract MainScreen nextScreen();

  @Override
  public boolean contains(Point p) {
    return true;
  }


  protected void addGuiElement(GuiElement element) {
    guiElements.add(element);
  }

  /**
   * Called when a key is pressed.
   *
   * @param event the event that occurred
   */
  public void onKey(KeyEvent event) {
    // By default, nothing is done on key press
  }

  /**
   * Called when this element is clicked.
   *
   * @param event the event that occurred
   */
  public void onClick(MouseButtonEvent event) {
    // Call onElementClicked for all GUI elements that contain the cursor
    guiElements.stream().filter(element -> element.contains(event.mousePos))
        .forEach(element -> onElementClicked(event, element));
  }

  /**
   * Called when an element in{@link #guiElements} is clicked.
   *
   * @param event   the event that occurred
   * @param element the element that was clicked
   */
  protected void onElementClicked(MouseButtonEvent event, GuiElement element) {
    // By default, do nothing
  }
}
