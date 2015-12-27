package groundwar.render.screen.gui;

import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Point;

public class Button extends GuiElement {

  public static class Builder {

    private int x;
    private int y;
    private int width = Constants.BUTTON_WIDTH;
    private int height = Constants.BUTTON_HEIGHT;
    private String text = "";
    private HorizAlignment horizAlign = HorizAlignment.LEFT;
    private VertAlignment vertAlign = VertAlignment.TOP;

    public Builder setX(int x) {
      this.x = x;
      return this;
    }

    public Builder setY(int y) {
      this.y = y;
      return this;
    }

    public Builder setWidth(int width) {
      this.width = width;
      return this;
    }

    public Builder setHeight(int height) {
      this.height = height;
      return this;
    }

    public Builder setText(String text) {
      this.text = text;
      return this;
    }

    public Builder setHorizAlign(HorizAlignment horizAlign) {
      this.horizAlign = horizAlign;
      return this;
    }

    public Builder setVertAlign(VertAlignment vertAlign) {
      this.vertAlign = vertAlign;
      return this;
    }

    public Button build() {
      // Adjust x for horizontal alignment
      switch (horizAlign) {
        case CENTER:
          x -= width / 2;
          break;
        case RIGHT:
          x -= width;
          break;
      }

      // Adjust y for vertical alignment
      switch (vertAlign) {
        case CENTER:
          y -= height / 2;
          break;
        case BOTTOM:
          y -= height;
          break;
      }

      return new Button(x, y, width, height, text);
    }
  }

  private String text;

  private Button(int x, int y, int width, int height, String text) {
    super(x, y, width, height);
    this.text = text;
  }

  @Override
  public void draw(Point mousePos) {
    final boolean mouseOver = contains(mousePos);
    renderer().drawTexture(Constants.BUTTON_NAME, 0, 0, getWidth(), getHeight());
    renderer().drawString(Constants.FONT_SIZE_UI, text, getWidth() / 2, getHeight() / 2,
                          mouseOver ? Colors.BUTTON_TEXT_HIGHLIGHT : Colors.BUTTON_TEXT_NORMAL,
                          HorizAlignment.CENTER, VertAlignment.CENTER);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
