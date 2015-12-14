package groundwar.screen.gui;

import org.lwjgl.opengl.GL11;

public class Button extends GuiElement {

  private String text;

  public Button(String text, int x, int y, int width, int height) {
    super(x, y, width, height);
    this.text = text;
  }

  @Override
  public void draw(long window, int mouseX, int mouseY) {
    if (contains(mouseX, mouseY)) {
      GL11.glColor3f(1.0f, 0.0f, 0.0f);
    } else {
      GL11.glColor3f(0.0f, 0.0f, 1.0f);
    }

    GL11.glPushMatrix();
    GL11.glBegin(GL11.GL_QUADS);
    {
      final int x1 = getX();
      final int y1 = getY();
      final int x2 = getX() + getWidth();
      final int y2 = getY() + getHeight();
      GL11.glVertex2f(x1, y1);
      GL11.glVertex2f(x2, y1);
      GL11.glVertex2f(x2, y2);
      GL11.glVertex2f(x1, y2);
    }
    GL11.glPopMatrix();
    GL11.glEnd();
  }
}
