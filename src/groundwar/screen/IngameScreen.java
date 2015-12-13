package groundwar.screen;

import groundwar.screen.gui.Button;

public class IngameScreen extends MainScreen {

  public IngameScreen() {
    guiElements.add(new Button("Hi", 100, 100, 200, 20));
  }

  @Override
  public void draw(long window, int mouseX, int mouseY) {
    super.draw(window, mouseX, mouseY);
  }
}
