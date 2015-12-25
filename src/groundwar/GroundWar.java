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

import java.io.IOException;

import groundwar.board.Board;
import groundwar.render.BoardScreen;
import groundwar.render.MainScreen;
import groundwar.render.Renderer;
import groundwar.render.event.KeyEvent;
import groundwar.render.event.MouseButtonEvent;
import groundwar.util.Constants;
import groundwar.util.Point;

public class GroundWar {

  public static final GroundWar groundWar = new GroundWar();

  private GLFWErrorCallback errorCallback;

  // These event handlers are initialized at the bottom
  private GLFWKeyCallback keyHandler;
  private GLFWMouseButtonCallback mouseButtonHandler;
  private GLFWCursorPosCallback cursorPosHandler;
  private GLFWWindowSizeCallback windowResizeHandler;

  private long window;
  private Renderer renderer;
  private MainScreen currentScreen;
  private int windowWidth;
  private int windowHeight;
  private Point mousePos = new Point();

  private Board board;

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
    window = GLFW.glfwCreateWindow(windowWidth, windowHeight, "Ground War", MemoryUtil.NULL,
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
    GL11.glClearColor(0.0f, 1.0f, 1.0f, 1.0f); // Set clear color
    GL11.glOrtho(0, Constants.NATIVE_WINDOW_WIDTH, Constants.NATIVE_WINDOW_HEIGHT, 0, -1, 1);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

    // Initialize input handlers
    GLFW.glfwSetKeyCallback(window, keyHandler);
    GLFW.glfwSetMouseButtonCallback(window, mouseButtonHandler);
    GLFW.glfwSetCursorPosCallback(window, cursorPosHandler);
    GLFW.glfwSetWindowSizeCallback(window, windowResizeHandler);

    renderer = new Renderer();

    try {
      board = new Board();
    } catch (IOException e) {
      System.err.printf("Error loading board \"%s\"\n", Constants.BOARD_FILE);
      e.printStackTrace();
    }

    currentScreen = new BoardScreen(window, board); // Initialize the current screen
  }

  private void gameLoop() {
    while (GLFW.glfwWindowShouldClose(window) == GLFW.GLFW_FALSE) {
      GLFW.glfwPollEvents(); // Poll for events (key, mouse, etc.)
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear the framebuffer
      currentScreen.draw(mousePos.copy());
      GLFW.glfwSwapBuffers(window); // Swap the color buffers

      // If the screen should change, do that
      MainScreen nextScreen;
      if ((nextScreen = currentScreen.nextScreen()) != null) {
        currentScreen = nextScreen;
      }
    }
    tearDown();
  }

  private void tearDown() {
    renderer.deleteTexturesAndFonts(); // Free up texture memory
  }

  public Renderer getRenderer() {
    return renderer;
  }

  public static void main(String[] args) {
    groundWar.run();
  }

  // Event handlers
  {
    keyHandler = new GLFWKeyCallback() {
      @Override
      public void invoke(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_RELEASE) {
          currentScreen.handleKey(new KeyEvent(window, key, scancode, mods));
        }
      }
    };

    mouseButtonHandler = new GLFWMouseButtonCallback() {
      @Override
      public void invoke(long window, int button, int action, int mods) {
        if (action == GLFW.GLFW_RELEASE && currentScreen.contains(mousePos)) {
          currentScreen.handleMouseButton(new MouseButtonEvent(window, button, mods, mousePos));
        }
      }
    };

    cursorPosHandler = new GLFWCursorPosCallback() {
      @Override
      public void invoke(long window, double xPos, double yPos) {
        // Scale the cursor coordinates to fit the coords that everything is drawn at.
        mousePos = new Point((int) (xPos * Constants.NATIVE_WINDOW_WIDTH / windowWidth),
                             (int) (yPos * Constants.NATIVE_WINDOW_HEIGHT / windowHeight));
      }
    };

    windowResizeHandler = new GLFWWindowSizeCallback() {
      @Override
      public void invoke(long window, int width, int height) {
        windowWidth = width;
        windowHeight = height;
        GL11.glViewport(0, 0, windowWidth, windowHeight);
      }
    };
  }
}