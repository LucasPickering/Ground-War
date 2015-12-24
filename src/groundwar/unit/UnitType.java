package groundwar.unit;

import groundwar.Player;

public enum UnitType {
  MARINES(Marines.class, "Marines", 1, 2, 10),
  ANTITANK(Antitank.class, "AT Squad", 2, 3, 12),
  TANK(Tank.class, "Tank", 3, 4, 20);

  public final Class<? extends Unit> unitClass;
  public final String textureName;
  public final String displayName;
  public final int cost;
  public final int movesPerTurn;
  public final int maxHealth;

  UnitType(Class<? extends Unit> unitClass, String displayName, int cost, int movesPerTurn,
           int maxHealth) {
    this.unitClass = unitClass;
    this.textureName = toString().toLowerCase();
    this.displayName = displayName;
    this.cost = cost;
    this.movesPerTurn = movesPerTurn;
    this.maxHealth = maxHealth;
  }

  public Unit createUnit(Player owner) {
    try {
      return unitClass.getConstructor(Player.class).newInstance(owner);
    } catch (Exception e) {
      System.err.println("Error creating unit!");
      e.printStackTrace();
      return null;
    }
  }
}
