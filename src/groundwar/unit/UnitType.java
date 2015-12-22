package groundwar.unit;

import groundwar.Player;

public enum UnitType {
  MARINES("marines", 1, 2, 10), ANTITANK("antitank", 2, 3, 12), TANK("tank", 3, 4, 20);

  private final Class<? extends Unit> unitClass;
  public final String name;
  public final int cost;
  public final int movesPerTurn;
  public final int maxHealth;

  UnitType(String name, int cost, int movesPerTurn, int maxHealth) {
    this(Unit.class, name, cost, movesPerTurn, maxHealth);
  }

  UnitType(Class<? extends Unit> unitClass, String name, int cost, int movesPerTurn, int maxHealth) {
    this.unitClass = unitClass;
    this.name = name;
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
