package groundwar.render;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class TrueTypeFont {

  //Constants
  private final Map<Integer, String> CHARS = new HashMap<Integer, String>() {{
    put(0, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    put(1, "abcdefghijklmnopqrstuvwxyz");
    put(2, "0123456789");
    put(3, "ÄÖÜäöüß");
    put(4, " $+-*/=%\"'#@&_(),.;:?!\\|<>[]§`^~");
  }};

  //Variables
  private Font font;
  private FontMetrics fontMetrics;
  private BufferedImage bufferedImage;
  private int fontTextureId;

  //Getters
  public float getFontImageWidth() {
    return (float) CHARS.values().stream()
        .mapToDouble(e -> fontMetrics.getStringBounds(e, null).getWidth()).max().getAsDouble();
  }

  public float getFontImageHeight() {
    return (float) CHARS.keySet().size() * getCharHeight();
  }

  public float getCharX(char c) {
    String originStr =
        CHARS.values().stream().filter(e -> e.contains("" + c)).findFirst().orElse("" + c);
    return (float) fontMetrics.getStringBounds(originStr.substring(0, originStr.indexOf(c)), null)
        .getWidth();
  }

  public float getCharY(char c) {
    float
        lineId =
        (float) CHARS.keySet().stream().filter(i -> CHARS.get(i).contains("" + c)).findFirst()
            .orElse(0);
    return getCharHeight() * lineId;
  }

  public float getCharWidth(char c) {
    return fontMetrics.charWidth(c);
  }

  public float getCharHeight() {
    return (float) (fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent());
  }

  public TrueTypeFont(String path, float size) {
    try {
      font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
    } catch (IOException | FontFormatException e) {
      System.err.println("Error creating font: " + path);
      e.printStackTrace();
    }

    //Generate buffered image
    GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getDefaultScreenDevice().getDefaultConfiguration();
    Graphics2D graphics = gc.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
    graphics.setFont(font);

    fontMetrics = graphics.getFontMetrics();
    bufferedImage = graphics.getDeviceConfiguration()
        .createCompatibleImage((int) getFontImageWidth(), (int) getFontImageHeight(),
                               Transparency.TRANSLUCENT);

    //Generate texture
    fontTextureId = GL11.glGenTextures();
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureId);
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                      (int) getFontImageWidth(), (int) getFontImageHeight(), 0,
                      GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, asByteBuffer());

    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
  }

  public void drawText(String text, int x, int y) {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureId);

    GL11.glBegin(GL11.GL_QUADS);
    int xTmp = x;
    for (char c : text.toCharArray()) {
      float width = getCharWidth(c);
      float height = getCharHeight();
      float cw = 1f / getFontImageWidth() * width;
      float ch = 1f / getFontImageHeight() * height;
      float cx = 1f / getFontImageWidth() * getCharX(c);
      float cy = 1f / getFontImageHeight() * getCharY(c);

      GL11.glTexCoord2f(cx, cy);
      GL11.glVertex3f(xTmp, y, 0);

      GL11.glTexCoord2f(cx + cw, cy);
      GL11.glVertex3f(xTmp + width, y, 0);

      GL11.glTexCoord2f(cx + cw, cy + ch);
      GL11.glVertex3f(xTmp + width, y + height, 0);

      GL11.glTexCoord2f(cx, cy + ch);
      GL11.glVertex3f(xTmp, y + height, 0);

      xTmp += width;
    }
    GL11.glEnd();
  }

  public ByteBuffer asByteBuffer() {

    ByteBuffer byteBuffer;

    //Draw the characters on our image
    Graphics2D imageGraphics = (Graphics2D) bufferedImage.getGraphics();
    imageGraphics.setFont(font);
    imageGraphics
        .setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    // draw every CHAR by line...
    imageGraphics.setColor(Color.WHITE);
    CHARS.keySet().forEach(i -> imageGraphics
        .drawString(CHARS.get(i), 0, fontMetrics.getMaxAscent() + (getCharHeight() * i)));

    //Generate texture data
    int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
    bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0,
                         bufferedImage.getWidth());
    byteBuffer = ByteBuffer.allocateDirect(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);

    for (int y = 0; y < bufferedImage.getHeight(); y++) {
      for (int x = 0; x < bufferedImage.getWidth(); x++) {
        int pixel = pixels[y * bufferedImage.getWidth() + x];
        byteBuffer.put((byte) ((pixel >> 16) & 0xFF));   // Red component
        byteBuffer.put((byte) ((pixel >> 8) & 0xFF));    // Green component
        byteBuffer.put((byte) (pixel & 0xFF));           // Blue component
        byteBuffer.put((byte) ((pixel >> 24) & 0xFF));   // Alpha component. Only for RGBA
      }
    }

    byteBuffer.flip();

    return byteBuffer;
  }
}