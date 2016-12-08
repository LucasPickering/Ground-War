package groundwar.board.tile;

import groundwar.util.Colors;
import groundwar.util.Point;

public class GoldTile extends Tile {

    public GoldTile(Point pos) {
        super(pos, Colors.GOLD_BG, Colors.GOLD_OUTLINE);
    }

    @Override
    public void onEndTurn() {
        if (hasUnit()) {
            getUnit().getOwner().incrGold(1);
        }
    }
}
