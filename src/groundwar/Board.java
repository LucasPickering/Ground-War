package groundwar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import groundwar.tile.ForwardFortTile;
import groundwar.tile.GoldTile;
import groundwar.tile.MountainTile;
import groundwar.tile.Tile;
import groundwar.unit.Unit;
import groundwar.unit.UnitType;
import groundwar.util.Constants;
import groundwar.util.Direction;
import groundwar.util.Path;
import groundwar.util.Point;

public class Board {

  private Player currentPlayer = Player.ORANGE;
  private final Map<Point, Tile> tiles = new HashMap<>();

  /**
   * The tile that is currently selected. If {@code selectedTile != null}, then {@code selectedTile
   * .hasUnit()}.
   */
  private Tile selectedTile;
  private Unit spawningUnit;

  /**
   * A set of all paths that the currently selected unit ({@code selectedTile.getUnit()}) can move
   * through. All paths must have their origin be {@link #selectedTile}, and must be moveable, meaning
   * that each tile in the path is moveable for the currently-selected unit.
   *
   * Empty if {@code selectedTile == null}.
   */
  private final Set<Path> moveablePaths = new HashSet<>();

  /**
   * A set of all paths that the currently-selected unit ({@code selectedTile.getUnit()}) can attack
   * via. All paths must have their origin be {@link #selectedTile}, and must be attackable, meaning
   * that each tile in the path is moveable for the currently-selected unit, except for the last,
   * which is attackable.
   *
   * Empty if {@code selectedTile == null}.
   */
  private final Set<Path> attackablePaths = new HashSet<>();

  public Board() {
    try {
      loadTilesFromFile(Constants.BOARD_FILE);
    } catch (IOException e) {
      System.err.printf("Error loading board \"%s\"\n", Constants.BOARD_FILE);
      e.printStackTrace();
    }

    // For each tile, tell it which tiles are adjacent to it
    tiles.values().forEach(tile -> tile.setAdjacentTiles(getAdjacentTiles(tile)));
  }

