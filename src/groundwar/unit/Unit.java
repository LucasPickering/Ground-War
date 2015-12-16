package groundwar.unit;

import groundwar.Player;

public class Unit {

  private final UnitType type;
  private final Player owner;

  public Unit(UnitType type, Player owner) {
    this.type = type;
    this.owner = owner;
  }

  public UnitType getType() {
    return type;
  }

  public Player getOwner() {
    return owner;
  }

  public String getName() {
    return type.name;
  }

  public int getCost() {
    return type.cost;
  }
}
