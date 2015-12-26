package groundwar.render;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import groundwar.board.Board;
import groundwar.render.event.KeyEvent;
import groundwar.util.Constants;
import groundwar.util.Point;

public class VictoryScreen extends MainScreen {

  private final Board board;

  public VictoryScreen(long window, Board board) {
    super(window);
    this.board = board;
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
  }

  @Override
  public void draw(Point mousePos) {
    super.draw(mousePos);
    GL11.glEnable(GL11.GL_BLEND);
    renderer.drawText(Constants.FONT_SIZE_TITLE,
                      String.format("%s wins\nin %d turns!", board.getWinner(), board.getTurnCount()),
                      Constants.NATIVE_WINDOW_WIDTH / 2, Constants.NATIVE_WINDOW_HEIGHT / 2,
                      board.getWinner().getPrimaryColor(), TextAlignment.CENTER);
    GL11.glDisable(GL11.GL_BLEND);
  }

  @Override
  public MainScreen nextScreen() {
    return this;
  }

  @Override
  public void onKey(KeyEvent event) {
    switch (event.key) {
      case GLFW.GLFW_KEY_ESCAPE:
        GLFW.glfwSetWindowShouldClose(window, GLFW.GLFW_TRUE);
        break;
    }
  }
}
