package groundwar.screen;

import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class Texture {

  private final int textureID;

  public Texture(int textureID) throws IOException {
    this.textureID = textureID;
  }

  public int getTextureID() {
    return textureID;
  }

  public void delete() {
    GL11.glDeleteTextures(textureID);
  }
}
