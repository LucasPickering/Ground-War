package groundwar.screen;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class IngameScreen extends MainScreen {

  @Override
  public void draw(long window, int mouseX, int mouseY) {
    super.draw(window, mouseX, mouseY);
    glColor3f(1.0f, 0.0f, 0.0f);

    glBegin(GL_QUADS);
    {
      glVertex2f(0, 0);
      glVertex2f(100, 0);
      glVertex2f(100, 100);
      glVertex2f(0, 100);
    }
    glEnd();
  }
}
