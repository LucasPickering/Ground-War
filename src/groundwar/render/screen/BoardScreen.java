package groundwar.render.screen;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import groundwar.board.Board;
import groundwar.board.Flag;
import groundwar.board.Player;
import groundwar.board.PlayerColor;
import groundwar.board.tile.Tile;
import groundwar.board.unit.Unit;
import groundwar.board.unit.UnitType;
import groundwar.render.ColorTexture;
import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
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

    // Draw each tile
    for (Tile tile : board.getTiles().values()) {
      GL11.glPushMatrix();
      GL11.glTranslatef(tile.getScreenPos().getX(), tile.getScreenPos().getY(), 0f);
      drawTile(mousePos, tile);
      GL11.glPopMatrix();
    }

    // Draw turn counter
    renderer().drawString(Constants.FONT_SIZE_UI, "Turn " + board.getTurnCount(),
                          Constants.TURN_COUNT_X, Constants.TURN_COUNT_Y, 0xffffffff,
                          HorizAlignment.RIGHT, VertAlignment.BOTTOM);

    // Draw the players's information
    drawPlayerInfo(board.getPlayer(PlayerColor.ORANGE), Constants.ORANGE_UI_X, Constants.ORANGE_UI_Y,
                   HorizAlignment.LEFT);
    drawPlayerInfo(board.getPlayer(PlayerColor.BLUE), Constants.BLUE_UI_X, Constants.BLUE_UI_Y,
                   HorizAlignment.RIGHT);

    if (board.getSelectedTile() != null) {
      drawUnitInfo(board.getSelectedTile().getUnit());
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
   * @param mousePos the position of the mouse
   * @param tile     the tile to draw
   */
  private void drawTile(Point mousePos, Tile tile) {
    final int width = Constants.TILE_WIDTH;
    final int height = Constants.TILE_HEIGHT;

    // Draw the regular background
    renderer().drawTexture(Constants.TILE_BG_NAME, 0, 0, width, height, tile.getBackgroundColor());

    // Draw the regular foreground
    renderer().drawTexture(Constants.TILE_OUTLINE_NAME, 0, 0, width, height, tile.getOutlineColor());

    drawUnit(tile.getUnit()); // Draw the unit on top
    drawFlag(tile.getFlag()); // Draw the flag on top of that
    drawTileOverlays(mousePos, tile); // Draw the tile overlays on top of everything else
  }

  /**
   * Draw the appropriate overlays for the given tile.
   *
   * @param mousePos the position of the mouse
   * @param tile     the tile to draw
   */
  private void drawTileOverlays(Point mousePos, Tile tile) {
    final int width = Constants.TILE_WIDTH;
    final int height = Constants.TILE_HEIGHT;

    // Draw selection-related overlays
    if (board.isSelected(tile)) { // If this tile is selected
      ColorTexture.selected.draw(0, 0, width, height); // Draw the selected overlay
    } else if (board.getSelectedTile() != null) { // Otherwise, if another tile is selected...
      if (board.canSelectedMoveTo(tile)) { // If this tile can be moved to...
        ColorTexture.moveable.draw(0, 0, width, height); // Draw the moveable overlay
      } else if (board.canSelectedAttack(tile)) { // Else, if this tile can be attacked...
        ColorTexture.attackable.draw(0, 0, width, height); // Draw the attackable overlay
      }
    }

    // Draw mouse-over overlays
    if (tile.contains(mousePos)) { // If the mouse is over this tile...
      final Unit spawningUnit = board.getSpawningUnit();
      if (spawningUnit != null) { // If a unit is being spawned...
        // Draw the unit-spawning overlay
        spawningUnit.getSpawningTexture().draw(0, 0, width, height);
        (tile.isSpawnable(spawningUnit) ? ColorTexture.validSpawning
                                        : ColorTexture.invalidSpawning).draw(0, 0, width, height);
      } else {
        ColorTexture.mouseOver.draw(0, 0, width, height); // Draw the mouse-over overlay
      }
    }
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
      final int splitPoint = Constants.UNIT_HEALTH_WIDTH * unit.getHealth() / unit.getMaxHealth();
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
   * Draws the given flag. if {@code flag == null}, nothing happens.
   *
   * @param flag the flag to be drawn
   */
  private void drawFlag(Flag flag) {
    if (flag != null) {
      renderer().drawTexture(Constants.FLAG_NAME, Constants.FLAG_X, Constants.FLAG_Y,
                             Constants.FLAG_SIZE, Constants.FLAG_SIZE,
                             flag.getOwner().getPrimaryColor());
    }
  }

  /**
   * Draws information for the given player. Information drawn includes the player's name and gold.
   *
   * @param player     the player whose info will be drawn
   * @param x          the x position to draw at
   * @param y          the y position to draw at
   * @param horizAlign the {@link HorizAlignment} to use
   */
  private void drawPlayerInfo(Player player, int x, int y, HorizAlignment horizAlign) {
    final boolean currentPlayer = player == board.getCurrentPlayer(); // Is is this player's turn?
    renderer().drawString(currentPlayer ? Constants.FONT_SIZE_UI_LARGE : Constants.FONT_SIZE_UI,
                          String.format("%s\nGold: %d", player.getName(), player.getGold()), x, y,
                          currentPlayer ? player.getPrimaryColor() : 0xffffffff,
                          horizAlign, VertAlignment.TOP);
  }

  /**
   * Draws information for the given unit.
   *
   * @param unit the unit whose info will be drawn
   */
  private void drawUnitInfo(Unit unit) {
    final String s = String.format("%s\nHealth: %d/%d\nStrength: %d", unit.getDisplayName(),
                                   unit.getHealth(), unit.getMaxHealth(), unit.getCombatStrength());
    renderer().drawString(Constants.FONT_SIZE_UI, s, Constants.UNIT_INFO_X, Constants.UNIT_INFO_Y,
                          unit.getOwner().getPrimaryColor(),
                          HorizAlignment.LEFT, VertAlignment.BOTTOM);
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
