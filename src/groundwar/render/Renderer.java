package groundwar.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import groundwar.GroundWar;
import groundwar.util.Constants;

public class Renderer {

  private static final int BYTES_PER_PIXEL = 4; // RGBA

  private final Map<String, Texture> textures = new HashMap<>();
  private final Map<Float, TrueTypeFont> fonts = new HashMap<>();


  /**
   * Loads the texture from the file with the given name and places it into the texture map.
   *
   * @param name the name of the file, which will be formatted into {@link Constants#TEXTURE_PATH} to
   *             create the file path
   */
  public void loadTexture(String name) {
    try {
      BufferedImage image =
          ImageIO.read(GroundWar.getResource(String.format(Constants.TEXTURE_PATH, name)));
      textures.put(name, new Texture(loadTextureFromImage(image)));
    } catch (IOException e) {
      System.err.println("Error loading texture: " + name);
      e.printStackTrace();
    }
  }

  /**
   * Written by Krythic (http://stackoverflow.com/users/3214889/krythic)
   */
  private int loadTextureFromImage(BufferedImage image) {
    int[] pixels = new int[image.getWidth() * image.getHeight()];
    image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
    ByteBuffer buffer =
        BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        int pixel = pixels[y * image.getWidth() + x];
        buffer.put((byte) ((pixel >> 16) & 0xff));  // Red
        buffer.put((byte) ((pixel >> 8) & 0xff));   // Green
        buffer.put((byte) (pixel & 0xff));          // Blue
        buffer.put((byte) ((pixel >> 24) & 0xff));  // Alpha
      }
    }

    buffer.flip(); // FOR THE LOVE OF GOD DO NOT FORGET THIS

    // You now have a ByteBuffer filled with the color data of each pixel.
    // Now just create a texture ID and bind it. Then you can load it using
    // whatever OpenGL method you want, for example:

    int textureID = GL11.glGenTextures(); //G enerate texture ID
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); // Bind texture ID

    // Setup wrap mode
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

    // Setup texture scaling filtering
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

    // Send texel data to OpenGL
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0,
                      GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

    // Return the texture ID so we can bind it later again
    return textureID;
  }

  /**
   * Deletes all loaded textures and fonts.
   */
  public void deleteTexturesAndFonts() {
    textures.values().forEach(Texture::delete);
    fonts.values().forEach(TrueTypeFont::delete);
  }

  public void loadFont(String name, float size) {
    try {
      fonts.put(size, new TrueTypeFont(name, size));
    } catch (IOException | FontFormatException e) {
      System.err.println("Error creating font: " + name);
      e.printStackTrace();
    }
  }

  public boolean hasTexture(String name) {
    return textures.containsKey(name);
  }

  public Texture getTexture(String name) {
    if (!textures.containsKey(name)) {
      throw new IllegalArgumentException("No texture by the name: " + name);
    }
    return textures.get(name);
  }

  public void drawRect(int x, int y, int width, int height, int color) {
    GL11.glColor4f((color >> 16 & 0xff) / 255.0f, (color >> 8 & 0xff) / 255.0f,
                   (color & 0xff) / 255.0f, (color >> 24 & 0xff) / 255.0f);

    // Draw a rectangle
    GL11.glBegin(GL11.GL_QUADS);
    {
      GL11.glVertex2f(x, y);
      GL11.glVertex2f(x + width, y);
      GL11.glVertex2f(x + width, y + height);
      GL11.glVertex2f(x, y + height);
    }
    GL11.glEnd();
  }

  /**
   * Draws the texture with the given name at the given location and size.
   *
   * @param name   the name of the texture to be drawn
   * @param x      the x-location of the top-left of the texture
   * @param y      the y-location of the top-left of the texture
   * @param width  the width of the texture
   * @param height the height of the texture
   * @throws IllegalArgumentException if there is no texture with the given name in the texture map
   * @see #drawTexture(String, int, int, int, int, int)
   */
  public void drawTexture(String name, int x, int y, int width, int height) {
    drawTexture(name, x, y, width, height, 0xffffffff);
  }

  /**
   * Draws the texture with the given name at the given location and size, with the given color.
   *
   * @param name   the name of the texture to be drawn
   * @param x      the x-location of the top-left of the texture
   * @param y      the y-location of the top-left of the texture
   * @param width  the width of the texture
   * @param height the height of the texture
   * @param color  the color of the texture
   */
  public void drawTexture(String name, int x, int y, int width, int height, int color) {
    if (!textures.containsKey(name)) {
      loadTexture(name);
    }
    textures.get(name).draw(x, y, width, height, color);
  }

  /**
   * Draw text in white with left alignment.
   *
   * @see #drawString(float, String, int, int, int, HorizAlignment, VertAlignment)
   */
  public void drawString(float size, String text, int x, int y) {
    drawString(size, text, x, y, 0xffffffff, HorizAlignment.LEFT, VertAlignment.TOP);
  }

  /**
   * Draw text with left alignment.
   *
   * @see #drawString(float, String, int, int, int, HorizAlignment, VertAlignment)
   */
  public void drawString(float size, String text, int x, int y, int color) {
    drawString(size, text, x, y, color, HorizAlignment.LEFT, VertAlignment.TOP);
  }

  /**
   * Draw text in white.
   *
   * @see #drawString(float, String, int, int, int, HorizAlignment, VertAlignment)
   */
  public void drawString(float size, String text, int x, int y, HorizAlignment alignment) {
    drawString(size, text, x, y, 0xffffffff, alignment, VertAlignment.TOP);
  }

  /**
   * Draw the given text in the given size, at the given position. A font for the given size must have
   * been loaded already using {@link #loadFont}.
   *
   * @param size       the size of the font
   * @param text       the text to draw
   * @param x          the x position to draw at
   * @param y          the y position to draw at
   * @param color      the color to draw in
   * @param horizAlign the text alignment (left, center, right)
   * @param vertAlign  the vertical text alignment (top, center, bottom)
   */
  public void drawString(float size, String text, int x, int y, int color,
                         HorizAlignment horizAlign, VertAlignment vertAlign) {
    if (!fonts.containsKey(size)) {
      loadFont(Constants.FONT1, size);
    }
    fonts.get(size).draw(text, x, y, color, horizAlign, vertAlign);
  }
}
