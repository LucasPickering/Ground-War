package groundwar.render.screen.gui;

import org.lwjgl.opengl.GL11;

import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
import groundwar.util.Colors;
import groundwar.util.Point;

public class TextDisplay extends GuiElement {

  private String text;
  private float fontSize;
  private int textColor = 0xffffffff;

  public TextDisplay(String text, float fontSize, Point pos, int width, int height) {
    super(pos, width, height);
    this.text = text;
    this.fontSize = fontSize;
  }

  public TextDisplay(String text, float fontSize, Point pos, int width, int height,
                     HorizAlignment horizAlign, VertAlignment vertAlign) {
    super(pos, width, height, horizAlign, vertAlign);
    this.text = text;
    this.fontSize = fontSize;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public float getFontSize() {
    return fontSize;
  }

  public void setFontSize(float fontSize) {
    this.fontSize = fontSize;
  }

  public int getTextColor() {
    return textColor;
  }

  public void setTextColor(int textColor) {
    this.textColor = textColor;
  }

  @Override
  public void draw(Point mousePos) {
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    renderer().drawRect(0, 0, getWidth(), getHeight(), Colors.UNIT_INFO_BG);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    renderer().drawString(fontSize, text, 0, 0, textColor);
  }
}
