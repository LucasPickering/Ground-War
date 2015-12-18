package groundwar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import groundwar.constants.Constants;
import groundwar.tile.FortTile;
import groundwar.tile.ForwardFortTile;
import groundwar.tile.GoldTile;
import groundwar.tile.MountainTile;
import groundwar.tile.Tile;
import groundwar.unit.Unit;
import groundwar.unit.UnitType;

public class Board {

  private Player currentPlayer = Player.ORANGE;
  private final Map<HexPoint, Tile> tiles = new HashMap<>();
  private Tile selectedTile;
  private Unit spawningUnit;

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
      final HexPoint p = new HexPoint(new Integer(data[0]), new Integer(data[1]));

      // Switch based on the type of the tile
      switch (data[2]) {
        case "T":
          return new Tile(p);
        case "M":
          return new MountainTile(p);
        case "R":
          return new FortTile(p, Player.ORANGE);
        case "B":
          return new FortTile(p, Player.BLUE);
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
    final HexPoint p = tile.getPos();
    final Tile[] adjTiles = new Tile[Constants.NUM_SIDES];
    for (Direction dir : Direction.values()) {
      HexPoint adjPoint = p.plus(dir.delta);
      if (tiles.containsKey(adjPoint)) {
        adjTiles[dir.ordinal()] = tiles.get(adjPoint);
      }
    }
    return adjTiles;
  }

  public Map<HexPoint, Tile> getTiles() {
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
   * @param tile the tile clicked
   */
  public void onTileClicked(Tile tile) {
    if (selectedTile == null && spawningUnit != null) { // Spawn the unit
      spawnUnit(tile);
      selectedTile = null;
    } else if (selectedTile != null && selectedTile != tile) { // Move the unit
      moveSelectedUnit(tile);
      selectedTile = null;
    } else if (selectedTile != tile && tile.isSelectable(currentPlayer)) {
      selectedTile = tile;
    } else {
      selectedTile = null;
    }
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
   * Can the unit on {@code from} be moved to {@code to}?
   *
   * @param from the tile to be moved from (non-null)
   * @param to   the tile to be moved to (non-null)
   * @return true if the unit can be moved, false otherwise
   */
  public boolean canMoveTo(Tile from, Tile to) {
    Objects.requireNonNull(from);
    Objects.requireNonNull(to);
    Unit unit = from.getUnit();
    int distance = from.distanceTo(to);
    return unit != null && to.openForMovement(unit) && from.distanceTo(to) <= unit
        .getMovementPoints();
  }

  /**
   * Moves the unit on {@link #selectedTile} to {@code destination}.
   *
   * @param destination the tile to be moved to, if valid (non-null)
   */
  private void moveSelectedUnit(Tile destination) {
    Objects.requireNonNull(destination);
    if (canMoveTo(selectedTile, destination)) {
      selectedTile.getUnit().move(selectedTile.distanceTo(destination));
      destination.setUnit(selectedTile.getUnit());
      selectedTile.setUnit(null);
    }
  }

  /**
   * Move to the next player's turn.
   */
  public void nextTurn() {
    // Reset movement points for each unit
    tiles.values().stream().filter(tile -> tile.getUnit() != null)
        .forEach(tile -> tile.getUnit().resetMovementPoints());

    currentPlayer = currentPlayer.other(); // Switch players
  }
}
