package groundwar;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import groundwar.screen.IngameScreen;
import groundwar.screen.MainScreen;

public class GroundWar {

  private GLFWErrorCallback errorCallback;
  private InputHandler inputHandler = new InputHandler();
  private long window;
  private MainScreen currentScreen;
  private int width;
  private int height;

  public void run() {
    try {
      initGame();
      gameLoop();

      // Release window and window callbacks
      GLFW.glfwDestroyWindow(window);
      inputHandler.release();
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

    GLFW.glfwSetKeyCallback(window, inputHandler.getKeyHandler()); // Init key callback

    GLFW.glfwMakeContextCurrent(window);
    GLFW.glfwSwapInterval(1); // Enable v-sync
    GLFW.glfwShowWindow(window); // Make the window visible

    currentScreen = new IngameScreen(); // Initialize the current screen to be drawn
  }

  private void gameLoop() {
    GL.createCapabilities(); // LWJGL needs this
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // Set clear color
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity(); // Resets any previous projection matrices
    GL11.glOrtho(-width / 2, width / 2, -height / 2, height / 2, -1, 1); // Origin is centered
    GL11.glMatrixMode(GL11.GL_MODELVIEW);

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
}