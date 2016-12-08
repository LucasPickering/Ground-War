package groundwar.board.unit;

import groundwar.board.Player;

public class Antitank extends Unit {

    public Antitank(Player owner) {
        super(UnitType.ANTITANK, owner);
    }

    @Override
    public float getStrengthVs(UnitCategory category) {
        switch (category) {
            case INFANTRY:
                return 0.5f;
            case ANTIARMOR:
                return 1.0f;
            case ARMOR:
                return 1.5f;
            default:
                throw new IllegalArgumentException("Unrecognized unit category: " + category);
        }
    }
}
