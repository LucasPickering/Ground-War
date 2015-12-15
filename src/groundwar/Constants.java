package groundwar;

public class Constants {

  // Game constants
  public static final int START_MONEY = 10;

  // Tile constants
  public static final int TILE_RADIUS = 62;
  public static final int TILE_WIDTH = TILE_RADIUS * 2;
  public static final int TILE_HEIGHT = (int) (TILE_RADIUS * Math.sqrt(3));

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

  // Board constants
  public static final Point BOARD_CENTER = new Point((NATIVE_WINDOW_WIDTH - TILE_WIDTH) / 2,
                                                     (NATIVE_WINDOW_HEIGHT - TILE_HEIGHT) / 2);
  public static final String BOARD_FILE = "/boards/board.csv";

  // Colors
  public static final int RED_COLOR = 0xff0000;
  public static final int RED2_COLOR = 0x8f7e3b;
  public static final int BLUE_COLOR = 0x0000ff;
  public static final int BLUE2_COLOR = 0x477e7b;
  public static final int TILE_BG_COLOR = 0x6aa84f;
  public static final int TILE_OUTLINE_COLOR = 0x434343;
  public static final int MOUNTAIN_BG_COLOR = 0x666666;
  public static final int GOLD_BG_COLOR = 0xaeb540;
  public static final int GOLD_OUTLINE_COLOR = 0xf1c232;
  public static final int FORT_BG_COLOR = 0xa26991;
  public static final int FORT_OUTLINE_COLOR = 0xff00ff;

}
