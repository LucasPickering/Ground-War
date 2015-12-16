package groundwar.unit;

import groundwar.Player;

public enum UnitType {
  MARINES("marines", 1), ANTITANK("antitank", 2), TANK("tank", 3);

  public final String name;
  public final int cost;

  UnitType(String name, int cost) {
    this.name = name;
    this.cost = cost;
  }

  public Unit createUnit(Player owner) {
    return new Unit(this, owner);
  }
}
