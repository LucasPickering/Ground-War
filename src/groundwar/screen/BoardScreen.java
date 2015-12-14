package groundwar.screen;

import groundwar.screen.gui.Button;

public class BoardScreen extends MainScreen {

  Button testButton;

  public BoardScreen() {
    guiElements.add(testButton = new Button("Hi", 100, 100, 200, 20));
    testButton.setMouseButtonHandler(this::onClick);
  }
}
