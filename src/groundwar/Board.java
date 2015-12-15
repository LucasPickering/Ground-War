package groundwar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import groundwar.tile.FortTile;
import groundwar.tile.ForwardFortTile;
import groundwar.tile.GoldTile;
import groundwar.tile.MountainTile;
import groundwar.tile.Tile;

public class Board {

  private final Map<HexPoint, Tile> tiles = new HashMap<>();

  public Board() {
    final String boardName = "/boards/board.csv";
    try {
      loadTiles(boardName);
    } catch (IOException e) {
      System.err.printf("Error loading board \"%s\"\n", boardName);
      e.printStackTrace();
    }
  }

  private void loadTiles(String fileName) throws IOException {
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

  public Map<HexPoint, Tile> getTiles() {
    return tiles;
  }
}
