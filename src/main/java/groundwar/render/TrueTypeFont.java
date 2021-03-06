package groundwar.render;

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import groundwar.GroundWar;
import groundwar.util.Constants;

public class TrueTypeFont {

    private static final List<String> CHARS = new ArrayList<String>() {{
        add("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        add("abcdefghijklmnopqrstuvwxyz");
        add("0123456789");
        add("ÄÖÜäöüß");
        add(" $+-*/=%\"'#@&_(),.;:?!\\|<>[]§`^~");
    }};

    private final Font font;
    private final FontMetrics fontMetrics;
    private final BufferedImage bufferedImage;
    private final int fontTextureId;
    private final int fontImageWidth;
    private final int fontImageHeight;
    private final float charHeight;

    public TrueTypeFont(String name, float size) throws IOException, FontFormatException {
        // Load the font from the file
        font = Font.createFont(Font.TRUETYPE_FONT, new File(GroundWar.getResource(
            Constants.FONT_PATH, name).getPath())).deriveFont(size);

        // Generate buffered image
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice().getDefaultConfiguration();
        Graphics2D
            graphics =
            gc.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
        graphics.setFont(font);

        // Create the font
        fontMetrics = graphics.getFontMetrics();

        // Get font measurements
        fontImageWidth = CHARS.stream().mapToInt(e -> (int) fontMetrics.getStringBounds(e, null)
            .getWidth()).max().getAsInt();
        charHeight = fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
        fontImageHeight = (int) (CHARS.size() * charHeight);

        // Make an image of the font
        bufferedImage = graphics.getDeviceConfiguration()
            .createCompatibleImage(fontImageWidth, fontImageHeight, Transparency.TRANSLUCENT);

        // Generate texture
        fontTextureId = GL11.glGenTextures();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureId);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, fontImageWidth, fontImageHeight, 0,
                          GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, asByteBuffer());

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    }

    private float getCharX(char c) {
        final String s = Character.toString(c); // String containing just the character
        // Find the string in CHARS that contains the character
        final String originStr = CHARS.stream().filter(e -> e.contains(s)).findFirst().orElse(s);
        return (float) fontMetrics
            .getStringBounds(originStr.substring(0, originStr.indexOf(c)), null)
            .getWidth();
    }

    private float getCharY(char c) {
        final String s = Character.toString(c); // String containing just the character
        int lineId;
        // Find the line containing c
        for (lineId = 0; lineId < CHARS.size(); lineId++) {
            if (CHARS.get(lineId).contains(s)) {
                break;
            }
        }
        return charHeight * lineId;
    }

    private float getCharWidth(char c) {
        return fontMetrics.charWidth(c);
    }

    public float getStringWidth(String s) {
        int width = 0;
        for (char c : s.toCharArray()) {
            width += getCharWidth(c);
        }
        return width;
    }

    private ByteBuffer asByteBuffer() {
        ByteBuffer byteBuffer;

        // Draw the characters on our image
        Graphics2D imageGraphics = (Graphics2D) bufferedImage.getGraphics();
        imageGraphics.setFont(font);
        imageGraphics
            .setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                       RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw every char by line...
        imageGraphics.setColor(Color.WHITE);
        for (int i = 0; i < CHARS.size(); i++) {
            imageGraphics.drawString(CHARS.get(i), 0, fontMetrics.getMaxAscent() + charHeight * i);
        }

        // Generate texture data
        int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0,
                             bufferedImage.getWidth());
        byteBuffer =
            ByteBuffer.allocateDirect(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);

        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int pixel = pixels[y * bufferedImage.getWidth() + x];
                byteBuffer.put((byte) ((pixel >> 16) & 0xFF));  // Red
                byteBuffer.put((byte) ((pixel >> 8) & 0xFF));   // Green
                byteBuffer.put((byte) (pixel & 0xFF));          // Blue
                byteBuffer.put((byte) ((pixel >> 24) & 0xFF));  // Alpha
            }
        }

        byteBuffer.flip();

        return byteBuffer;
    }

    /**
     * Draw the given text in this font.
     *
     * @param text       the text to draw (non-null)
     * @param x          the x location to draw at
     * @param y          the y location to draw at
     * @param color      the color to draw with
     * @param horizAlign the {@link HorizAlignment} to draw with
     * @param vertAlign  the {@link VertAlignment} to draw with
     * @throws NullPointerException if {@code text == null}
     */
    public void draw(String text, int x, int y, int color,
                     HorizAlignment horizAlign, VertAlignment vertAlign) {
        Objects.requireNonNull(text);
        // Set the color (aren't bitshifts cool?)
        GL11.glColor4f((color >> 16 & 0xff) / 255.0f, (color >> 8 & 0xff) / 255.0f,
                       (color & 0xff) / 255.0f, (color >> 24 & 0xff) / 255.0f);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureId);
        String[] lines = text.split("\n");

        int xTmp = x;
        int yTmp = y;

        // Adjust y for vertical alignment
        switch (vertAlign) {
            case CENTER:
                yTmp -= charHeight * lines.length / 2;
                break;
            case BOTTOM:
                yTmp -= charHeight * lines.length;
                break;
        }

        GL11.glBegin(GL11.GL_QUADS);
        for (String line : lines) {
            // Adjust x for horizontal alignment
            switch (horizAlign) {
                case CENTER:
                    xTmp = x - (int) getStringWidth(line) / 2;
                    break;
                case RIGHT:
                    xTmp = x - (int) getStringWidth(line);
                    break;
            }

            // Draw each character
            for (char c : line.toCharArray()) {
                final float width = getCharWidth(c);

                final float cw = 1f / fontImageWidth * width; // Character width
                final float ch = 1f / fontImageHeight * charHeight; // Character height
                final float cx = 1f / fontImageWidth * getCharX(c); // Character x-pos in texture
                final float cy = 1f / fontImageHeight * getCharY(c); // Character y-pos in texture

                // Top-left corner
                GL11.glTexCoord2f(cx, cy);
                GL11.glVertex2f(xTmp, yTmp);

                // Top-right corner
                GL11.glTexCoord2f(cx + cw, cy);
                GL11.glVertex2f(xTmp + width, yTmp);

                // Bottom-right corner
                GL11.glTexCoord2f(cx + cw, cy + ch);
                GL11.glVertex2f(xTmp + width, yTmp + charHeight);

                // Bottom-left corner
                GL11.glTexCoord2f(cx, cy + ch);
                GL11.glVertex2f(xTmp, yTmp + charHeight);

                // Increase x for the next character
                xTmp += width;
            }

            // Reset x and increase y for the next line
            xTmp = x;
            yTmp += charHeight;
        }
        GL11.glEnd();
    }

    public void delete() {
        GL11.glDeleteTextures(fontTextureId);
    }
}