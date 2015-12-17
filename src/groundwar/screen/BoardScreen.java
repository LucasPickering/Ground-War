package groundwar.screen;

import org.lwjgl.glfw.GLFW;

import groundwar.Board;
import groundwar.Constants;
import groundwar.Point;
import groundwar.screen.event.KeyEvent;
import groundwar.screen.event.MouseButtonEvent;
import groundwar.tile.Tile;
import groundwar.unit.Unit;
import groundwar.unit.UnitType;

public class BoardScreen extends MainScreen {

  private static final String TILE_BG_NAME = "tile_background";
  private static final String TILE_OUTLINE_NAME = "tile_outline";

  private final Board board;

  public BoardScreen(long window, Board board) {
    super(window);
    this.board = board;

    TextureHandler.loadTexture(TILE_BG_NAME);
    TextureHandler.loadTexture(TILE_OUTLINE_NAME);
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
    TextureHandler.draw(TILE_BG_NAME, x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT,
                        tile.getBackgroundColor());
    if (effect != null) {
      TextureHandler.draw(TILE_BG_NAME, x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT,
                          effect.backgroundColor);
    }

    // Draw the outline
    TextureHandler.draw(TILE_OUTLINE_NAME, x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT,
                        tile.getOutlineColor());
    if (effect != null) {
      TextureHandler.draw(TILE_OUTLINE_NAME, x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT,
                          effect.outlineColor);
    }

    // Draw the unit on top
    Unit unit;
    if ((unit = tile.getUnit()) != null) {
      TextureHandler.draw(unit.getName(), x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT,
                          unit.getOwner().primaryColor);
    }

    TextureHandler.stopDrawingTextures(); // Tear down all the texture-drawing setup
  }

  @Override
  public void onKey(KeyEvent event) {
    super.onKey(event);
    switch (event.key) {
      case GLFW.GLFW_KEY_M:
        board.prepareToSpawn(UnitType.MARINES);
      case GLFW.GLFW_KEY_A:
        board.prepareToSpawn(UnitType.ANTITANK);
      case GLFW.GLFW_KEY_T:
        board.prepareToSpawn(UnitType.TANK);
    }
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
