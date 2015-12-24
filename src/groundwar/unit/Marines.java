package groundwar.unit;

import groundwar.Player;

public class Marines extends Unit {

  public Marines(Player owner) {
    super(UnitType.MARINES, owner);
  }

  @Override
  public float getStrengthVs(Unit defender) {
    switch (defender.getType()) {
      case MARINES:
        return 0.5f;
      case ANTITANK:
        return 0.67f;
      case TANK:
        return 0.33f;
      default:
        throw new IllegalArgumentException("Unrecognized unit type: " + defender.getType());
    }
  }
}
