package groundwar.screen;

import org.lwjgl.opengl.GL11;

public class IngameScreen extends MainScreen {

  @Override
  public void draw(long window, int mouseX, int mouseY) {
    super.draw(window, mouseX, mouseY);
    GL11.glColor3f(1.0f, 0.0f, 0.0f);

    GL11.glBegin(GL11.GL_QUADS);
    {
      GL11.glVertex2f(0, 0);
      GL11.glVertex2f(100, 0);
      GL11.glVertex2f(100, 100);
      GL11.glVertex2f(0, 100);
    }
    GL11.glEnd();
  }
}
