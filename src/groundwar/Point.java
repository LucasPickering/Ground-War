package groundwar;

import java.util.Objects;

/**
 * A class representing immutable 2-dimensional integer points. Used mostly for points on-screen.
 */
public class Point {

  public final int x;
  public final int y;

  /**
   * Constructs a new {@code Point} with the given x and y.
   * @param x the x-value
   * @param y the y-value
   */
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs a new {@code Point} with the same x and y values as the given {@code Point}.
   * @param p the {@code Point} to be copied (non-null)
   */
  public Point(Point p) {
    Objects.requireNonNull(p);
    this.x = p.x;
    this.y = p.y;
  }

  /**
   * Gets the Euclidean distance between this point and another point.
   * @param p the other point (non-null)
   * @return the Euclidean distance between the two points
   */
  public double distanceTo(Point p) {
    Objects.requireNonNull(p);
    final int xDiff = x - p.x;
    final int yDiff = y - p.y;
    return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
  }
}
