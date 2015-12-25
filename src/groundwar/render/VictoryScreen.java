package groundwar.render;

import org.lwjgl.opengl.GL11;

import groundwar.board.Board;
import groundwar.board.Player;
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
    final Player winner = board.getWinner();
    renderer.drawText(Constants.FONT_SIZE_TITLE,
                      String.format("%s wins in\n%d turns!", winner, board.getTurnCounter()),
                      Constants.NATIVE_WINDOW_WIDTH / 2, Constants.NATIVE_WINDOW_HEIGHT / 2,
                      board.getWinner().getPrimaryColor(), TextAlignment.CENTER);
  }

  @Override
  public MainScreen nextScreen() {
    return this;
  }
}
