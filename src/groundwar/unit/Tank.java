package groundwar.unit;

import groundwar.Player;

public class Tank extends Unit {

  public Tank(Player owner) {
    super(UnitType.TANK, owner);
  }

  @Override
  public float getStrengthVs(Unit defender) {
    switch (defender.getType()) {
      case MARINES:
        return 0.67f;
      case ANTITANK:
        return 0.33f;
      case TANK:
        return 0.5f;
      default:
        throw new IllegalArgumentException("Unrecognized unit type: " + defender.getType());
    }
  }
}
