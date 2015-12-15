package groundwar.screen.gui;

import org.lwjgl.opengl.GL11;

import groundwar.screen.Texture;

public class Button extends GuiElement {

  private String text;
  private final Texture texture;

  public Button(long window, int x, int y, int width, int height, String text) {
    super(window, x, y, width, height);
    this.text = text;
    texture = Texture.loadTexture("/textures/tile_background.png");
  }

  @Override
  public void draw(int mouseX, int mouseY) {
    /*
    if (contains(mouseX, mouseY)) {
      GL11.glColor3f(1.0f, 0.0f, 0.0f);
    } else {
      GL11.glColor3f(0.0f, 0.0f, 1.0f);
    }
    */

    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
    GL11.glBegin(GL11.GL_QUADS);
    {
      final int x1 = getX();
      final int y1 = getY();
      final int x2 = getX() + getWidth();
      final int y2 = getY() + getHeight();
      GL11.glTexCoord2f(0, 0);
      GL11.glVertex2f(x1, y1);

      GL11.glTexCoord2f(1, 0);
      GL11.glVertex2f(x2, y1);

      GL11.glTexCoord2f(1, 1);
      GL11.glVertex2f(x2, y2);

      GL11.glTexCoord2f(0, 1);
      GL11.glVertex2f(x1, y2);
    }
    GL11.glEnd();
    GL11.glDisable(GL11.GL_TEXTURE_2D);
  }
}
