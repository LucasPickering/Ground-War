package groundwar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import groundwar.constants.Constants;
import groundwar.tile.ForwardFortTile;
import groundwar.tile.GoldTile;
import groundwar.tile.MountainTile;
import groundwar.tile.Tile;
import groundwar.unit.Unit;
import groundwar.unit.UnitType;

public class Board {

  private Player currentPlayer = Player.ORANGE;
  private final Map<Point, Tile> tiles = new HashMap<>();

  /**
   * The tile that is currently selected. If {@code selectedTile != null}, then {@code selectedTile
   * .getUnit() != null}.
   */
  private Tile selectedTile;
  private Unit spawningUnit;

  /**
   * A set of all tiles that the currently selected unit ({@code selectedTile.getUnit()}) can move to.
   * Null if any of the following are true: <ul> <li>{@code selectedTile == null}</li> <li>{@code
   * selectedTile.getUnit() == null}</li> </ul>
   */
  private Set<Tile> moveableTiles;

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
      selectedTile = null;
    } else if (selectedTile != null && selectedTile != tile) { // Move the unit
      if (moveSelectedUnit(tile)) {
        selectedTile = null;
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
   * @param tile the tile to select (non-null, {@code tile.getUnit() != null})
   * @throws NullPointerException if {@code tile == null}
   */
  private void selectTile(Tile tile) {
    Objects.requireNonNull(tile);
    Objects.requireNonNull(tile.getUnit());
    selectedTile = tile;
    moveableTiles = getTilesInMoveableRange();
  }

  /**
   * Un-selects the currently-selected tile. Sets {@link #selectedTile} equal to {@code null}.
   */
  private void unselectTile() {
    selectedTile = null;
    moveableTiles = null;
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
   * @param to the tile to be moved to (non-null)
   * @return true if the unit can be moved, false otherwise
   * @throws NullPointerException if {@code from == null} or {@code to == null}
   */
  public boolean canSelectedMoveTo(Tile to) {
    Objects.requireNonNull(selectedTile);
    Objects.requireNonNull(to);
    Unit unit = selectedTile.getUnit();
    return to.isMoveable(unit) && getTilesInMoveableRange().contains(to);
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
    if (canSelectedMoveTo(destination)) {
      selectedTile.getUnit().move(selectedTile.distanceTo(destination));
      destination.setUnit(selectedTile.getUnit());
      selectedTile.setUnit(null);
      return true;
    }
    return false;
  }

  /**
   * Move to the next player's turn.
   */
  public void nextTurn() {
    // Reset movement points for each unit
    tiles.values().stream().filter(tile -> tile.getUnit() != null)
        .forEach(tile -> tile.getUnit().resetMoves());

    currentPlayer = currentPlayer.other(); // Switch players
  }

  /**
   * Gets a {@link Set} of all tiles within moveable range of {@link #selectedTile}. Moveable range
   * means that a unit can move to the tile that is "in range" in <i>at most</i> {@code range} steps.
   * A range of 0 is not permitted, and a tile is never considered to be in range of itself.
   */
  public Set<Tile> getTilesInMoveableRange() {
    return getTilesInMoveableRange(selectedTile, selectedTile.getUnit(),
                                   selectedTile.getUnit().getMovesRemaining());
  }

  /**
   * Gets a {@link Set} of all tiles within moveable range of the given tile, if the given unit were
   * to be doing the movement. Moveable range means that a unit can move to the tile that is "in
   * range" in <i>at most</i> {@code range} steps. A range of 0 is not permitted, and a tile is never
   * considered to be in range of itself.
   *
   * @param tile  the starting tile (non-null)
   * @param unit  the unit to be checked for movement
   * @param range the amount of tiles to spread outwards (positive)
   * @return a {@link Set} of all tiles in range, not including {@code tile}
   */
  private Set<Tile> getTilesInMoveableRange(Tile tile, Unit unit, int range) {
    Set<Tile> adjTiles = new HashSet<>();

    if (range > 0) {
      for (Direction dir : Direction.values()) {
        Tile adjTile = tile.getAdjacentTiles()[dir.ordinal()];
        if (adjTile != null && adjTile.isMoveable(unit)) {
          adjTiles.add(adjTile);
          if (range > 1) {
            adjTiles.addAll(getTilesInMoveableRange(adjTile, unit, range - 1));
          }
        }
      }
    }

    return adjTiles;
  }
}
