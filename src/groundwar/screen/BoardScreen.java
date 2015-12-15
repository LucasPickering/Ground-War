package groundwar.screen;

import groundwar.Board;
import groundwar.Constants;
import groundwar.HexPoint;
import groundwar.screen.gui.Button;
import groundwar.tile.Tile;

public class BoardScreen extends MainScreen {

  private final Board board;
  Button testButton;

  private final Texture tileBgTex;
  private final Texture tileOutlineTex;

  public BoardScreen(long window, Board board) {
    super(window);
    this.board = board;
    guiElements.add(testButton = new Button(window, 100, 100, 512, 512, "Hello World"));
    testButton.setMouseButtonHandler(this::onClick);

    tileBgTex = TextureHandler.loadTexture("/textures/tile_background.png");
    tileOutlineTex = TextureHandler.loadTexture("/textures/tile_outline.png");
  }

  @Override
  public void draw(int mouseX, int mouseY) {
    super.draw(mouseX, mouseY);
    board.getTiles().values().forEach(this::drawTile); // Draw each tile
  }

  private void drawTile(Tile tile) {
    final HexPoint pos = tile.getPos();
    final int x = Constants.BOARD_CENTER_X + (int) (Constants.TILE_WIDTH * pos.getX() * 0.75f);
    final int y = Constants.BOARD_CENTER_Y + -Constants.TILE_HEIGHT * (pos.getX() / 2 + pos.getY());
    tileBgTex.draw(x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, tile.getBackgroundColor());
    tileOutlineTex.draw(x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, tile.getOutlineColor());
  }
}
