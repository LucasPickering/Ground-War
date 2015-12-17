package groundwar.screen;

import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;
import java.util.List;

import groundwar.Board;
import groundwar.Constants;
import groundwar.Point;
import groundwar.screen.event.KeyEvent;
import groundwar.screen.event.MouseButtonEvent;
import groundwar.screen.tileeffect.SpawningUnitTileEffect;
import groundwar.screen.tileeffect.TileEffect;
import groundwar.tile.Tile;
import groundwar.unit.Unit;
import groundwar.unit.UnitType;

public class BoardScreen extends MainScreen {


  private final Board board;

  public BoardScreen(long window, Board board) {
    super(window);
    this.board = board;

    TextureHandler.loadTexture(Constants.TILE_BG_NAME);
    TextureHandler.loadTexture(Constants.TILE_OUTLINE_NAME);
  }

  @Override
  public void draw(Point mousePos) {
    super.draw(mousePos);

    // Draw each tile
    for (Tile tile : board.getTiles().values()) {
      List<TileEffect> effects = new LinkedList<>();
      if (tile == board.getSelectedTile()) {
        effects.add(TileEffect.selected);
      }
      if (tile.contains(mousePos)) {
        Unit spawningUnit = board.getSpawningUnit();
        if (spawningUnit != null) {
          effects.add(new SpawningUnitTileEffect(spawningUnit, tile.isSpawnable(spawningUnit)));
        }
        effects.add(TileEffect.mouseOver);
      }
      drawTile(tile, effects);
    }
  }

  private void drawTile(Tile tile, List<TileEffect> effects) {
    final int x = tile.getScreenPos().getX();
    final int y = tile.getScreenPos().getY();
    final int width = Constants.TILE_WIDTH;
    final int height = Constants.TILE_HEIGHT;

    TextureHandler.startDrawingTextures(); // Set up the environment for drawing texture

    // Draw the regular background
    TextureHandler.draw(Constants.TILE_BG_NAME, x, y, width, height, tile.getBackgroundColor());

    // Draw background layer effects
    drawEffects(effects, TileEffect.Layer.BG, x, y, width, height);

    // Draw the regular foreground
    TextureHandler.draw(Constants.TILE_OUTLINE_NAME, x, y, width, height, tile.getOutlineColor());

    // Draw foreground layer effects
    drawEffects(effects, TileEffect.Layer.FG, x, y, width, height);

    // Draw the unit on top
    Unit unit = tile.getUnit();
    if (unit != null) {
      TextureHandler.draw(unit.getName(), x, y, width, height, unit.getOwner().primaryColor);
    }

    // Draw unit layer effects
    drawEffects(effects, TileEffect.Layer.UNIT, x, y, width, height);

    TextureHandler.stopDrawingTextures(); // Tear down all the texture-drawing setup
  }

  private void drawEffects(List<TileEffect> effects, TileEffect.Layer layer,
                           int x, int y, int width, int height) {
    effects.stream().filter(effect -> effect.getLayer() == layer)
        .forEach(effect -> effect.draw(x, y, width, height));
  }

  @Override
  public void onKey(KeyEvent event) {
    switch (event.key) {
      case GLFW.GLFW_KEY_M:
        board.prepareToSpawn(UnitType.MARINES);
        break;
      case GLFW.GLFW_KEY_A:
        board.prepareToSpawn(UnitType.ANTITANK);
        break;
      case GLFW.GLFW_KEY_T:
        board.prepareToSpawn(UnitType.TANK);
        break;
      case GLFW.GLFW_KEY_ESCAPE:
        board.prepareToSpawn(null);
        break;
      case GLFW.GLFW_KEY_SPACE:
        board.nextTurn();
        break;
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
