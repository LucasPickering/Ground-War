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
import java.net.URL;

import groundwar.board.Board;
import groundwar.render.Renderer;
import groundwar.render.event.KeyEvent;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.screen.MainMenuScreen;
import groundwar.render.screen.MainScreen;
import groundwar.util.Colors;
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
            initGame(); // Initialize
            gameLoop(); // Run the game
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tearDown(); // Shutdown
        }
    }

    private void initGame() {
        // Setup error callback to print to System.err
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
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
        GL11.glClearColor((Colors.CLEAR >> 16 & 0xff) / 255.0f, (Colors.CLEAR >> 8 & 0xff) / 255.0f,
                          (Colors.CLEAR & 0xff) / 255.0f, 1.0f);
        GL11.glOrtho(0, Constants.RES_WIDTH, Constants.RES_HEIGHT, 0, -1, 1);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Initialize input handlers
        GLFW.glfwSetKeyCallback(window, keyHandler);
        GLFW.glfwSetMouseButtonCallback(window, mouseButtonHandler);
        GLFW.glfwSetCursorPosCallback(window, cursorPosHandler);
        GLFW.glfwSetWindowSizeCallback(window, windowResizeHandler);

        renderer = new Renderer();
        loadNewBoard();
        currentScreen = new MainMenuScreen(); // Initialize the current screen
    }

    private void gameLoop() {
        while (GLFW.glfwWindowShouldClose(window)) {
            GLFW.glfwPollEvents(); // Poll for events (key, mouse, etc.)
            GL11.glClear(
                GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear the framebuffer
            currentScreen.draw(mousePos.copy());
            GLFW.glfwSwapBuffers(window); // Swap the color buffers

            // Change to the next screen (usually nextScreen() returns the same screen)
            currentScreen = currentScreen.nextScreen();

            // If the current screen is null, exit the game
            if (currentScreen == null) {
                exitGame();
            }
        }
    }

    private void tearDown() {
        renderer.deleteTexturesAndFonts(); // Free up texture memory
        GLFW.glfwDestroyWindow(window); // Destroy the window
        // Release callbacks
//        keyHandler.release();
//        mouseButtonHandler.release();
//        cursorPosHandler.release();
//        windowResizeHandler.release();
//        errorCallback.release();
        GLFW.glfwTerminate(); // Terminate GLFW
    }

    /**
     * Loads a new board from the default file.
     *
     * @return the new {@link Board}
     */
    public Board loadNewBoard() {
        try {
            board = new Board();
        } catch (IOException e) {
            System.err.printf("Error loading board");
            e.printStackTrace();
        }
        return board;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * Exits the game gracefully.
     */
    private void exitGame() {
        GLFW.glfwSetWindowShouldClose(window, true);
    }

    public static void main(String[] args) {
        groundWar.run();
    }

    public static URL getResource(String path, String fileName) {
        return GroundWar.class.getResource(String.format(path, fileName));
    }

    // Event handlers
    {
        keyHandler = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW.GLFW_RELEASE) {
                    currentScreen.onKey(new KeyEvent(window, key, scancode, mods));
                }
            }
        };

        mouseButtonHandler = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW.GLFW_RELEASE && currentScreen.contains(mousePos)) {
                    currentScreen.onClick(new MouseButtonEvent(window, button, mods, mousePos));
                }
            }
        };

        cursorPosHandler = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xPos, double yPos) {
                // Scale the cursor coordinates to fit the coords that everything is drawn at.
                mousePos = new Point((int) (xPos * Constants.RES_WIDTH / windowWidth),
                                     (int) (yPos * Constants.RES_HEIGHT / windowHeight));
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