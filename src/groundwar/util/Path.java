package groundwar.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a path of tiles starting at some point and following some list of directions. This can
 * be in one of two states: editable or termianted. A path is editable if {@link #terminate} has not
 * yet been called, meaning its destination has not been calculated yet. Once {@link #terminate} is
 * called, {@link #destination} is given a value. At that point, the path is termintad and no more
 * directions can be added.
 */
public class Path {

  /**
   * Non-null
   */
  private final Point origin;

  /**
   * Can't be modified after termination.
   */
  private final List<Direction> directions = new LinkedList<>();
  private Point destination;

  public Path(Point origin) {
    this.origin = origin;
  }

  public void addDirection(Direction dir) {
    if (destination != null) {
      throw new IllegalStateException("The path has already been terminated!");
    }
    directions.add(dir);
  }

  public int getLength() {
    return directions.size();
  }

  public Point getDestination() {
    if (destination == null) {
      throw new IllegalStateException("This path has not yet been terminated!");
    }
    return destination;
  }

  public void terminate() {
    destination = origin;
    for (Direction dir : directions) {
      destination = destination.plus(dir.delta);
    }
  }

  /**
   * Creates an <i>editable</i> copy of this path.
   */
  public Path copy() {
    Path copy = new Path(origin);
    directions.forEach(copy::addDirection);
    return copy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof Path)) {
      return false;
    }

    Path path = (Path) o;
    return origin.equals(path.origin) && !(destination != null ? !destination.equals(path.destination)
                                                               : path.destination != null);
  }

  @Override
  public int hashCode() {
    return origin.hashCode() * 31 + (destination != null ? destination.hashCode() : 0);
  }
}
