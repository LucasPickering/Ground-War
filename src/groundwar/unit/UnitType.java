package groundwar.unit;

import groundwar.Player;

public enum UnitType {

  MARINES(UnitCategory.INFANTRY, Marines.class, "Marines", 1, 2, 10, 5),
  ANTITANK(UnitCategory.ANTIARMOR, Antitank.class, "AT Squad", 2, 3, 12, 7),
  TANK(UnitCategory.ARMOR, Tank.class, "Tank", 3, 4, 15, 8);

  public final Class<? extends Unit> unitClass;
  public final UnitCategory category;
  public final String textureName;
  public final String displayName;
  public final int cost;
  public final int movesPerTurn;
  public final int maxHealth;
  public final int combatStrength;

  UnitType(Class<? extends Unit> unitClass, UnitCategory category, String displayName, int cost,
           int movesPerTurn, int maxHealth, int combatStrength) {
    this.unitClass = unitClass;
    this.category = category;
    this.textureName = toString().toLowerCase();
    this.displayName = displayName;
    this.cost = cost;
    this.movesPerTurn = movesPerTurn;
    this.maxHealth = maxHealth;
    this.combatStrength = combatStrength;
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
