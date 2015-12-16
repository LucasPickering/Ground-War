package groundwar;

import java.util.Objects;

/**
 * An immutable 3D point representing the location of a hex-tile within a board. The tiles' locations
 * are defined in three axes: x, y, and z. It must be that {@code getX() + getY() + getZ() == 0},
 * therefore not all three coordinates must be maintained. As such, the class only holds onto two of
 * the three values (x and y), and calculates z in {@link #getZ}.
 *
 * Comparison for these points works as follows: they are compared first by x, then by y.
 */
public class HexPoint implements Comparable<HexPoint> {

  private final int x;
  private final int y;

  /**
   * Constructs a new {@code HexPoint} with the given x and y coordinates. The z coordinate for this
   * tile will be calculated from x and y, because {@code x + y + z == 0}.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public HexPoint(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return -x - y;
  }

  /**
   * Gets the distance between this point and another point in the same grid.
   *
   * @param p2 the other point (non-null)
   * @return the distance between the two points
   */
  public int distanceTo(HexPoint p2) {
    Objects.requireNonNull(p2);
    return (Math.abs(getX() - p2.getX()) + Math.abs(getY() - p2.getY()) +
            Math.abs(getZ() - p2.getZ())) / 2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof HexPoint)) {
      return false;
    }

    HexPoint p2 = (HexPoint) o;
    return x == p2.x && y == p2.y;
  }

  @Override
  public int hashCode() {
    return x * 31 + y;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d, %d)", x, y, getZ());
  }

  @Override
  public int compareTo(HexPoint p2) {
    Objects.requireNonNull(p2);
    final int comp = Integer.compare(x, p2.x);
    return comp != 0 ? comp : Integer.compare(y, p2.y);
  }
}
