package groundwar.screen;

/**
 * A {@code MainScreen} is a type of {@link ScreenElement} that is meant to be a top-level element. A
 * {@code MainScreen} has no parent {@link ScreenElement} and there can only ever be one active {@code
 * MainScreen} at a time. Examples of a {@code MainScreen} include the main menu screen and the
 * in-game screen.
 */
public abstract class MainScreen implements ScreenElement {

  @Override
  public void draw(long window, int mouseX, int mouseY) {
  }

  @Override
  public boolean insideElement(int x, int y) {
    return true;
  }

  @Override
  public void onClicked(int mouseX, int mouseY, int button, int mods) {}
}
