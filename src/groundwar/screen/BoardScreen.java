package groundwar.screen;

import groundwar.Board;
import groundwar.screen.gui.Button;
import groundwar.tile.Tile;

public class BoardScreen extends MainScreen {

  private final Board board;
  Button testButton;

  public BoardScreen(Board board) {
    this.board = board;
    guiElements.add(testButton = new Button("Hi", 100, 100, 200, 20));
    testButton.setMouseButtonHandler(this::onClick);
  }

  @Override
  public void draw(long window, int mouseX, int mouseY) {
    super.draw(window, mouseX, mouseY);
    board.getTiles().values().forEach(this::drawTile); // Draw each tile
  }

  private void drawTile(Tile tile) {

  }
}
