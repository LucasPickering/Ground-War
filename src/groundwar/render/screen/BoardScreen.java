package groundwar.render.screen;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

import groundwar.board.Board;
import groundwar.board.Flag;
import groundwar.board.PlayerColor;
import groundwar.board.tile.Tile;
import groundwar.board.unit.Unit;
import groundwar.board.unit.UnitType;
import groundwar.render.ColorTexture;
import groundwar.render.HorizAlignment;
import groundwar.render.event.KeyEvent;
import groundwar.render.event.MouseButtonEvent;
import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Point;

public class BoardScreen extends MainScreen {

  private final Board board;

  public BoardScreen(Board board) {
    this.board = board;
  }

  @Override
  public void draw(Point mousePos) {
    super.draw(mousePos);

    GL11.glEnable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_TEXTURE_2D);

    final Tile selectedTile = board.getSelectedTile();

    // Draw each tile
    for (Tile tile : board.getTiles().values()) {
      final List<ColorTexture> overlays = new LinkedList<>();

      // If the tile is selected, add the selected overlay.
      // Otherwise, if another tile is selected, do some more logic.
      if (tile == selectedTile) {
        overlays.add(ColorTexture.selected);
      } else if (selectedTile != null) {
        // If this tile can be moved to by the selected unit, draw the moveable overlay.
        // Otherwise, if it can be attack by the selected unit, draw the attackable overlay.
        if (board.canSelectedMoveTo(tile)) {
          overlays.add(ColorTexture.moveable);
        } else if (board.canSelectedAttack(tile)) {
          overlays.add(ColorTexture.attackable);
        }
      }

      // If the mouse is over this tile
      if (tile.contains(mousePos)) {
        final Unit spawningUnit = board.getSpawningUnit();
        // If a unit is being spawned, draw the unit-spawning overlay.
        // Otherwise, draw the normal mouse-over overlay.
        if (spawningUnit != null) {
          overlays.add(spawningUnit.getSpawningTexture());
          overlays.add(tile.isSpawnable(spawningUnit) ? ColorTexture.validSpawning
                                                      : ColorTexture.invalidSpawning);
        } else {
          overlays.add(ColorTexture.mouseOver);
        }
      }

      GL11.glPushMatrix();
      GL11.glTranslatef(tile.getScreenPos().getX(), tile.getScreenPos().getY(), 0f);
      drawTile(tile, overlays);
      GL11.glPopMatrix();
    }

    // Draw turn counter
    renderer().drawString(Constants.FONT_SIZE_UI, String.format("Turn %d", board.getTurnCount()),
                          Constants.TURN_COUNT_X, Constants.TURN_COUNT_Y, HorizAlignment.RIGHT);

    // Draw the players's information
    renderer().drawString(Constants.FONT_SIZE_UI,
                          String.format("Gold: %d", board.getPlayer(PlayerColor.ORANGE).getGold()),
                          Constants.ORANGE_UI_X, Constants.ORANGE_UI_Y);
    renderer().drawString(Constants.FONT_SIZE_UI,
                          String.format("Gold: %d", board.getPlayer(PlayerColor.BLUE).getGold()),
                          Constants.BLUE_UI_X, Constants.BLUE_UI_Y, HorizAlignment.RIGHT);

    // Draw unit information
    if (selectedTile != null) {
      final Unit selectedUnit = selectedTile.getUnit();
      renderer().drawString(Constants.FONT_SIZE_UI, String.format("%s\nHealth: %d",
                                                                  selectedUnit.getDisplayName(),
                                                                  selectedUnit.getHealth()),
                            Constants.UNIT_INFO_X, Constants.UNIT_INFO_Y);
    }

    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);
  }

  @Override
  public MainScreen nextScreen() {
    if (board.isGameOver()) {
      return new VictoryScreen(board);
    }
    return this;
  }

  /**
   * Draws the given tile.
   *
   * @param tile     the tile to draw
   * @param overlays the overlays to draw on the tile
   */
  private void drawTile(Tile tile, List<ColorTexture> overlays) {
    final int width = Constants.TILE_WIDTH;
    final int height = Constants.TILE_HEIGHT;

    // Draw the regular background
    renderer().drawTexture(Constants.TILE_BG_NAME, 0, 0, width, height, tile.getBackgroundColor());

    // Draw the regular foreground
    renderer().drawTexture(Constants.TILE_OUTLINE_NAME, 0, 0, width, height, tile.getOutlineColor());

    drawUnit(tile.getUnit()); // Draw the unit on top
    drawFlag(tile.getFlag()); // Draw the flag on top of that

    // Draw tile overlays
    overlays.forEach(overlay -> overlay.draw(0, 0, width, height));
  }

  /**
   * Draws the given unit. If {@code unit == null}, nothing happens.
   *
   * @param unit the unit to draw (null permitted)
   */
  private void drawUnit(Unit unit) {
    if (unit != null) {
      final int width = Constants.TILE_WIDTH;
      final int height = Constants.TILE_HEIGHT;

      unit.getTexture().draw(0, 0, width, height); // Draw the unit itself

      // If the unit belongs to the current player, draw the amount of moves remaining
      if (unit.getOwner() == board.getCurrentPlayer()) {
        renderer().drawString(Constants.FONT_SIZE_TILE, Integer.toString(unit.getMovesRemaining()),
                              Constants.UNIT_MOVES_X, Constants.UNIT_MOVES_Y);
      }

      // Draw the health bar
      final int splitPoint =
          Constants.UNIT_HEALTH_WIDTH * unit.getHealth() / unit.getMaxHealth();
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      renderer().drawRect(Constants.UNIT_HEALTH_X, Constants.UNIT_HEALTH_Y,
                          splitPoint, Constants.UNIT_HEALTH_HEIGHT,
                          Colors.HEALTH_BAR_POS); // Green part
      renderer().drawRect(Constants.UNIT_HEALTH_X + splitPoint, Constants.UNIT_HEALTH_Y,
                          Constants.UNIT_HEALTH_WIDTH - splitPoint, Constants.UNIT_HEALTH_HEIGHT,
                          Colors.HEALTH_BAR_NEG); // Red part
      GL11.glEnable(GL11.GL_TEXTURE_2D);

      drawFlag(unit.getFlag()); // Draw the flag, if the unit has one
    }
  }

  /**
   * Draws the given flag at the given location. if {@code flag == null}, nothing happens.
   *
   * @param flag the flag to be drawn
   */
  private void drawFlag(Flag flag) {
    if (flag != null) {
      renderer().drawTexture(Constants.FLAG_NAME, 0, 0, Constants.TILE_WIDTH, Constants.TILE_HEIGHT,
                             flag.getOwner().getPrimaryColor());
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
