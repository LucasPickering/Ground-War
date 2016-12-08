package groundwar.render.screen;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
import groundwar.render.event.KeyEvent;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.screen.gui.Button;
import groundwar.render.screen.gui.GuiElement;
import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Point;

public class PauseMenu extends MainScreen {

    private static final int BACK_TO_GAME_Y = 600;
    private static final int HELP_Y = 800;
    private static final int EXIT_TO_MENU_Y = 1000;
    private static final int EXIT_TO_DESKTOP_Y = 1200;

    private final BoardScreen boardScreen;
    private final Button backToGameButton;
    private final Button helpButton;
    private final Button mainMenuButton;
    private final Button desktopButton;

    /**
     * Constructs a new PauseMenu. The given screen is the {@link BoardScreen} that will be returned
     * when the game is resumed.
     *
     * @param boardScreen the screen to eventually return to
     */
    public PauseMenu(BoardScreen boardScreen) {
        this.boardScreen = boardScreen;
        addGuiElement(backToGameButton = new Button("Resume Game", new Point(center.getX(),
                                                                             BACK_TO_GAME_Y),
                                                    HorizAlignment.CENTER, VertAlignment.TOP));

        addGuiElement(helpButton = new Button("Help", new Point(center.getX(), HELP_Y),
                                              HorizAlignment.CENTER, VertAlignment.TOP));

        addGuiElement(mainMenuButton = new Button("Exit to Menu", new Point(center.getX(),
                                                                            EXIT_TO_MENU_Y),
                                                  HorizAlignment.CENTER, VertAlignment.TOP));

        addGuiElement(desktopButton = new Button("Exit to Desktop", new Point(center.getX(),
                                                                              EXIT_TO_DESKTOP_Y),
                                                 HorizAlignment.CENTER, VertAlignment.TOP));
    }

    @Override
    public void draw(Point mousePos) {
        boardScreen.draw(new Point());

        GL11.glEnable(GL11.GL_BLEND);
        renderer().drawRect(0, 0, Constants.RES_WIDTH, Constants.RES_HEIGHT, Colors.MENU_SHADER);
        GL11.glDisable(GL11.GL_BLEND);

        super.draw(mousePos);
    }

    @Override
    public void onKey(KeyEvent event) {
        switch (event.key) {
            case GLFW.GLFW_KEY_ESCAPE:
                returnToGame();
                break;
        }
    }

    @Override
    public void onElementClicked(MouseButtonEvent event, GuiElement element) {
        if (element == backToGameButton) {
            returnToGame();
        } else if (element == mainMenuButton) {
            setNextScreen(new MainMenuScreen());
        } else if (element == desktopButton) {
            setNextScreen(null);
        }
    }

    private void returnToGame() {
        boardScreen.setNextScreen(boardScreen);
        setNextScreen(boardScreen);
    }
}
