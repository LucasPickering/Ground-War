package groundwar.util;

public class Constants {

  // Game constants
  public static final int START_MONEY = 10;

  // Tile constants
  /**
   * The distance between the center point of the hexagon and the center-point of one side of the
   * hexagon.
   */
  public static final int TILE_RADIUS = 108;
  public static final int TILE_WIDTH = (int) (TILE_RADIUS * 4 / Math.sqrt(3));
  public static final int TILE_HEIGHT = TILE_RADIUS * 2;
  public static final int NUM_SIDES = Direction.values().length;

  // Renderer constants
  /**
   * The width of the window that will be assumed when all textures, words, etc. are drawn to the
   * screen. Everything will be rendered to this resolution, then scaled to the actual resolution.
   */
  public static final int NATIVE_WINDOW_WIDTH = 3840;
  /**
   * @see {@link #NATIVE_WINDOW_WIDTH}
   */
  public static final int NATIVE_WINDOW_HEIGHT = 2160;

  // Board constants
  public static final Point BOARD_CENTER = new Point((NATIVE_WINDOW_WIDTH - TILE_WIDTH) / 2,
                                                     (NATIVE_WINDOW_HEIGHT - TILE_HEIGHT) / 2);
  /**
   * The combat strength factor specified by {@link groundwar.unit.Unit#getStrengthVs} is then
   * randomized to be within this value of it's original. For example, if it original value is x and
   * this value is 0.1, then the randomized value will be in the range [0.9x, 1.1x].
   */
  public static final float DAMAGE_MARGIN = 0.1f;

  // File paths
  public static final String BOARD_FILE = "/boards/board.csv";
  public static final String TEXTURE_PATH = "/textures/%s.png";
  public static final String FONT_PATH = "/fonts/%s.ttf";

  // Texture names
  public static final String TILE_BG_NAME = "tile_background";
  public static final String TILE_OUTLINE_NAME = "tile_outline";

  // Font sizes
  public static final float FONT_SIZE_TILE = 60f;
  public static final float FONT_SIZE_UI = 80f;

  // UI sizes/positions
  public static final int TURN_COUNT_X = 3830;
  public static final int TURN_COUNT_Y = 2050;
  public static final int UNIT_HEALTH_WIDTH = (int) (Constants.TILE_WIDTH * 0.6f);
  public static final int UNIT_HEALTH_HEIGHT = 16;
  public static final int UNIT_HEALTH_X = (TILE_WIDTH - UNIT_HEALTH_WIDTH) / 2; // Centered
  public static final int UNIT_HEALTH_Y = 48;
  public static final int UNIT_MOVES_X = 60;
  public static final int UNIT_MOVES_Y = TILE_HEIGHT - 80;
  public static final int UNIT_INFO_X = 10;
  public static final int UNIT_INFO_Y = 10;
}
