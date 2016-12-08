package groundwar.util;

import java.util.Objects;

/**
 * A class representing an immutable 2-dimensional integer point. Used mostly for points on-screen.
 *
 * Points are compared first by x, then by y.
 */
public class Point implements Comparable<Point> {

    private final int x;
    private final int y;

    /**
     * Constructs a new {@code Point} with an x and y of 0.
     */
    public Point() {
        this(0, 0);
    }

    /**
     * Constructs a new {@code Point} with the given x and y.
     *
     * @param x the x-value
     * @param y the y-value
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new {@code Point} with the same x and y values as the given {@code Point}.
     *
     * @param p the {@code Point} to be copied (non-null)
     * @throws NullPointerException if {@code p == null}
     */
    public Point(Point p) {
        Objects.requireNonNull(p);
        this.x = p.x;
        this.y = p.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Creates a new point whose coordinates are the sum of this point's and the given point's. In
     * other words, this creates a {@code new Point(this.x + p.x, this.y + p.y)}.
     *
     * @param p the point to be added with this one
     * @return the new {@code Point}
     */
    public Point plus(Point p) {
        return plus(p.getX(), p.getY());
    }

    /**
     * Creates a new point whose coordinates are the sum of this point's and x and y. In other
     * words, this creates a {@code new Point(this.x + x, this.y + y)}.
     *
     * @param x the x to be added
     * @param y the y to be added
     * @return the new {@code Point}
     */
    public Point plus(int x, int y) {
        return new Point(this.x + x, this.y + y);
    }

    /**
     * Creates a new point whose coordinates are the difference of this point's and the given
     * point's. In other words, this creates a {@code new Point(this.x - p.x, this.y - p.y)}.
     *
     * @param p the point to be added with this one
     * @return the new {@code Point}
     */
    public Point minus(Point p) {
        return plus(-p.getX(), -p.getY());
    }

    /**
     * Gets the Euclidean distance between this point and another point.
     *
     * @param p the other point (non-null)
     * @return the Euclidean distance between the two points
     * @throws NullPointerException if {@code p == null}
     */
    public double distanceTo(Point p) {
        Objects.requireNonNull(p);
        final int xDiff = x - p.x;
        final int yDiff = y - p.y;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    /**
     * Creates a copy of this point.
     *
     * @return a copy of this point, with the same x and y values
     */
    public Point copy() {
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Point)) {
            return false;
        }

        Point point = (Point) o;

        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public int compareTo(Point p) {
        final int xcomp = Integer.compare(x, p.x);
        return xcomp != 0 ? xcomp : Integer.compare(y, p.y);
    }
}
