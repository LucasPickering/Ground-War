package groundwar.unit;

import groundwar.Player;

public enum UnitType {
  MARINES("marines", 1, 2), ANTITANK("antitank", 2, 3), TANK("tank", 3, 4);

  public final String name;
  public final int cost;
  public final int movementPointsPerTurn;

  UnitType(String name, int cost, int movementPointsPerTurn) {
    this.name = name;
    this.cost = cost;
    this.movementPointsPerTurn = movementPointsPerTurn;
  }

  public Unit createUnit(Player owner) {
    return new Unit(this, owner, movementPointsPerTurn);
  }
}
