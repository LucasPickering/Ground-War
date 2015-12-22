package groundwar;

import java.util.LinkedList;
import java.util.List;

import groundwar.tile.Tile;

public class Path {

  /**
   * Non-null
   */
  private final Tile origin;

  /**
   * Can't be modified after destination is calculated.
   */
  private final List<Direction> directions = new LinkedList<>();
  private Point destination;

  public Path(Tile origin) {
    this.origin = origin;
  }

  public void addDirection(Direction dir) {
    if (destination != null) {
      throw new IllegalStateException("The path has already been finalized!");
    }
    directions.add(dir);
  }

  public Point getDestination() {
    if (destination == null) {
      destination = findDestination();
    }
    return destination;
  }

  private Point findDestination() {
    Point pos = origin.getPos();
    for (Direction dir : directions) {
      pos.shift(dir.delta);
    }
    return pos;
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
