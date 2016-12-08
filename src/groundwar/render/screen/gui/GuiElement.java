package groundwar.render.screen.gui;

import java.util.Objects;

import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
import groundwar.render.screen.ScreenElement;
import groundwar.util.Point;

public abstract class GuiElement implements ScreenElement {

    private Point pos;
    private int width;
    private int height;
    private HorizAlignment horizAlign;
    private VertAlignment vertAlign;
    private boolean visible = true;
    private boolean enabled = true;

    /**
     * Constructs a new {@code GuiElement} with the given coordinates and size.
     *
     * @param pos    the position of the element
     * @param width  the width of the element (non-negative)
     * @param height the height of the element (non-negative)
     * @throws IllegalArgumentException if width or height is non-positive
     */
    protected GuiElement(Point pos, int width, int height) {
        this(pos, width, height, HorizAlignment.LEFT, VertAlignment.TOP);
    }

    /**
     * Constructs a new {@code GuiElement} with the given coordinates, size, and alignments.
     *
     * @param pos        the position of the element
     * @param width      the width of the element (non-negative)
     * @param height     the height of the element (non-negative)
     * @param horizAlign the horizontal alignment of the element (non-null)
     * @param vertAlign  the vertical alignment of the element (non-null)
     * @throws IllegalArgumentException if width or height is non-positive
     * @throws NullPointerException     if {@code horizAlign == null} or {@code vertAlign == null}
     */
    protected GuiElement(Point pos, int width, int height,
                         HorizAlignment horizAlign, VertAlignment vertAlign) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width or height out of bounds");
        }
        Objects.requireNonNull(horizAlign);
        Objects.requireNonNull(vertAlign);

        this.width = width;
        this.height = height;
        this.horizAlign = horizAlign;
        this.vertAlign = vertAlign;
        setPos(pos);
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = adjustForAlignment(pos);
    }

    public int getX() {
        return pos.getX();
    }

    public int getY() {
        return pos.getY();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHorizAlign(HorizAlignment horizAlign) {
        this.horizAlign = horizAlign;
        pos = adjustForAlignment(pos);
    }

    public void setVertAlign(VertAlignment vertAlign) {
        this.vertAlign = vertAlign;
        pos = adjustForAlignment(pos);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEnabled() {
        return visible && enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private Point adjustForAlignment(Point p) {
        int adjX = 0;
        int adjY = 0;

        // Adjust x for horizontal alignment
        switch (horizAlign) {
            case CENTER:
                adjX = -width / 2;
                break;
            case RIGHT:
                adjX = -width;
                break;
        }

        // Adjust y for vertical alignment
        switch (vertAlign) {
            case CENTER:
                adjY = -height / 2;
                break;
            case BOTTOM:
                adjY = -height;
                break;
        }

        return p.plus(adjX, adjY);
    }

    @Override
    public boolean contains(Point p) {
        return pos.getX() <= p.getX() && p.getX() <= pos.getX() + width
               && pos.getY() <= p.getY() && p.getY() <= pos.getY() + height;
    }
}
