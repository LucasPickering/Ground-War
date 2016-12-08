package groundwar.board;

import groundwar.render.HorizAlignment;
import groundwar.util.Colors;
import groundwar.util.Constants;
import groundwar.util.Point;

public enum PlayerInfo {

    ORANGE("Orange", Colors.ORANGE, Colors.ORANGE2, new Point(10, 500), HorizAlignment.LEFT),
    BLUE("Blue", Colors.BLUE, Colors.BLUE2, new Point(Constants.RES_WIDTH - 10, 500),
         HorizAlignment.RIGHT);

    public final String displayName;
    public final int primaryColor;
    public final int secondaryColor;
    public final Point infoPos;
    public final HorizAlignment textHorizAlign;

    PlayerInfo(String displayName, int primaryColor, int secondaryColor, Point infoPos,
               HorizAlignment textHorizAlign) {
        this.displayName = displayName;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.infoPos = infoPos;
        this.textHorizAlign = textHorizAlign;
    }
}
