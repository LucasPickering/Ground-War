package groundwar.unit;

import groundwar.Player;

public abstract class Unit {

  private final String name;
  private final Player owner;

  public Unit(String name, Player owner) {
    this.name = name;
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public Player getOwner() {
    return owner;
  }
}
