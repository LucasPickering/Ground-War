package groundwar.render.screen;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.Collection;

import groundwar.board.Board;
import groundwar.board.Flag;
import groundwar.board.Player;
import groundwar.board.PlayerInfo;
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
    private static final Point ORANGE_UI_POS = new Point(10, 10);
    private static final Point BLUE_UI_POS = new Point(3830, 10);
    private static final int UNIT_HEALTH_WIDTH = (int) (Constants.TILE_WIDTH * 0.6f);
    private static final int UNIT_HEALTH_HEIGHT = 18;
    private static final Point UNIT_HEALTH_POS =
        new Point((Constants.TILE_WIDTH - UNIT_HEALTH_WIDTH) / 2, 46);
    private static final Point UNIT_MOVES_POS = new Point(60, Constants.TILE_HEIGHT - 80);
    private static final Point FLAG_POS = new Point(140, Constants.TILE_HEIGHT - 70);
    private static final int FLAG_SIZE = 48;
    private static final Point UNIT_INFO_POS = new Point(20, -10);
    private static final int UNIT_INFO_WIDTH = 370;
    private static final int UNIT_INFO_HEIGHT = 200;

    private final Board board;
    private final TextDisplay mouseOverUnitInfo;

    public BoardScreen(Board board) {
        this.board = board;
        addGuiElement(mouseOverUnitInfo = new TextDisplay(null, new Point(), 0, 0,
                                                          HorizAlignment.LEFT,
                                                          VertAlignment.BOTTOM));
        mouseOverUnitInfo.setVisible(false);
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

        // Draw the players' information
        drawPlayerInfo(board.getPlayer(PlayerInfo.ORANGE), ORANGE_UI_POS, HorizAlignment.LEFT);
        drawPlayerInfo(board.getPlayer(PlayerInfo.BLUE), BLUE_UI_POS, HorizAlignment.RIGHT);

        // Draw info for the currently-selected unit
        if (board.hasSelectedTile() && board.getSelectedTile().getUnit() != null) {
            drawUnitInfoSidebar(board.getSelectedTile().getUnit());
        }

        // Update mouseOverUnitInfo for the unit that the mouse is over
        for (Tile tile : tiles) {
            if (tile.contains(mousePos) && tile.hasUnit()) {
                final Unit unit = tile.getUnit();
                mouseOverUnitInfo.setText(unit.getInfoString());
                mouseOverUnitInfo.setPos(mousePos.plus(UNIT_INFO_POS));
                mouseOverUnitInfo.setWidth(UNIT_INFO_WIDTH);
                mouseOverUnitInfo.setHeight(UNIT_INFO_HEIGHT);
                mouseOverUnitInfo.setTextColor(unit.getOwner().getInfo().primaryColor);
                mouseOverUnitInfo.setVisible(true);
                break;
            }
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        super.draw(mousePos); // Draw GUI elements
        mouseOverUnitInfo.setVisible(false); // Hide the unit info, to be updated on the next frame

        // If the game is over, go to the victory screen
        if (board.isGameOver()) {
            setNextScreen(new VictoryScreen(board));
        }
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
        renderer()
            .drawTexture(Constants.TILE_BG_NAME, 0, 0, width, height, tile.getBackgroundColor());

        // Draw the regular foreground
        renderer()
            .drawTexture(Constants.TILE_OUTLINE_NAME, 0, 0, width, height, tile.getOutlineColor());

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
                                                : ColorTexture.invalidSpawning)
                    .draw(0, 0, width, height);
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
                renderer().drawString(Constants.FONT_SIZE_TILE,
                                      Integer.toString(unit.getMovesRemaining()),
                                      UNIT_MOVES_POS.getX(), UNIT_MOVES_POS.getY());
            }

            // Draw the health bar
            final int splitPoint = UNIT_HEALTH_WIDTH * unit.getHealth() / unit.getMaxHealth();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            renderer().drawRect(UNIT_HEALTH_POS.getX(), UNIT_HEALTH_POS.getY(),
                                splitPoint, UNIT_HEALTH_HEIGHT, Colors.HEALTH_BAR_POS);
            renderer().drawRect(UNIT_HEALTH_POS.getX() + splitPoint, UNIT_HEALTH_POS.getY(),
                                UNIT_HEALTH_WIDTH - splitPoint, UNIT_HEALTH_HEIGHT,
                                Colors.HEALTH_BAR_NEG);
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
            renderer().drawTexture(Constants.FLAG_NAME, FLAG_POS.getX(), FLAG_POS.getY(),
                                   FLAG_SIZE, FLAG_SIZE,
                                   flag.getOwner().getInfo().primaryColor);
        }
    }

    /**
     * Draws information for the given player. Information drawn includes the player's name and
     * gold.
     *
     * @param player     the player whose info will be drawn
     * @param pos        the position to draw at
     * @param horizAlign the {@link HorizAlignment} to use
     */
    private void drawPlayerInfo(Player player, Point pos, HorizAlignment horizAlign) {
        final boolean
            currentPlayer =
            player == board.getCurrentPlayer(); // Is it this player's turn?
        renderer().drawString(currentPlayer ? Constants.FONT_SIZE_UI_LARGE : Constants.FONT_SIZE_UI,
                              player.getInfoString(), pos.getX(), pos.getY(),
                              currentPlayer ? player.getInfo().primaryColor : 0xffffffff,
                              horizAlign, VertAlignment.TOP);
    }

    /**
     * Draws information for the given unitin the sidebar. Typically this is done for the
     * currently-selecetd unit.
     *
     * @param unit the unit whose info will be drawn
     */
    private void drawUnitInfoSidebar(Unit unit) {
        PlayerInfo playerInfo = unit.getOwner().getInfo();
        renderer().drawString(Constants.FONT_SIZE_UI, unit.getInfoString(),
                              playerInfo.infoPos.getX(), playerInfo.infoPos.getY(),
                              0xffffffff, playerInfo.textHorizAlign, VertAlignment.TOP);
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
                if (board.getSpawningUnit() != null || board.hasSelectedTile()) {
                    board.cancelSpawning();
                    board.unselectTile();
                } else {
                    setNextScreen(new PauseMenu(this));
                }
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
