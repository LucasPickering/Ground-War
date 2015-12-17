package groundwar.screen;

import java.io.IOException;

public class Texture {

  private final int textureID;

  public Texture(int textureID) throws IOException {
    this.textureID = textureID;
  }

  public int getTextureID() {
    return textureID;
  }
}
