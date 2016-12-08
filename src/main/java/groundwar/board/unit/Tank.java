package groundwar.board.unit;

import groundwar.board.Player;

public class Tank extends Unit {

    public Tank(Player owner) {
        super(UnitType.TANK, owner);
    }

    @Override
    public float getStrengthVs(UnitCategory category) {
        switch (category) {
            case INFANTRY:
                return 1.5f;
            case ANTIARMOR:
                return 0.5f;
            case ARMOR:
                return 1.0f;
            default:
                throw new IllegalArgumentException("Unrecognized unit category: " + category);
        }
    }
}
