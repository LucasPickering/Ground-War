package groundwar.board.unit;

import groundwar.board.Flag;
import groundwar.board.Player;

public class Marines extends Unit {

    private Flag flag;

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

    @Override
    public Flag getFlag() {
        return flag;
    }

    @Override
    public void grabFlag(Flag flag) {
        if (hasFlag()) {
            throw new IllegalStateException("This unit is already carrying a flag!");
        }
        this.flag = flag;
    }

    @Override
    public Flag dropFlag() {
        if (!hasFlag()) {
            throw new IllegalStateException("This unit is not carrying a flag!");
        }
        Flag toReturn = flag;
        flag = null;
        return toReturn;
    }
}
