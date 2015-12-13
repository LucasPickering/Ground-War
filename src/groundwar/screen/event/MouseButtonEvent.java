package groundwar.screen.event;

public class MouseButtonEvent extends Event {

  public final int button;
  public final int mods;
  public final int mouseX;
  public final int mouseY;

  public MouseButtonEvent(long window, int button, int mods, int mouseX, int mouseY) {
    super(window);
    this.button = button;
    this.mods = mods;
    this.mouseX = mouseX;
    this.mouseY = mouseY;
  }

}
