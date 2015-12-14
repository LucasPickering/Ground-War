package groundwar;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import groundwar.screen.IngameScreen;
import groundwar.screen.MainScreen;
import groundwar.screen.event.KeyEvent;
import groundwar.screen.event.MouseButtonEvent;

public class GroundWar {

  private GLFWErrorCallback errorCallback;
  private KeyHandler keyHandler;
  private MouseButtonHandler mouseButtonHandler;
  private CursorPosHandler cursorPosHandler;
  private WindowResizeHandler windowResizeHandler;
  private long window;
  private MainScreen currentScreen;
  private int windowWidth;
  private int windowHeight;
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
      cursorPosHandler.release();
      windowResizeHandler.release();
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
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

    // Set default size to half the monitor
    GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()); // Get resolution
    windowWidth = vidmode.width() / 2;
    windowHeight = vidmode.height() / 2;

    // Create the window
    window =
        GLFW.glfwCreateWindow(windowWidth, windowHeight, "Ground War", MemoryUtil.NULL,
                              MemoryUtil.NULL);
    if (window == MemoryUtil.NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }
    GLFW.glfwSetWindowPos(window,
                          (vidmode.width() - windowWidth) / 2,
                          (vidmode.height() - windowHeight) / 2); // Center the window

    GLFW.glfwMakeContextCurrent(window);
    GLFW.glfwSwapInterval(1); // Enable v-sync
    GLFW.glfwShowWindow(window); // Make the window visible
    GL.createCapabilities(); // LWJGL needs this
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // Set clear color
    GL11.glOrtho(0, Constants.NATIVE_WINDOW_WIDTH, 0, Constants.NATIVE_WINDOW_HEIGHT, -1, 1);

    // Initialize input handlers
    GLFW.glfwSetKeyCallback(window, keyHandler = new KeyHandler());
    GLFW.glfwSetMouseButtonCallback(window, mouseButtonHandler = new MouseButtonHandler());
    GLFW.glfwSetCursorPosCallback(window, cursorPosHandler = new CursorPosHandler());
    GLFW.glfwSetWindowSizeCallback(window, windowResizeHandler = new WindowResizeHandler());

    currentScreen = new IngameScreen(); // Initialize the current screen to be drawn
  }

  private void gameLoop() {
    while (GLFW.glfwWindowShouldClose(window) == GLFW.GLFW_FALSE) {
      GLFW.glfwPollEvents(); // Poll for events (key, mouse, etc.)
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear the framebuffer
      currentScreen.draw(window, mouseX, mouseY);
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
      if (action == GLFW.GLFW_RELEASE) {
        currentScreen.handleKey(new KeyEvent(window, key, scancode, mods));
      }
    }
  }

  private class MouseButtonHandler extends GLFWMouseButtonCallback {

    @Override
    public void invoke(long window, int button, int action, int mods) {
      if (action == GLFW.GLFW_RELEASE && currentScreen.contains(mouseX, mouseY)) {
        currentScreen.handleMouseButton(new MouseButtonEvent(window, button, mods, mouseX, mouseY));
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

  private class WindowResizeHandler extends GLFWWindowSizeCallback {

    @Override
    public void invoke(long window, int width, int height) {
      windowWidth = width;
      windowHeight = height;
      GL11.glViewport(0, 0, width, height);
    }
  }
}