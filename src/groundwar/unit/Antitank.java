package groundwar.unit;

import groundwar.Player;

public class Antitank extends Unit {

  public Antitank(Player owner) {
    super(UnitType.ANTITANK, owner);
  }

  @Override
  public float getStrengthVs(Unit defender) {
    switch (defender.getType()) {
      case MARINES:
        return 0.33f;
      case ANTITANK:
        return 0.5f;
      case TANK:
        return 0.67f;
      default:
        throw new IllegalArgumentException("Unrecognized unit type: " + defender.getType());
    }
  }
}
