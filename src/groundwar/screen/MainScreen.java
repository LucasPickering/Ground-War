package groundwar.screen;

import java.util.LinkedList;
import java.util.List;

import groundwar.screen.gui.GuiElement;

/**
 * A {@code MainScreen} is a type of {@link ScreenElement} that is meant to be a top-level element. A
 * {@code MainScreen} has no parent {@link ScreenElement} and there can only ever be one active {@code
 * MainScreen} at a time. Examples of a {@code MainScreen} include the main menu screen and the
 * in-game screen.
 */
public abstract class MainScreen implements ScreenElement {

  protected List<GuiElement> guiElements = new LinkedList<>();

  @Override
  public void draw(long window, int mouseX, int mouseY) {
    for (GuiElement guiElement : guiElements) {
      guiElement.draw(window, mouseX, mouseY);
    }
  }

  @Override
  public boolean contains(int x, int y) {
    return true;
  }
}
