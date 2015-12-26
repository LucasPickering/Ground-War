package groundwar.render.screen;

import org.lwjgl.opengl.GL11;

import groundwar.GroundWar;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.screen.gui.Button;
import groundwar.render.screen.gui.GuiElement;
import groundwar.util.Constants;

public class MainMenuScreen extends MainScreen {

  private MainScreen nextScreen;
  private Button newGameButton;

  public MainMenuScreen(long window) {
    super(window);
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    addGuiElement(newGameButton = new Button(window, center.getX() - Constants.BUTTON_WIDTH / 2,
                                             1000, "New Game"));
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
      nextScreen = new BoardScreen(window, GroundWar.groundWar.loadNewBoard());
    }
  }
}
