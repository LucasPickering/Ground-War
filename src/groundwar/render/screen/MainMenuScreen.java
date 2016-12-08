package groundwar.render.screen;

import org.lwjgl.opengl.GL11;

import groundwar.GroundWar;
import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.screen.gui.Button;
import groundwar.render.screen.gui.GuiElement;
import groundwar.util.Constants;
import groundwar.util.Point;

public class MainMenuScreen extends MainScreen {

    private static final int TITLE_Y = 350;
    private static final int NEW_GAME_BUTTON_Y = 1000;
    private static final int EXIT_BUTTON_Y = 1200;

    private Button newGameButton;
    private Button exitButton;

    public MainMenuScreen() {
        addGuiElement(
            newGameButton = new Button("New Game", new Point(center.getX(), NEW_GAME_BUTTON_Y),
                                       HorizAlignment.CENTER, VertAlignment.TOP));
        addGuiElement(exitButton = new Button("Exit Game", new Point(center.getX(), EXIT_BUTTON_Y),
                                              HorizAlignment.CENTER, VertAlignment.TOP));
    }

    @Override
    public void draw(Point mousePos) {
        super.draw(mousePos);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        renderer().drawString(Constants.FONT_SIZE_TITLE, "Ground War",
                              center.getX(), TITLE_Y, HorizAlignment.CENTER);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void onElementClicked(MouseButtonEvent event, GuiElement element) {
        if (element == newGameButton) {
            setNextScreen(new BoardScreen(GroundWar.groundWar.loadNewBoard()));
        } else if (element == exitButton) {
            setNextScreen(null);
        }
    }
}
