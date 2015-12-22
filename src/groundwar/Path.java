package groundwar;

import java.util.LinkedList;
import java.util.List;

import groundwar.tile.Tile;

public class Path {

  private final Tile origin;
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
}
