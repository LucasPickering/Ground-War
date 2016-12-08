package groundwar.util;

public enum Direction {

    NORTH(0, 1), NORTHEAST(1, 0), SOUTHEAST(1, -1), SOUTH(0, -1), SOUTHWEST(-1, 0), NORTHWEST(-1,
                                                                                              1);

    public final Point delta;

    Direction(int x, int y) {
        delta = new Point(x, y);
    }
}
