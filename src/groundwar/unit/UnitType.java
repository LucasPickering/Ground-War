package groundwar.unit;

import groundwar.Player;

public enum UnitType {
  MARINES("Marines", 1, 2, 10), ANTITANK("AT Squad", 2, 3, 12), TANK("Tank", 3, 4, 20);

  private final Class<? extends Unit> unitClass;
  public final String textureName;
  public final String displayName;
  public final int cost;
  public final int movesPerTurn;
  public final int maxHealth;

  UnitType(String textureName, int cost, int movesPerTurn, int maxHealth) {
    this(Unit.class, textureName, cost, movesPerTurn, maxHealth);
  }

  UnitType(Class<? extends Unit> unitClass, String displayName, int cost, int movesPerTurn, int maxHealth) {
    this.unitClass = unitClass;
    this.textureName = toString().toLowerCase();
    this.displayName = displayName;
    this.cost = cost;
    this.movesPerTurn = movesPerTurn;
    this.maxHealth = maxHealth;
  }

  public Unit createUnit(Player owner) {
    try {
      return unitClass.newInstance().init(this, owner);
    } catch (InstantiationException | IllegalAccessException e) {
      System.err.println("Error initializing unit");
      e.printStackTrace();
      return null;
    }
  }
}
