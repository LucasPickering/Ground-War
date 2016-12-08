package groundwar.render.event;

import groundwar.util.Point;

public class MouseButtonEvent extends Event {

    public final int button;
    public final int mods;
    public final Point mousePos;

    public MouseButtonEvent(long window, int button, int mods, Point mousePos) {
        super(window);
        this.button = button;
        this.mods = mods;
        this.mousePos = mousePos;
    }

}
