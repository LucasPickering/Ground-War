package groundwar;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import groundwar.screen.IngameScreen;
import groundwar.screen.MainScreen;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.system.MemoryUtil.NULL;

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
      glfwDestroyWindow(window);
      inputHandler.release();
    } finally {
      // Terminate GLFW and release the GLFWErrorCallback
      glfwTerminate();
      errorCallback.release();
    }
  }

  private void initGame() {
    // Setup an error callback. The default implementation will print the error message in System.err
    glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (glfwInit() != GLFW_TRUE) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    // Configure the window
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

    // Set default size to half the monitor
    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor()); // Get res of the primary monitor
    width = vidmode.width() / 2;
    height = vidmode.height() / 2;

    // Create the window
    window = glfwCreateWindow(width, height, "Ground War", NULL, NULL);
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }
    glfwSetWindowPos(window,
                     (vidmode.width() - width) / 2,
                     (vidmode.height() - height) / 2); // Center the window

    glfwSetKeyCallback(window, inputHandler.getKeyHandler()); // Init key callback

    glfwMakeContextCurrent(window);
    glfwSwapInterval(1); // Enable v-sync
    glfwShowWindow(window); // Make the window visible

    currentScreen = new IngameScreen(); // Initialize the current screen to be drawn
  }

  private void gameLoop() {
    GL.createCapabilities(); // LWJGL needs this
    glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // Set clear color

    // Main game loop
    while (glfwWindowShouldClose(window) == GLFW_FALSE) {
      glfwPollEvents(); // Poll for events (key, mouse, etc.)
      glClear(GL_COLOR_BUFFER_BIT); // Clear the framebuffer
      glLoadIdentity(); // Resets any previous projection matrices
      glOrtho(0, width, 0, height, 1, -1);
      currentScreen.draw(window);
      glfwSwapBuffers(window); // Swap the color buffers
    }
  }

  public static void main(String[] args) {
    new GroundWar().run();
  }
}