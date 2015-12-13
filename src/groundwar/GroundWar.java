package groundwar;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import groundwar.screen.IngameScreen;
import groundwar.screen.MainScreen;

public class GroundWar {

  private GLFWErrorCallback errorCallback;
  private KeyHandler keyHandler;
  private CursorPosHandler cursorPosHandler;
  private MouseButtonHandler mouseButtonHandler;
  private long window;
  private MainScreen currentScreen;
  private int width;
  private int height;
  private int mouseX;
  private int mouseY;

  public void run() {
    try {
      initGame();
      gameLoop();

      // Release window and window callbacks
      GLFW.glfwDestroyWindow(window);
      keyHandler.release();
      mouseButtonHandler.release();
    } finally {
      // Terminate GLFW and release the GLFWErrorCallback
      GLFW.glfwTerminate();
      errorCallback.release();
    }
  }

  private void initGame() {
    // Setup an error callback. The default implementation will print the error message in System.err
    GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (GLFW.glfwInit() != GLFW.GLFW_TRUE) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    // Configure the window
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable

    // Set default size to half the monitor
    GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()); // Get resolution
    width = vidmode.width() / 2;
    height = vidmode.height() / 2;

    // Create the window
    window = GLFW.glfwCreateWindow(width, height, "Ground War", MemoryUtil.NULL, MemoryUtil.NULL);
    if (window == MemoryUtil.NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }
    GLFW.glfwSetWindowPos(window,
                          (vidmode.width() - width) / 2,
                          (vidmode.height() - height) / 2); // Center the window

    // Initialize input handlers
    GLFW.glfwSetKeyCallback(window, keyHandler = new KeyHandler());
    GLFW.glfwSetCursorPosCallback(window, cursorPosHandler = new CursorPosHandler());
    GLFW.glfwSetMouseButtonCallback(window, mouseButtonHandler = new MouseButtonHandler());

    GLFW.glfwMakeContextCurrent(window);
    GLFW.glfwSwapInterval(1); // Enable v-sync
    GLFW.glfwShowWindow(window); // Make the window visible

    currentScreen = new IngameScreen(); // Initialize the current screen to be drawn
  }

  private void gameLoop() {
    GL.createCapabilities(); // LWJGL needs this
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // Set clear color
    GL11.glOrtho(0, width, height, 0, -1, 1); // Origin is top-left

    // Main game loop
    while (GLFW.glfwWindowShouldClose(window) == GLFW.GLFW_FALSE) {
      GLFW.glfwPollEvents(); // Poll for events (key, mouse, etc.)
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear the framebuffer
      currentScreen.draw(window, 0, 0);
      GLFW.glfwSwapBuffers(window); // Swap the color buffers
    }
  }

  public static void main(String[] args) {
    new GroundWar().run();
  }

  // Input handlers
  private class KeyHandler extends GLFWKeyCallback {

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
      if (action == GLFW.GLFW_RELEASE && key == GLFW.GLFW_KEY_ESCAPE) {
        GLFW.glfwSetWindowShouldClose(window, GLFW.GLFW_TRUE);
      }
    }
  }

  private class CursorPosHandler extends GLFWCursorPosCallback {

    @Override
    public void invoke(long window, double xPos, double yPos) {
      mouseX = (int) xPos;
      mouseY = (int) yPos;
    }
  }

  private class MouseButtonHandler extends GLFWMouseButtonCallback {

    @Override
    public void invoke(long window, int button, int action, int mods) {
      if (action == GLFW.GLFW_RELEASE) {
        if(currentScreen.insideElement(mouseX, mouseY)) {
          currentScreen.onClicked(mouseX, mouseY, button, mods);
        }
      }
    }
  }
}