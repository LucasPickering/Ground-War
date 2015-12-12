package groundwar;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class InputHandler {

  private class KeyHandler extends GLFWKeyCallback {

    @Override
    public void invoke(long window, int key, int scancode, int action, int mode) {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, GLFW_TRUE);
      }
    }
  }

  private KeyHandler keyHandler = new KeyHandler();

  public KeyHandler getKeyHandler() {
    return keyHandler;
  }

  public void release() {
    keyHandler.release();
  }
}
