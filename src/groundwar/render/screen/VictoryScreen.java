package groundwar.render.screen;

import org.lwjgl.opengl.GL11;

import groundwar.board.Board;
import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.screen.gui.Button;
import groundwar.render.screen.gui.GuiElement;
import groundwar.util.Constants;
import groundwar.util.Point;

public class VictoryScreen extends MainScreen {

  private final Board board;
  private final Button menuButton;
  private boolean backToMenu;

  public VictoryScreen(Board board) {
    this.board = board;
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    addGuiElement(menuButton = new Button.Builder().setX(center.getX()).setY(600)
        .setText("Main Menu").setHorizAlign(HorizAlignment.CENTER).build());
  }

  @Override
  public void draw(Point mousePos) {
    super.draw(mousePos);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    renderer().drawString(
        Constants.FONT_SIZE_TITLE,
        String.format("%s wins\nin %d turns!", board.getWinner(), board.getTurnCount()),
        center.getX(), center.getY(), board.getWinner().getPrimaryColor(),
        HorizAlignment.CENTER, VertAlignment.CENTER);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);
  }

  @Override
  public MainScreen nextScreen() {
    if (backToMenu) {
      return new MainMenuScreen();
    }
    return this;
  }

  @Override
  public void onElementClicked(MouseButtonEvent event, GuiElement element) {
    if (element == menuButton) {
      backToMenu = true;
    }
  }
}
