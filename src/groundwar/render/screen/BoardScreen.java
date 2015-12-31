package groundwar.render.screen;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.Collection;

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
import groundwar.render.screen.gui.TextDisplay;
import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Point;

public class BoardScreen extends MainScreen {

  private static final int TURN_COUNT_Y = 10;
  private static final int ORANGE_UI_X = 10;
  private static final int ORANGE_UI_Y = 10;
  private static final int BLUE_UI_X = 3830;
  private static final int BLUE_UI_Y = 10;
  private static final int UNIT_HEALTH_WIDTH = (int) (Constants.TILE_WIDTH * 0.6f);
  private static final int UNIT_HEALTH_HEIGHT = 18;
  private static final int UNIT_HEALTH_X = (Constants.TILE_WIDTH - UNIT_HEALTH_WIDTH) / 2; // Centered
  private static final int UNIT_HEALTH_Y = 46;
  private static final int UNIT_MOVES_X = 60;
  private static final int UNIT_MOVES_Y = Constants.TILE_HEIGHT - 80;
  private static final int UNIT_INFO_X = 20;
  private static final int UNIT_INFO_Y = -10;
  private static final int UNIT_INFO_WIDTH = 370;
  private static final int UNIT_INFO_HEIGHT = 200;
  private static final int FLAG_X = 140;
  private static final int FLAG_Y = Constants.TILE_HEIGHT - 70;
  private static final int FLAG_SIZE = 48;

  private final Board board;
  private final TextDisplay unitInfo;

  public BoardScreen(Board board) {
    this.board = board;
    addGuiElement(unitInfo = new TextDisplay(null, new Point(), 0, 0,
                                             HorizAlignment.LEFT, VertAlignment.BOTTOM));
    unitInfo.setVisible(false);
  }

  @Override
  public void draw(Point mousePos) {
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_TEXTURE_2D);

    final Collection<Tile> tiles = board.getTiles().values();

    tiles.forEach(tile -> drawTile(tile, mousePos)); // Draw each tile

    // Draw turn counter
    renderer().drawString(Constants.FONT_SIZE_UI, "Turn " + board.getTurnCount(),
                          center.getX(), TURN_COUNT_Y, HorizAlignment.CENTER);

    // Draw the players's information
    drawPlayerInfo(board.getPlayer(PlayerColor.ORANGE), ORANGE_UI_X, ORANGE_UI_Y,
                   HorizAlignment.LEFT);
    drawPlayerInfo(board.getPlayer(PlayerColor.BLUE), BLUE_UI_X, BLUE_UI_Y,
                   HorizAlignment.RIGHT);

    // Update unitInfo for the unit that the mouse is over
    for (Tile tile : tiles) {
      if (tile.contains(mousePos) && tile.hasUnit()) {
        final Unit unit = tile.getUnit();
        unitInfo.setText(unit.getInfoString());
        unitInfo.setPos(mousePos.plus(UNIT_INFO_X, UNIT_INFO_Y));
        unitInfo.setWidth(UNIT_INFO_WIDTH);
        unitInfo.setHeight(UNIT_INFO_HEIGHT);
        unitInfo.setTextColor(unit.getOwner().getPrimaryColor());
        unitInfo.setVisible(true);
        break;
      }
    }

    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);

    super.draw(mousePos); // Draw GUI elements
    unitInfo.setVisible(false); // Hide the unit info, to be updated on the next frame
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
   * @param mousePos the position of the mouse
   */
  private void drawTile(Tile tile, Point mousePos) {
    GL11.glPushMatrix();
    GL11.glTranslatef(tile.getScreenPos().getX(), tile.getScreenPos().getY(), 0f);

    final int width = Constants.TILE_WIDTH;
    final int height = Constants.TILE_HEIGHT;

    // Draw the regular background
    renderer().drawTexture(Constants.TILE_BG_NAME, 0, 0, width, height, tile.getBackgroundColor());

    // Draw the regular foreground
    renderer().drawTexture(Constants.TILE_OUTLINE_NAME, 0, 0, width, height, tile.getOutlineColor());

    drawUnit(tile.getUnit()); // Draw the unit on top
    drawFlag(tile.getFlag()); // Draw the flag on top of that
    drawTileOverlays(tile, mousePos); // Draw the tile overlays on top of everything else

    GL11.glPopMatrix();
  }

  /**
   * Draw the appropriate overlays for the given tile.
   *
   * @param tile     the tile to draw
   * @param mousePos the position of the mouse
   */
  private void drawTileOverlays(Tile tile, Point mousePos) {
    final int width = Constants.TILE_WIDTH;
    final int height = Constants.TILE_HEIGHT;

    // Draw selection-related overlays
    if (board.isSelected(tile)) { // If this tile is selected
      ColorTexture.selected.draw(0, 0, width, height); // Draw the selected overlay
    } else if (board.hasSelectedTile()) { // Otherwise, if another tile is selected...
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
                              UNIT_MOVES_X, UNIT_MOVES_Y);
      }

      // Draw the health bar
      final int splitPoint = UNIT_HEALTH_WIDTH * unit.getHealth() / unit.getMaxHealth();
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      renderer().drawRect(UNIT_HEALTH_X, UNIT_HEALTH_Y, splitPoint, UNIT_HEALTH_HEIGHT,
                          Colors.HEALTH_BAR_POS);
      renderer().drawRect(UNIT_HEALTH_X + splitPoint, UNIT_HEALTH_Y,
                          UNIT_HEALTH_WIDTH - splitPoint, UNIT_HEALTH_HEIGHT, Colors.HEALTH_BAR_NEG);
      GL11.glEnable(GL11.GL_TEXTURE_2D);

      drawFlag(unit.getFlag()); // Draw the flag, if the unit has one
    }
  }

  /**
   * Draws the given flag. If {@code flag == null}, nothing happens.
   *
   * @param flag the flag to be drawn
   */
  private void drawFlag(Flag flag) {
    if (flag != null) {
      renderer().drawTexture(Constants.FLAG_NAME, FLAG_X, FLAG_Y,
                             FLAG_SIZE, FLAG_SIZE,
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
                          player.getInfoString(), x, y,
                          currentPlayer ? player.getPrimaryColor() : 0xffffffff,
                          horizAlign, VertAlignment.TOP);
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
