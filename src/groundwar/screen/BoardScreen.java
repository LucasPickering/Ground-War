package groundwar.screen;

import groundwar.Board;
import groundwar.Constants;
import groundwar.Point;
import groundwar.screen.event.MouseButtonEvent;
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
      TileEffect effect = null;
      if (tile == board.getSelectedTile()) {
        effect = TileEffect.SELECTED;
      } else if (tile.contains(mousePos)) {
        effect = TileEffect.MOUSE_OVER;
      }
      drawTile(tile, effect);
    }
  }

  private void drawTile(Tile tile, TileEffect effect) {
    final int x = tile.getScreenPos().getX();
    final int y = tile.getScreenPos().getY();

    TextureHandler.startDrawingTextures(); // Set up the environment for drawing texture

    // Draw the background
    tileBgTex.draw(x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, tile.getBackgroundColor());
    if (effect != null) {
      tileBgTex.draw(x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, effect.backgroundColor);
    }

    // Draw the outline
    tileOutlineTex.draw(x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, tile.getOutlineColor());
    if (effect != null) {
      tileOutlineTex.draw(x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, effect.outlineColor);
    }

    TextureHandler.stopDrawingTextures(); // Tear down all the texture-drawing setup
  }

  @Override
  public void onClick(MouseButtonEvent event) {
    super.onClick(event);
    for (Tile tile : board.getTiles().values()) {
      if (tile.contains(event.mousePos)) {
        board.onTileClicked(tile);
        break;
      }
    }
  }
}
