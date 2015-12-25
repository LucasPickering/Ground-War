package groundwar.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import groundwar.GroundWar;
import groundwar.board.Board;
import groundwar.board.Flag;
import groundwar.board.PlayerColor;
import groundwar.board.tile.ForwardFortTile;
import groundwar.board.tile.GoldTile;
import groundwar.board.tile.MountainTile;
import groundwar.board.tile.Tile;

public class BoardLoader {

  private final Map<Point, Tile> tiles = new HashMap<>();
  private Board board;

  public BoardLoader(Board board) {
    this.board = board;
  }

  public Map<Point, Tile> loadBoard() throws IOException {
    // Load tiles from the file
    loadTilesFromFile(Constants.BOARD_FILE);

    // For each tile, tell it which tiles are adjacent to it
    tiles.values().forEach(tile -> tile.setAdjacentTiles(getAdjacentTiles(tile)));

    return tiles;
  }

  private void loadTilesFromFile(String fileName) throws IOException {
    BufferedReader reader = null;
    String line;
    try {
      reader = new BufferedReader(new FileReader(GroundWar.class.getResource(fileName).getFile()));
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
      Tile toReturn;
      final String tileData = data[2];
      final char tileType = tileData.charAt(0);
      switch (tileType) {
        case 'T':
          toReturn = new Tile(p);
          break;
        case 'M':
          toReturn = new MountainTile(p);
          break;
        case 'O':
          toReturn = new Tile(p, board.getPlayer(PlayerColor.ORANGE));
          break;
        case 'B':
          toReturn = new Tile(p, board.getPlayer(PlayerColor.BLUE));
          break;
        case 'G':
          toReturn = new GoldTile(p);
          break;
        case 'F':
          toReturn = new ForwardFortTile(p);
          break;
        default:
          throw new IllegalArgumentException("No tile of type: " + tileType);
      }

      // Read other tile data, such as flags
      for (char c : tileData.substring(1).toCharArray()) {
        switch (c) {
          case 'o':
            toReturn.setFlag(new Flag(board.getPlayer(PlayerColor.ORANGE)));
            break;
          case 'b':
            toReturn.setFlag(new Flag(board.getPlayer(PlayerColor.BLUE)));
            break;
        }
      }

      return toReturn;
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

}
