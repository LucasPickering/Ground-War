package groundwar.unit;

import groundwar.Player;

public enum UnitType {
  MARINES("marines", 1, 2), ANTITANK("antitank", 2, 3), TANK("tank", 3, 4);

  public final String name;
  public final int cost;
  public final int movementPointsPerTurn;
  private final Class<? extends Unit> unitClass;

  UnitType(String name, int cost, int movementPointsPerTurn) {
    this(Unit.class, name, cost, movementPointsPerTurn);
  }

  UnitType(Class<? extends Unit> unitClass, String name, int cost, int movementPointsPerTurn) {
    this.unitClass = unitClass;
    this.name = name;
    this.cost = cost;
    this.movementPointsPerTurn = movementPointsPerTurn;
  }

  public Unit createUnit(Player owner) {
    try {
      return unitClass.newInstance().init(this, owner, movementPointsPerTurn);
    } catch (InstantiationException | IllegalAccessException e) {
      System.err.println("Error initializing unit");
      e.printStackTrace();
      return null;
    }
  }
}
