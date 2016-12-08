package groundwar.render.screen.gui;

import org.lwjgl.opengl.GL11;

import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Point;

public class TextDisplay extends GuiElement {

    private static final int TEXT_OFFSET_X = 8;

    private String text;
    private int textColor = 0xffffffff;

    public TextDisplay(String text, Point pos, int width, int height) {
        super(pos, width, height);
        this.text = text;
    }

    public TextDisplay(String text, Point pos, int width, int height,
                       HorizAlignment horizAlign, VertAlignment vertAlign) {
        super(pos, width, height, horizAlign, vertAlign);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        renderer().drawString(Constants.FONT_SIZE_TILE, text, TEXT_OFFSET_X, 0, textColor);
    }
}
