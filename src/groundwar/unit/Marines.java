package groundwar.unit;

import groundwar.Player;

public class Marines extends Unit {

  public Marines(Player owner) {
    super(UnitType.MARINES, owner);
  }

  @Override
  public float getStrengthVs(UnitCategory category) {
    switch (category) {
      case INFANTRY:
        return 1.0f;
      case ANTIARMOR:
        return 1.5f;
      case ARMOR:
        return 0.5f;
      default:
        throw new IllegalArgumentException("Unrecognized unit category: " + category);
    }
  }
}
