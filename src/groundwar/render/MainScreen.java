package groundwar.render;

import java.util.LinkedList;
import java.util.List;

import groundwar.util.Point;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.gui.GuiElement;

/**
 * A {@code MainScreen} is a type of {@link ScreenElement} that is meant to be a top-level element. A
 * {@code MainScreen} has no parent {@link ScreenElement} and there can only ever be one active {@code
 * MainScreen} at a time. Examples of a {@code MainScreen} include the main menu screen and the
 * in-game screen.
 */
public abstract class MainScreen extends ScreenElement {

  protected List<GuiElement> guiElements = new LinkedList<>();

  protected MainScreen(long window) {
    super(window);
  }

  @Override
  public void draw(Point mousePos) {
    // Draw all the GUI elements
    guiElements.stream().forEach(guiElement -> guiElement.draw(mousePos));
  }

  @Override
  public boolean contains(Point p) {
    return true;
  }

  @Override
  public void onClick(MouseButtonEvent event) {
    // Call onClick for all GUI elements that contain the cursor
    guiElements.stream().filter(guiElement -> guiElement.contains(event.mousePos))
        .forEach(guiElement -> guiElement.onClick(event));
  }
}
