package groundwar.unit;

import groundwar.Player;

public abstract class Unit {

  private final Player owner;

  public Unit(Player owner) {
    this.owner = owner;
  }

  public Player getOwner() {
    return owner;
  }
}
