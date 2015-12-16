package groundwar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import groundwar.tile.FortTile;
import groundwar.tile.ForwardFortTile;
import groundwar.tile.GoldTile;
import groundwar.tile.MountainTile;
import groundwar.tile.Tile;

public class Board {

  private Player currentPlayer = Player.RED;
  private final Map<HexPoint, Tile> tiles = new HashMap<>();
  private Tile selectedTile;

  public Board() {
    try {
      loadTilesFromFile(Constants.BOARD_FILE);
    } catch (IOException e) {
      System.err.printf("Error loading board \"%s\"\n", Constants.BOARD_FILE);
      e.printStackTrace();
    }

    // For each tile, tell it which tiles are adjacent to it. Yes, this is O(n^2) time.
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
          return new FortTile(p, Player.RED);
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
   * Gets a list of tiles adjcaent to the given tile.
   *
   * @param tile the given tile
   * @return all tiles adjacent to {@param tile}
   */
  private List<Tile> getAdjacentTiles(Tile tile) {
    final HexPoint p = tile.getPos();
    final List<Tile> adjTiles = new LinkedList<>();
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        HexPoint adjPoint = new HexPoint(p.getX() + x, p.getY() + y);
        if (tiles.containsKey(adjPoint) && tile.isAdjacentTo(adjPoint)) {
          adjTiles.add(tiles.get(adjPoint));
        }
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

  /**
   * Called when a specific tile is clicked. Selecte tile is changed appropriately.
   *
   * @param tile the tile clicked
   */
  public void onTileClicked(Tile tile) {
    if (selectedTile != tile && tile.isSelectable(currentPlayer)) {
      selectedTile = tile;
    } else {
      selectedTile = null;
    }
  }
}
