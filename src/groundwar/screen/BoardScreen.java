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
  public void draw(Point mousePos) {
    super.draw(mousePos);

    // Draw each tile
    for (Tile tile : board.getTiles().values()) {
      TileEffect effect;
      if (tile.contains(mousePos)) {
        effect = TileEffect.MOUSE_OVER;
      } else {
        effect = TileEffect.DEFAULT;
      }
      drawTile(tile, effect);
    }
  }

  private void drawTile(Tile tile, TileEffect effect) {
    // Draw the background
    tileBgTex.draw(tile.getScreenPos().getX(), tile.getScreenPos().getY(),
                   Constants.TILE_WIDTH, Constants.TILE_HEIGHT,
                   effect.getBackgroundColor(tile.getBackgroundColor()));

    // Draw the outline
    tileOutlineTex.draw(tile.getScreenPos().getX(), tile.getScreenPos().getY(),
                        Constants.TILE_WIDTH, Constants.TILE_HEIGHT,
                        effect.getOutlineColor(tile.getOutlineColor()));
  }
}