  private void loadTilesFromFile(String fileName) throws IOException {
    BufferedReader reader = null;
    String line;
    try {
      reader = new BufferedReader(new FileReader(getClass().getResource(fileName).getFile()));
      while ((line = reader.readLine()) != null) { // Read each line from the file
        line = line.replaceAll(" ", ""); // Strip spaces out
        if (line.length() > 0 && line.charAt(0) != '#') { // If the line isn't blank or commented-out
          try {
            putTile(getTileForData(line.split(","))); // Split by commas, then convert to tile data
          } catch (IllegalArgumentException e) {
            System.err.printf("Error reading line \"%s\" from board \"%s\"\n", line, fileName);
          }
        }
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

  private Tile getTileForData(String[] data) throws IllegalArgumentException {
    if (data.length >= 3) {
      // First value is x, second is y. Parse the Strings to ints.
      final Point p = new Point(new Integer(data[0]), new Integer(data[1]));

      // Switch based on the type of the tile
      switch (data[2]) {
        case "T":
          return new Tile(p);
        case "M":
          return new MountainTile(p);
        case "R":
          return new Tile(p, Player.ORANGE);
        case "B":
          return new Tile(p, Player.BLUE);
        case "G":
          return new GoldTile(p);
        case "F":
          return new ForwardFortTile(p);
      }
    }
    throw new IllegalArgumentException("Not enough data to create a tile");
  }

  private void putTile(Tile tile) {
    tiles.put(tile.getPos(), tile);
  }

  /**
   * Gets an array of tiles adjcaent to the given tile.
   *
   * @param tile the given tile
   * @return all tiles adjacent to {@param tile}
   */
  private Tile[] getAdjacentTiles(Tile tile) {
    final Point p = tile.getPos();
    final Tile[] adjTiles = new Tile[Constants.NUM_SIDES];
    for (Direction dir : Direction.values()) {
      Point adjPoint = p.plus(dir.delta);
      if (tiles.containsKey(adjPoint)) {
        adjTiles[dir.ordinal()] = tiles.get(adjPoint);
      }
    }
    return adjTiles;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public Map<Point, Tile> getTiles() {
    return tiles;
  }

  public Tile getSelectedTile() {
    return selectedTile;
  }

  public Unit getSpawningUnit() {
    return spawningUnit;
  }

  /**
   * Called when a specific tile is clicked.
   *
   * @param tile the tile clicked (non-null)
   * @throws NullPointerException if {@code tile == null}
   */
  public void onTileClicked(Tile tile) {
    Objects.requireNonNull(tile);
    if (selectedTile == null && spawningUnit != null) { // Spawn the unit
      spawnUnit(tile);
    } else if (selectedTile != null && selectedTile != tile) { // Move the unit
      if (moveSelectedUnit(tile)) {
        unselectTile();
      }
    } else if (selectedTile != tile && tile.isSelectable(currentPlayer)) { // Select the tile
      selectTile(tile);
    } else {
      unselectTile();
    }
  }

  /**
   * Selects the given tile. Sets {@link #selectedTile} equal to {@code tile}.
   *
   * @param tile the tile to select (non-null, {@code tile.hasUnit()})
   * @throws NullPointerException if {@code tile == null}
   */
  private void selectTile(Tile tile) {
    Objects.requireNonNull(tile);
    Objects.requireNonNull(tile.getUnit());
    selectedTile = tile;
    populatePaths();
  }

  /**
   * Un-selects the currently-selected tile. Sets {@link #selectedTile} equal to {@code null}.
   */
  private void unselectTile() {
    selectedTile = null;
    moveablePaths.clear();
    attackablePaths.clear();
  }

  /**
   * Prepare to spawn a unit of the given type, if allowed.
   *
   * @param unitType the type of unit to spawn
   */
  public void prepareToSpawn(UnitType unitType) {
    if (unitType == null) {
      spawningUnit = null;
    } else if (unitType.cost <= currentPlayer.getMoney()) {
      selectedTile = null;
      spawningUnit = unitType.createUnit(currentPlayer);
    }
  }

  /**
   * Attempt to spawn {@link #spawningUnit} on the given tile
   *
   * @param tile the tile to be spawned on
   */
  private void spawnUnit(Tile tile) {
    if (tile.isSpawnable(spawningUnit)) {
      tile.setUnit(spawningUnit);
      spawningUnit = null;
    }
  }

  /**
   * Can the unit on {@link #selectedTile} be moved to {@code to}?
   *
   * @param destination the tile to be moved to (non-null)
   * @return true if the unit can be moved, false otherwise
   * @throws NullPointerException if {@code selectedTile == null} or {@code destination == null}
   */
  public boolean canSelectedMoveTo(Tile destination) {
    Objects.requireNonNull(selectedTile);
    Objects.requireNonNull(destination);
    return getPathToTile(destination) != null;
  }

  /**
   * Finds a path in {@link #moveablePaths} that leads to the given tile.
   *
   * @param destination the destination tile
   * @return the path leading to {@code destination}, or {@code null} if none exists
   */
  public Path getPathToTile(Tile destination) {
    for (Path path : moveablePaths) {
      if (path.getDestination().equals(destination)) {
        return path;
      }
    }
    return null;
  }

  /**
   * Moves the unit on {@link #selectedTile} to {@code destination}.
   *
   * @param destination the tile to be moved to, if valid (non-null)
   * @return true if the unit was moved, false otherwise
   * @throws NullPointerException if {@code destination == null}
   */
  private boolean moveSelectedUnit(Tile destination) {
    Objects.requireNonNull(destination);
    Path path = getPathToTile(destination);
    if (path != null) {
      selectedTile.getUnit().useMoves(path.getLength());
      destination.setUnit(selectedTile.getUnit());
      selectedTile.setUnit(null);
      return true;
    }
    return false;
  }

  /**
   * Can the unit on {@link #selectedTile} attack the unit on {@code to}? Units cna only attack
   * adjacent tiles.
   *
   * @param destination the tile to be attacked
   * @return true if the unit on can attack, false otherwise
   * @throws NullPointerException if {@code selectedTile == null} or {@code destination == null}
   */
  public boolean canSelectedAttack(Tile destination) {
    Objects.requireNonNull(selectedTile);
    Objects.requireNonNull(destination);
    return selectedTile.isAdjacentTo(destination) && destination.isAttackable(selectedTile.getUnit());
  }

  /**
   * Move to the next player's turn.
   */
  public void nextTurn() {
    // Reset movement points for each unit
    tiles.values().stream().filter(Tile::hasUnit).forEach(tile -> tile.getUnit().resetMoves());
    currentPlayer = currentPlayer.other(); // Switch players
  }

  /**
   * Gets a {@link Set} of all paths within moveable range of {@link #selectedTile}. A path is
   * moveable if each tile in it is moveable, and its length is less than or equal to the selected
   * unit's remaining moves.
   */
  private void populatePaths() {
    populatePaths(new Path(selectedTile), selectedTile.getUnit(),
                  selectedTile.getUnit().getMovesRemaining());
  }

  /**
   * Gets a {@link Set} of all moveable paths that are at mose {@code range} steps longer than {@code
   * path}. A path is moveable if each tile in it is moveable for {@code unit}.
   *
   * @param path  the path to add onto (non-null, terminated)
   * @param unit  the unit to be checked for movement
   * @param range the amount of tiles to spread outwards (positive)
   */
  private void populatePaths(Path path, Unit unit, int range) {
    if (range > 0) {
      for (Direction dir : Direction.values()) {
        Tile nextTile = path.getDestination().getAdjacentTile(dir);
        if (nextTile != null) {
          // Create a path to the adjacent tile
          Path newPath = path.copy();
          newPath.addTile(nextTile);

          // If it's moveable, add it to moveablePaths and search deeper
          // If it's attackable, add it to attackPaths, but don't search any deeper
          if (nextTile.isMoveable(unit)) {
            moveablePaths.add(newPath);
            populatePaths(newPath, unit, range - 1);
          } else if (nextTile.isAttackable(unit)) {
            attackablePaths.add(newPath);
          }
        }
      }
    }
  }
}
