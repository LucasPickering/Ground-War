package groundwar.screen;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

/**
 * A {@code MainScreen} is a type of {@link ScreenElement} that is meant to be a top-level element. A
 * {@code MainScreen} has no parent {@link ScreenElement} and there can only ever be one active {@code
 * MainScreen} at a time. Examples of a {@code MainScreen} include the main menu screen and the
 * in-game screen.
 */
public class MainScreen implements ScreenElement {

  @Override
  public void draw(long window) {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer
    glfwSwapBuffers(window); // Swap the color buffers
    glfwPollEvents();// Poll for events (key, mouse, etc.)
  }
}
