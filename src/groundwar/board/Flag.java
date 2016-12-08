package groundwar.board;

public class Flag {

    private final Player owner;

    public Flag(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
