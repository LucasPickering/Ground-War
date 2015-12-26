package groundwar.render.screen;

import org.lwjgl.opengl.GL11;

import groundwar.GroundWar;
import groundwar.render.HorizAlignment;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.screen.gui.Button;
import groundwar.render.screen.gui.GuiElement;

public class MainMenuScreen extends MainScreen {

  private MainScreen nextScreen;
  private Button newGameButton;

  public MainMenuScreen() {
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    addGuiElement(newGameButton = new Button.Builder().setX(center.getX()).setY(1000)
        .setText("New Game").setHorizAlign(HorizAlignment.CENTER).build());
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
    }
  }
}
