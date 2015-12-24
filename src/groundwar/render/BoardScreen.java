package groundwar.render;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

import groundwar.Board;
import groundwar.util.Colors;
import groundwar.util.Point;
import groundwar.util.Constants;
import groundwar.render.event.KeyEvent;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.tileoverlay.SpawningUnitTileOverlay;
import groundwar.render.tileoverlay.TileOverlay;
import groundwar.tile.Tile;
import groundwar.unit.Unit;
import groundwar.unit.UnitType;

public class BoardScreen extends MainScreen {

  private final Board board;

  public BoardScreen(long window, Board board) {
    super(window);
    this.board = board;

    renderer.loadTexture(Constants.TILE_BG_NAME);
    renderer.loadTexture(Constants.TILE_OUTLINE_NAME);
  }

  @Override
  public void draw(Point mousePos) {
    super.draw(mousePos);
    final int clearColor = board.getCurrentPlayer().primaryColor & 0xcccccc;
    GL11.glClearColor((clearColor >> 16 & 0xff) / 255.0f, (clearColor >> 8 & 0xff) / 255.0f,
                      (clearColor & 0xff) / 255.0f, 1.0f);

    // Draw each tile
    for (Tile tile : board.getTiles().values()) {
      final Tile selectedTile = board.getSelectedTile();
      final List<TileOverlay> overlays = new LinkedList<>();

      // If the tile is selected, add the selected overlay.
      // Otherwise, if another tile is selected, do some more logic.
      if (tile == selectedTile) {
        overlays.add(TileOverlay.selected);
      } else if (selectedTile != null) {
        // If this tile can be moved to by the selected unit, draw the moveable overlay.
        // Otherwise, if it can be attack by the selected unit, draw the attackable overlay.
        if (board.canSelectedMoveTo(tile)) {
          overlays.add(TileOverlay.moveable);
        } else if (board.canSelectedAttack(tile)) {
          overlays.add(TileOverlay.attackable);
        }
      }

      // If the mouse is over this tile
      if (tile.contains(mousePos)) {
        final Unit spawningUnit = board.getSpawningUnit();
        // If a unit is being spawned, draw the unit-spawning overlay.
        // Otherwise, draw the normal mouse-over overlay.
        if (spawningUnit != null) {
          overlays.add(new SpawningUnitTileOverlay(spawningUnit, tile.isSpawnable(spawningUnit)));
        } else {
          overlays.add(TileOverlay.mouseOver);
        }
      }
      drawTile(tile, overlays);
    }

    GL11.glColor4f(1f, 1f, 1f, 1f);
    renderer.drawText(100f, "Hello world!", 100, 100);
  }

  /**
   * Draws the given tile.
   *
   * @param tile     the tile to draw
   * @param overlays the overlays to draw on the tile
   */
  private void drawTile(Tile tile, List<TileOverlay> overlays) {
    final int x = tile.getScreenPos().getX();
    final int y = tile.getScreenPos().getY();
    final int width = Constants.TILE_WIDTH;
    final int height = Constants.TILE_HEIGHT;

    // Draw the regular background
    renderer.drawTexture(Constants.TILE_BG_NAME, x, y, width, height, tile.getBackgroundColor());

    // Draw the regular foreground
    renderer.drawTexture(Constants.TILE_OUTLINE_NAME, x, y, width, height, tile.getOutlineColor());

    // Draw the unit on top
    drawUnit(tile.getUnit(), x, y); // Don't worry, the call is null-safe

    // Draw tile overlays
    overlays.forEach(overlay -> overlay.draw(x, y, width, height));
  }

  /**
   * Draws the given unit at the given location. If {@code unit == null}, nothing happens.
   *
   * @param unit the unit to draw (null permitted)
   * @param x    the x location to draw at
   * @param y    the y location to draw at
   */
  private void drawUnit(Unit unit, int x, int y) {
    if (unit != null) {
      final int width = Constants.TILE_WIDTH;
      final int height = Constants.TILE_HEIGHT;

      // Draw the unit itself
      renderer.drawTexture(unit.getName(), x, y, width, height, unit.getOwner().primaryColor);

      // Draw the health bar
      final int splitPoint = Constants.HEALTH_BAR_WIDTH * unit.getHealth() / unit.getType().maxHealth;
      renderer.drawRect(x + Constants.HEALTH_BAR_X, y + Constants.HEALTH_BAR_Y,
                        splitPoint, Constants.HEALTH_BAR_HEIGHT,
                        Colors.HEALTH_BAR_POS); // Green part
      renderer.drawRect(x + Constants.HEALTH_BAR_X + splitPoint, y + Constants.HEALTH_BAR_Y,
                        Constants.HEALTH_BAR_WIDTH - splitPoint, Constants.HEALTH_BAR_HEIGHT,
                        Colors.HEALTH_BAR_NEG); // Red part
    }
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
        board.cancelSpawning();
        board.unselectTile();
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
