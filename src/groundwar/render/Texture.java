package groundwar.render;

import org.lwjgl.opengl.GL11;

public class Texture {

    private final int textureID;

    public Texture(int textureID) {
        this.textureID = textureID;
    }

    /**
     * Draws this texture at the given location and size, with the given color.
     *
     * @param x      the x-location of the top-left of the texture
     * @param y      the y-location of the top-left of the texture
     * @param width  the width of the texture
     * @param height the height of the texture
     * @param color  the color of the texture
     */
    public void draw(int x, int y, int width, int height, int color) {
        // Set the color (aren't bitshifts cool?)
        GL11.glColor4f((color >> 16 & 0xff) / 255.0f, (color >> 8 & 0xff) / 255.0f,
                       (color & 0xff) / 255.0f, (color >> 24 & 0xff) / 255.0f);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); // Bind the texture

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
    }

    public void delete() {
        GL11.glDeleteTextures(textureID);
    }
}
