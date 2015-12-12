package groundwar;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GroundWar {

  private GLFWErrorCallback errorCallback;
  private InputHandler inputHandler = new InputHandler();

  private long window; // Reference to the window

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
    final int width = vidmode.width() / 2;
    final int height = vidmode.height() / 2;

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
  }

  private void gameLoop() {
    GL.createCapabilities(); // LWJGL needs this
    glClearColor(1.0f, 0.0f, 1.0f, 0.0f); // Clear the screen

    // Main game loop
    while (glfwWindowShouldClose(window) == GLFW_FALSE) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer
      glfwSwapBuffers(window); // Swap the color buffers
      glfwPollEvents();// Poll for events (key, mouse, etc.)
    }
  }

  public static void main(String[] args) {
    new GroundWar().run();
  }
}