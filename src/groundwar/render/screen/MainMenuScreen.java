package groundwar.render.screen;

import org.lwjgl.opengl.GL11;

import groundwar.GroundWar;
import groundwar.render.HorizAlignment;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.screen.gui.Button;
import groundwar.render.screen.gui.GuiElement;
import groundwar.util.Constants;
import groundwar.util.Point;

public class MainMenuScreen extends MainScreen {

  private MainScreen nextScreen;
  private Button newGameButton;
  private Button exitButton;

  public MainMenuScreen() {
    addGuiElement(newGameButton = new Button.Builder().setText("New Game").setX(center.getX())
        .setY(Constants.MAIN_NEW_GAME_BUTTON_Y).setHorizAlign(HorizAlignment.CENTER).build());
    addGuiElement(exitButton = new Button.Builder().setText("Exit Game").setX(center.getX())
        .setY(Constants.MAIN_EXIT_BUTTON_Y).setHorizAlign(HorizAlignment.CENTER).build());
  }

  @Override
  public void draw(Point mousePos) {
    super.draw(mousePos);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    renderer().drawString(Constants.FONT_SIZE_TITLE, "Ground War",
                          center.getX(), Constants.MAIN_TITLE_Y, HorizAlignment.CENTER);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);
  }

  @Override
  public MainScreen nextScreen() {
    if (nextScreen == null) {
      return this;
    }
    return nextScreen;
  }

  @Override
  public void onElementClicked(MouseButtonEvent event, GuiElement element) {
    if (element == newGameButton) {
      nextScreen = new BoardScreen(GroundWar.groundWar.loadNewBoard());
    } else if (element == exitButton) {
      GroundWar.groundWar.exitGame();
    }
  }
}
