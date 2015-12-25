package groundwar.render;

import org.lwjgl.opengl.GL11;

import groundwar.board.Player;
import groundwar.util.Constants;
import groundwar.util.Point;

public class VictoryScreen extends MainScreen {

  private final Player winner;

  public VictoryScreen(long window, Player winner) {
    super(window);
    this.winner = winner;
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
  }

  @Override
  public void draw(Point mousePos) {
    super.draw(mousePos);
    renderer.drawText(Constants.FONT_SIZE_TITLE, "Victory!",
                      Constants.NATIVE_WINDOW_WIDTH / 2, Constants.NATIVE_WINDOW_HEIGHT / 2,
                      winner.getPrimaryColor(), TextAlignment.CENTER);
  }

  @Override
  public MainScreen nextScreen() {
    return null;
  }
}
