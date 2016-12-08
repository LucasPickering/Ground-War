package groundwar.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import groundwar.board.tile.Tile;

/**
 * Represents a path of tiles. Two paths are considered equal if they share the same origin and
 * destination; the tiles in the middle don't matter for equality. Note that the origin and
 * destination of the two paths are compared using the '==' operator rather than {@link
 * Object#equals}, so two paths must have references to the same origin and destination {@link Tile}
 * objects to be considered equal.
 */
public class Path {

    private final Tile origin;
    private final List<Tile> tiles = new LinkedList<>();

    /**
     * Constructs a new path starting at the given tile.
     *
     * @param origin the tile to start at (non-null)
     * @throws NullPointerException if {@code origin == null}
     */
    public Path(Tile origin) {
        this.origin = origin;
    }

    /**
     * Adds a tile to the end of this path.
     *
     * @param tile the tile to be added (non-null, must be adjacent to {@link #getDestination()})
     * @throws NullPointerException if {@code tile == null}
     */
    public void addTile(Tile tile) {
        Objects.requireNonNull(tile);
        if (!tile.isAdjacentTo(getDestination())) {
            throw new IllegalArgumentException(
                "Each tile in the path must be adjacent to the previous!");
        }
        tiles.add(tile);
    }

    public int getLength() {
        return tiles.size();
    }

    public Tile getOrigin() {
        return origin;
    }

    /**
     * Gets the destination of this path.
     *
     * @return the last tile in {@link #tiles}, or {@link #origin} if {@link #tiles} is empty
     */
    public Tile getDestination() {
        if (!tiles.isEmpty()) {
            return tiles.get(tiles.size() - 1);
        }
        return origin;
    }

    public Tile getSecondToLastTile() {
        if (tiles.isEmpty()) {
            throw new IllegalStateException("There is no second to last tile!");
        }
        if (tiles.size() == 1) {
            return origin;
        }
        return tiles.get(tiles.size() - 2);
    }

    public Path copy() {
        Path copy = new Path(origin);
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
        return getOrigin() == path.getOrigin() && getDestination() == path.getDestination();
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
