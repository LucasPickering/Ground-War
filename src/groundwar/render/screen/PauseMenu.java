package groundwar.render.screen;

import groundwar.GroundWar;
import groundwar.board.Board;
import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.screen.gui.Button;
import groundwar.render.screen.gui.GuiElement;
import groundwar.util.Point;

public class PauseMenu extends MainScreen {

  private static final int BACK_TO_GAME_Y = 600;
  private static final int EXIT_TO_MENU_Y = 800;
  private static final int EXIT_TO_DESKTOP_Y = 1000;

  private final Board board;
  private final Button backToGameButton;
  private final Button mainMenuButton;
  private final Button desktopButton;

  /**
   * Constructs a new PauseMenu. The given board is the board that will be returned when the game is
   * resumed.
   *
   * @param board the board to eventually return to
   */
  public PauseMenu(Board board) {
    this.board = board;
    addGuiElement(backToGameButton = new Button("Back to Game", new Point(center.getX(),
                                                                          BACK_TO_GAME_Y),
                                                HorizAlignment.CENTER, VertAlignment.TOP));

    addGuiElement(mainMenuButton = new Button("Exit to Menu", new Point(center.getX(),
                                                                        EXIT_TO_MENU_Y),
                                              HorizAlignment.CENTER, VertAlignment.TOP));

    addGuiElement(desktopButton = new Button("Exit to Desktop", new Point(center.getX(),
                                                                          EXIT_TO_DESKTOP_Y),
                                             HorizAlignment.CENTER, VertAlignment.TOP));
  }

  @Override
  public void onElementClicked(MouseButtonEvent event, GuiElement element) {
    if (element == backToGameButton) {
      setNextScreen(new BoardScreen(board));
    } else if (element == mainMenuButton) {
      setNextScreen(new MainMenuScreen());
    } else if (element == desktopButton) {
      GroundWar.groundWar.exitGame();
    }
  }
}
