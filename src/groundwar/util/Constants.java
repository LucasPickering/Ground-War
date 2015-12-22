package groundwar.util;

import groundwar.util.Direction;
import groundwar.util.Point;

public class Constants {

  // Game constants
  public static final int START_MONEY = 10;

  // Tile constants
  /**
   * The distance between the center point of the hexagon and the center-point of one side of the
   * hexagon.
   */
  public static final int TILE_RADIUS = 54;
  public static final int TILE_WIDTH = (int) (TILE_RADIUS * 4 / Math.sqrt(3));
  public static final int TILE_HEIGHT = TILE_RADIUS * 2;
  public static int NUM_SIDES = Direction.values().length;

  // Renderer constants
  /**
   * The width of the window that will be assumed when all textures, words, etc. are drawn to the
   * screen. Everything will be rendered to this resolution, then scaled to the actual resolution.
   */
  public static final int NATIVE_WINDOW_WIDTH = 1920;
  /**
   * @see {@link #NATIVE_WINDOW_WIDTH}
   */
  public static final int NATIVE_WINDOW_HEIGHT = 1080;

  public static final String TEXTURE_PATH = "/textures/%s.png";
  // Board constants
  public static final Point BOARD_CENTER = new Point((NATIVE_WINDOW_WIDTH - TILE_WIDTH) / 2,
                                                     (NATIVE_WINDOW_HEIGHT - TILE_HEIGHT) / 2);

  public static final String BOARD_FILE = "/boards/board.csv";
  // Texture names
  public static final String TILE_BG_NAME = "tile_background";

  public static final String TILE_OUTLINE_NAME = "tile_outline";
}
