package groundwar.render.screen;

import org.lwjgl.opengl.GL11;

import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Point;

public class HelpMenu extends MainScreen {

    private final PauseMenu pauseMenu;

    public HelpMenu(PauseMenu pauseMenu) {
        this.pauseMenu = pauseMenu;
    }

    @Override
    public void draw(Point mousePos) {
        GL11.glEnable(GL11.GL_BLEND);
        renderer().drawRect(0, 0, Constants.RES_WIDTH, Constants.RES_HEIGHT, Colors.MENU_SHADER);
        GL11.glDisable(GL11.GL_BLEND);

        super.draw(mousePos);
    }

    private void returnToPauseMenu() {
        pauseMenu.setNextScreen(pauseMenu);
        setNextScreen(pauseMenu);
    }
}
