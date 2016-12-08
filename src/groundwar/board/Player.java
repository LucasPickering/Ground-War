package groundwar.board;

import groundwar.util.Constants;

public class Player {

    private final PlayerInfo playerInfo;
    private int gold = Constants.STARTING_GOLD;

    public Player(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public PlayerInfo getInfo() {
        return playerInfo;
    }

    public int getGold() {
        return gold;
    }

    /**
     * Increases this player's gold by the given amount.
     *
     * @param amt the amount to be added, (non-negative)
     * @throws IllegalStateException if {@code amt > {@link #gold}}
     */
    public void incrGold(int amt) {
        if (amt < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        gold += amt;
    }

    /**
     * Decreases this player's gold by the given amount.
     *
     * @param amt the amount to be taken away, must be <= {@link #gold} (non-negative)
     * @throws IllegalArgumentException if {@code amt < 0}
     * @throws IllegalStateException    if {@code amt > {@link #gold}}
     */
    public void decrGold(int amt) {
        if (amt < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (amt > gold) {
            throw new IllegalStateException("Amount must be <= gold");
        }
        gold -= amt;
    }

    /**
     * Gets a String containing information about this player, meant to be displayed on the screen.
     * This info includes name and gold, with each value separated by a newline character.
     *
     * @return the String of information
     */
    public String getInfoString() {
        return String.format("%s\nGold: %d", playerInfo.displayName, getGold());
    }

    @Override
    public String toString() {
        return playerInfo.displayName + " Player";
    }
}
