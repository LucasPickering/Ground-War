package groundwar.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import groundwar.tile.Tile;

/**
 * Represents a path of tiles.
 */
public class Path {

  /**
   * Can't be modified after termination.
   */
  private final List<Tile> tiles = new LinkedList<>();

  /**
   * Constructs a new path starting at the given tile.
   *
   * @param origin the tile to start at (non-null)
   * @throws NullPointerException if {@code origin == null}
   */
  public Path(Tile origin) {
    addTile(origin);
  }

  private Path() {
  }

  /**
   * Adds a tile to the end of this path.
   *
   * @param tile the tile to be added (non-null)
   * @throws NullPointerException if {@code tile == null}
   */
  public void addTile(Tile tile) {
    Objects.requireNonNull(tile);
    tiles.add(tile);
  }

  public int getLength() {
    return tiles.size();
  }

  public Tile getOrigin() {
    return tiles.get(0);
  }

  public Tile getDestination() {
    return tiles.get(tiles.size() - 1);
  }

  public Path copy() {
    Path copy = new Path();
    tiles.forEach(copy::addTile);
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

    // Two paths are equal if they have the same origin and destination. Yay for O(1)!
    Path path = (Path) o;
    return getOrigin().equals(path.getOrigin()) && getDestination().equals(path.getDestination());
  }

  @Override
  public int hashCode() {
    return getOrigin().hashCode() * 31 + getDestination().hashCode();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    Iterator<Tile> iter = tiles.iterator();
    builder.append(iter.next());
    while (iter.hasNext()) {
      builder.append(" -> ").append(iter.next());
    }
    return builder.toString();
  }
}
