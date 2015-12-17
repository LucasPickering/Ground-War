package groundwar.screen;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import groundwar.Constants;
import groundwar.GroundWar;

public class TextureHandler {

  private static final int BYTES_PER_PIXEL = 4; // RGBA
  private static boolean drawingTextures;
  private static Map<String, Texture> textureMap = new HashMap<>();

  /**
   * Loads the texture from the file with the given name and places it into the texture map.
   *
   * @param name the name of the file, which will be formatted into {@link Constants#TEXTURE_PATH} to
   *             create the file path
   */
  public static void loadTexture(String name) {
    try {
      BufferedImage image =
          ImageIO.read(GroundWar.class.getResource(String.format(Constants.TEXTURE_PATH, name)));
      textureMap.put(name, new Texture(loadTextureFromImage(image)));
    } catch (IOException e) {
      System.err.println("Error loading texture: " + name);
      e.printStackTrace();
    }
  }

  /**
   * Written by Krythic (http://stackoverflow.com/users/3214889/krythic)
   */
  private static int loadTextureFromImage(BufferedImage image) {
    int[] pixels = new int[image.getWidth() * image.getHeight()];
    image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
    ByteBuffer buffer =
        BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        int pixel = pixels[y * image.getWidth() + x];
        buffer.put((byte) ((pixel >> 16) & 0xff)); // Red
        buffer.put((byte) ((pixel >> 8) & 0xff)); // Green
        buffer.put((byte) (pixel & 0xff)); // Blue
        buffer.put((byte) ((pixel >> 24) & 0xff)); // Alpha
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
   * Enables texture-drawing for all textures. This can optionally be used before drawing a lot of
   * textures to save time on the setup and cleanup.
   */
  public static void startDrawingTextures() {
    if (drawingTextures) {
      throw new IllegalStateException("Textures are already being drawn!");
    }
    textureSetup();
    drawingTextures = true;
  }

  /**
   * Disables texture-drawing for all textures. This MUST be used after drawing textures IF {@link
   * #startDrawingTextures} was used.
   */
  public static void stopDrawingTextures() {
    if (!drawingTextures) {
      throw new IllegalStateException("Textures aren't being drawn!");
    }
    textureTearDown();
    drawingTextures = false;
  }

  private static void textureSetup() {
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
  }

  private static void textureTearDown() {
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);
  }

  /**
   * Draws the texture with the given name at the given location and size. If texture-drawing has been
   * started with {@link #startDrawingTextures}, this skips the setup and the cleanup before/after the
   * texture drawing.
   *
   * @param name   the name of the texture to be drawn
   * @param x      the x-location of the top-left of the texture
   * @param y      the y-location of the top-left of the texture
   * @param width  the width of the texture
   * @param height the height of the texture
   * @throws IllegalArgumentException if there is no texture with the given name in the texture map
   * @see #draw(String, int, int, int, int, int)
   */
  public static void draw(String name, int x, int y, int width, int height) {
    draw(name, x, y, width, height, 0xffffff);
  }

  /**
   * Draws the texture with the given name at the given location and size, with the given color. If
   * texture-drawing has been started with {@link #startDrawingTextures}, this skips the setup and the
   * cleanup before/after the texture drawing.
   *
   * @param name   the name of the texture to be drawn
   * @param x      the x-location of the top-left of the texture
   * @param y      the y-location of the top-left of the texture
   * @param width  the width of the texture
   * @param height the height of the texture
   * @param color  the color of the texture
   * @throws IllegalArgumentException if there is no texture with the given name in the texture map
   */
  public static void draw(String name, int x, int y, int width, int height, int color) {
    // Load the texture from the texture map
    Texture texture = textureMap.get(name);
    if (texture == null) {
      throw new IllegalArgumentException("No texture by the name \"" + name + "\"!");
    }

    if (!drawingTextures) {
      textureSetup();
    }

    // Set the color (aren't bitshifts cool?)
    GL11.glColor4f(((color >> 16) & 0xff) / 255.0f,
                   ((color >> 8) & 0xff) / 255.0f,
                   (color & 0xff) / 255.0f,
                   ((color >> 24) & 0xff) / 255.0f);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID()); // Bind the texture

    // Draw a rectangle
    GL11.glBegin(GL11.GL_QUADS);
    {
      GL11.glTexCoord2f(0, 0);
      GL11.glVertex2f(x, y);

      GL11.glTexCoord2f(1, 0);
      GL11.glVertex2f(x + width, y);

      GL11.glTexCoord2f(1, 1);
      GL11.glVertex2f(x + width, y + height);

      GL11.glTexCoord2f(0, 1);
      GL11.glVertex2f(x, y + height);
    }
    GL11.glEnd();

    if (!drawingTextures) {
      textureTearDown();
    }
  }
}
