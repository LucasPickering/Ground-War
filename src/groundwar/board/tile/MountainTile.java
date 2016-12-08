package groundwar.board.tile;

import groundwar.board.unit.Unit;
import groundwar.util.Colors;
import groundwar.util.Point;

public class MountainTile extends Tile {

    public MountainTile(Point pos) {
        super(pos, Colors.MOUNTAIN_BG, Colors.TILE_OUTLINE);
    }

    @Override
    public boolean isMoveable(Unit mover) {
        return false;
    }
}
