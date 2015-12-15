package groundwar.screen;

import groundwar.Board;
import groundwar.screen.gui.Button;
import groundwar.tile.Tile;

public class BoardScreen extends MainScreen {

  private final Board board;
  Button testButton;

  public BoardScreen(long window, Board board) {
    super(window);
    this.board = board;
    guiElements.add(testButton = new Button(window, 100, 100, 100, 100, "Hello World"));
    testButton.setMouseButtonHandler(this::onClick);
  }

  @Override
  public void draw(int mouseX, int mouseY) {
    super.draw(mouseX, mouseY);
    board.getTiles().values().forEach(this::drawTile); // Draw each tile
  }

  private void drawTile(Tile tile) {

  }
}
