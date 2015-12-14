package groundwar.screen;

import groundwar.screen.gui.Button;

public class IngameScreen extends MainScreen {

  Button testButton;

  public IngameScreen() {
    guiElements.add(testButton = new Button("Hi", 100, 100, 200, 20));
    testButton.setMouseButtonHandler(this::onClick);
  }
}
