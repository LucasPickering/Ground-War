package groundwar.screen;

import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;
import java.util.List;

import groundwar.screen.event.KeyEvent;
import groundwar.screen.gui.GuiElement;

/**
 * A {@code MainScreen} is a type of {@link ScreenElement} that is meant to be a top-level element. A
 * {@code MainScreen} has no parent {@link ScreenElement} and there can only ever be one active {@code
 * MainScreen} at a time. Examples of a {@code MainScreen} include the main menu screen and the
 * in-game screen.
 */
public abstract class MainScreen extends ScreenElement {

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

  @Override
  public void onKey(KeyEvent event) {
    if (event.key == GLFW.GLFW_KEY_ESCAPE) {
      GLFW.glfwSetWindowShouldClose(event.window, GLFW.GLFW_TRUE);
    }
  }
}
