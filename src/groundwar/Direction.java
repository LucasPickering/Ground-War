package groundwar;

public enum Direction {

  NORTH(0, 1), NORTHEAST(1, 0), SOUTHEAST(1, -1), SOUTH(0, -1), SOUTHWEST(-1, 0), NORTHWEST(-1, 1);

  public final HexPoint delta;

  Direction(int x, int y) {
    delta = new HexPoint(x, y);
  }
}
