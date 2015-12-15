package groundwar.screen;

import groundwar.Board;
import groundwar.Constants;
import groundwar.Point;
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
    board.getTiles().values().forEach(tile -> drawTile(tile, mouseX, mouseY)); // Draw each tile
  }

  private void drawTile(Tile tile, int mouseX, int mouseY) {
    int color = tile.contains(new Point(mouseX, mouseY)) ? 0xff0000 : 0x00ff00;
    // Draw the background
    tileBgTex.draw(tile.getScreenPos().getX(), tile.getScreenPos().getY(),
                   Constants.TILE_WIDTH, Constants.TILE_HEIGHT, color);

    // Draw the outline
    tileOutlineTex.draw(tile.getScreenPos().getX(), tile.getScreenPos().getY(),
                        Constants.TILE_WIDTH, Constants.TILE_HEIGHT, tile.getOutlineColor());
  }
}
