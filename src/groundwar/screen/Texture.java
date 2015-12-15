package groundwar.screen;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import groundwar.GroundWar;

public class Texture {

  private static final int BYTES_PER_PIXEL = 4; // RGBA

  public static Texture loadTexture(String file) {
    try {
      BufferedImage image = ImageIO.read(GroundWar.class.getResource(file));
      return new Texture(loadTexture(image));
    } catch (IOException e) {
      System.err.println("Error loading texture: " + file);
      e.printStackTrace();
      return null;
    }
  }

  private static int loadTexture(BufferedImage image) {
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

  private final int textureID;

  private Texture(int textureID) throws IOException {
    this.textureID = textureID;
  }

  public int getTextureID() {
    return textureID;
  }
}
