package groundwar.board.tile;

import groundwar.board.Player;
import groundwar.util.Point;

public class FortTile extends Tile {

    public FortTile(Point pos, Player owner) {
        super(pos, owner);
    }

    @Override
    public boolean shouldGameEnd() {
        return hasUnit() && getUnit().hasFlag() && getUnit().getFlag().getOwner() != getOwner();
    }
}
