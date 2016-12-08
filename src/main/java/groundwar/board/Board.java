package groundwar.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import groundwar.board.tile.Tile;
import groundwar.board.unit.Unit;
import groundwar.board.unit.UnitType;
import groundwar.util.BoardHandler;
import groundwar.util.Constants;
import groundwar.util.Direction;
import groundwar.util.Funcs;
import groundwar.util.Path;
import groundwar.util.Point;

public class Board {

    private final Player[] players = new Player[PlayerInfo.values().length];
    private int currentPlayer;
    private int turnCount = 1;
    private final Map<Point, Tile> tiles;

    /**
     * The tile that is currently selected. If {@code selectedTile != null}, then {@code
     * selectedTile .hasUnit()}.
     */
    private Tile selectedTile;
    private Unit spawningUnit;

    /**
     * A set of all paths that the currently selected unit ({@code selectedTile.getUnit()}) can move
     * through. All paths must have their origin be {@link #selectedTile}, and must be moveable,
     * meaning that each tile in the path is moveable for the currently-selected unit. Each entry in
     * the map contains the destination of the path, and the path itself.
     *
     * Empty if {@code selectedTile == null}.
     */
    private final Map<Tile, Path> moveablePaths = new HashMap<>();

    /**
     * A map of all paths that the currently-selected unit ({@code selectedTile.getUnit()}) can
     * attack via. All paths must have their origin be {@link #selectedTile}, and must be
     * attackable, meaning that each tile in the path is moveable for the currently-selected unit,
     * except for the last, which is attackable. Each entry in the map contains the destination of
     * the path, and the path itself.
     *
     * Empty if {@code selectedTile == null}.
     */
    private final Map<Tile, Path> attackablePaths = new HashMap<>();
    private final Random random = new Random();
    private Player winner;

    public Board() throws IOException {
        // Initialize players
        for (PlayerInfo color : PlayerInfo.values()) {
            players[color.ordinal()] = new Player(color);
        }

        // Load tiles from the file
        tiles = new BoardHandler(this).loadBoard();
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    public Player getPlayer(PlayerInfo color) {
        return players[color.ordinal()];
    }

    public int getTurnCount() {
        return turnCount;
    }

    public Map<Point, Tile> getTiles() {
        return tiles;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public boolean hasSelectedTile() {
        return selectedTile != null;
    }

    public boolean isSelected(Tile tile) {
        return tile == selectedTile;
    }

    public Unit getSpawningUnit() {
        return spawningUnit;
    }

    /**
     * Called when a specific tile is clicked.
     *
     * @param tile the tile clicked (non-null)
     * @throws NullPointerException if {@code tile == null}
     */
    public void onTileClicked(Tile tile) {
        Objects.requireNonNull(tile);
        if (selectedTile == null && spawningUnit != null) { // Spawn the unit
            spawnUnit(tile);
        } else if (selectedTile != null && selectedTile != tile) { // Move the unit
            if (moveSelectedUnit(tile) || attackWithSelectedUnit(tile)) {
                unselectTile();
            }
        } else if (selectedTile != tile && tile
            .isSelectable(getCurrentPlayer())) { // Select the tile
            selectTile(tile);
        } else {
            unselectTile();
        }
    }

    /**
     * Selects the given tile. Sets {@link #selectedTile} equal to {@code tile}.
     *
     * @param tile the tile to select (non-null, {@code tile.hasUnit()})
     * @throws NullPointerException if {@code tile == null}
     */
    public void selectTile(Tile tile) {
        Objects.requireNonNull(tile);
        Objects.requireNonNull(tile.getUnit());
        selectedTile = tile;
        populatePaths();
    }

    /**
     * Un-selects the currently-selected tile. Sets {@link #selectedTile} equal to {@code null}.
     */
    public void unselectTile() {
        selectedTile = null;
        moveablePaths.clear();
        attackablePaths.clear();
    }

    /**
     * Prepare to spawn a unit of the given type, if allowed.
     *
     * @param unitType the type of unit to spawn
     */
    public void prepareToSpawn(UnitType unitType) {
        if (unitType == null) {
            spawningUnit = null;
        } else if (unitType.cost <= getCurrentPlayer().getGold()) {
            selectedTile = null;
            spawningUnit = unitType.createUnit(getCurrentPlayer());
        }
    }

    /**
     * Stops spawning a unit.
     */
    public void cancelSpawning() {
        spawningUnit = null;
    }

    /**
     * Attempt to spawn {@link #spawningUnit} on the given tile. Assumes the player has enough gold
     * to spawn the unit.
     *
     * @param tile the tile to be spawned on
     */
    private void spawnUnit(Tile tile) {
        if (tile.isSpawnable(spawningUnit)) {
            getCurrentPlayer().decrGold(spawningUnit.getCost());
            tile.setUnit(spawningUnit);
            spawningUnit = null;
        }
    }

    /**
     * Can the unit on {@link #selectedTile} be moved to {@code to}?
     *
     * @param destination the tile to be moved to (non-null)
     * @return true if the unit can be moved, false otherwise
     * @throws NullPointerException if {@code selectedTile == null} or {@code destination == null}
     */
    public boolean canSelectedMoveTo(Tile destination) {
        Objects.requireNonNull(selectedTile);
        Objects.requireNonNull(destination);
        final Path path = moveablePaths.get(destination);
        return path != null && path.getDestination().isMoveable(selectedTile.getUnit());
    }

    /**
     * Moves the unit on {@link #selectedTile} to {@code destination}.
     *
     * @param destination the tile to be moved to, if valid (non-null)
     * @return true if the unit was moved, false otherwise
     * @throws NullPointerException if {@code destination == null}
     */
    private boolean moveSelectedUnit(Tile destination) {
        Objects.requireNonNull(destination);
        final Path path = moveablePaths.get(destination);
        if (path != null) {
            moveUnit(selectedTile, destination, path.getLength());
            return true;
        }
        return false;
    }

    /**
     * Moves the unit from th first tile to the second. Also removes {@code distance} moves from the
     * unit. NO CHECKS ARE PERFORMED, so be careful with this!
     *
     * @param from     the tile to move from (non-null)
     * @param to       the tile to move to (non-null)
     * @param distance the distance that the move was
     * @throws NullPointerException if {@code from == null} or {@code to == null}
     */
    private void moveUnit(Tile from, Tile to, int distance) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (from != to) {
            to.setUnit(from.getUnit());
            from.setUnit(null);
            to.getUnit().useMoves(distance);

            // After moving the unit, check the moved-to tile for victory
            if (to.shouldGameEnd()) {
                endGame();
            }
        }
    }

    /**
     * Can the unit on {@link #selectedTile} conductCombat the unit on {@code to}? Units cna only
     * conductCombat adjacent tiles.
     *
     * @param destination the tile to be attacked
     * @return true if the unit on can conductCombat, false otherwise
     * @throws NullPointerException if {@code selectedTile == null} or {@code destination == null}
     */
    public boolean canSelectedAttack(Tile destination) {
        Objects.requireNonNull(selectedTile);
        Objects.requireNonNull(destination);
        final Path path = attackablePaths.get(destination);
        return path != null && path.getDestination().isAttackable(selectedTile.getUnit());
    }

    /**
     * Attacks the unit on {@code destination} with the unit on {@link #selectedTile}. If the
     * defending unit is destroyed, the attacking unit moves onto that tile, otherwise it moves onto
     * the second-to-last tile in the path.
     *
     * @param destination the tile to be attacked
     * @return true if the attack occurs, false otherwise
     * @throws NullPointerException if {@code destination == null}
     */
    private boolean attackWithSelectedUnit(Tile destination) {
        Objects.requireNonNull(destination);
        final Path path = attackablePaths.get(destination);
        if (path != null) {
            // Get the tile adjacent to the defender first, then move the unit there.
            final Tile adjTile = path.getSecondToLastTile();
            moveUnit(selectedTile, adjTile, path.getLength() - 1);

            // Conduct combat. If the attacker wins, it will be moved onto the defender's tile, otherwise
            // both units hold their positions (unless they're killed).
            conductCombat(adjTile, destination);
            return true;
        }
        return false;
    }

    /**
     * Conducts combat between the two given units. The two units must be adjacent to each other.
     *
     * @param attackingTile the attacking unit
     * @param defendingTile the defending unit
     */
    private void conductCombat(Tile attackingTile, Tile defendingTile) {
        // Shortcuts for the units
        final Unit attackingUnit = attackingTile.getUnit();
        final Unit defendingUnit = defendingTile.getUnit();

        // The bias towards the attacker
        final float attackerBias = attackingUnit.getStrengthVs(defendingUnit.getCategory());

        // The actual bias used will be in the range [biasLow, biasHigh]
        final float biasLow = attackerBias - Constants.DAMAGE_MARGIN;
        final float biasHigh = attackerBias + Constants.DAMAGE_MARGIN;

        // Calculate inflicted damage
        final int attackerDamage = (int) (defendingUnit.getCombatStrength() *
                                          Funcs.randomInRange(random, biasLow, biasHigh));
        final int defenderDamage = (int) (attackingUnit.getCombatStrength() *
                                          Funcs.randomInRange(random, biasLow, biasHigh));

        System.out.printf("Attacking %s %s took %d damage!\n", attackingUnit.getOwner(),
                          attackingUnit.getDisplayName(), attackerDamage);
        System.out.printf("Defending %s %s took %d damage!\n", defendingUnit.getOwner(),
                          defendingUnit.getDisplayName(), defenderDamage);
        // Inflict the damage
        attackingTile.hurtUnit(attackerDamage);
        defendingTile.hurtUnit(defenderDamage);

        if (attackingTile.hasUnit()) {
            // The attacker survived. Take away all their movement points.
            attackingTile.getUnit().exhaustMoves();
            if (!defendingTile.hasUnit()) {
                // The defender was killed. Move the attacker onto the defender's tile
                moveUnit(attackingTile, defendingTile, 0);
            }
        }
    }

    /**
     * Move to the next player's turn.
     */
    public void nextTurn() {
        tiles.values().forEach(Tile::onEndTurn); // Call onEndTurn() for each tile
        // Reset movement points for each unit
        tiles.values().stream().filter(Tile::hasUnit).forEach(tile -> tile.getUnit().resetMoves());
        cancelSpawning(); // Cancel unit spawning
        unselectTile();

        // Next player. If this was the last player...
        if (++currentPlayer >= players.length) {
            currentPlayer = 0; // Go back to the first player
        }
        turnCount++;
    }

    private void endGame() {
        winner = getCurrentPlayer();
    }

    public Player getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return winner != null;
    }

    /**
     * Gets a {@link Set} of all paths within moveable range of {@link #selectedTile}. A path is
     * moveable if each tile in it is moveable, and its length is less than or equal to the selected
     * unit's remaining moves.
     */
    private void populatePaths() {
        populatePaths(new Path(selectedTile), selectedTile.getUnit(),
                      selectedTile.getUnit().getMovesRemaining());
    }

    /**
     * Gets a {@link Set} of all moveable paths that are at mose {@code range} steps longer than
     * {@code path}. A path is moveable if each tile in it is moveable for {@code unit}.
     *
     * @param path  the path to add onto (non-null, terminated)
     * @param unit  the unit to be checked for movement
     * @param range the amount of tiles to spread outwards (positive)
     */
    private void populatePaths(Path path, Unit unit, int range) {
        if (range > 0) {
            for (Direction dir : Direction.values()) {
                Tile nextTile = path.getDestination().getAdjacentTile(dir);
                if (nextTile != null) {
                    // Create a path to the adjacent tile
                    Path newPath = path.copy();
                    newPath.addTile(nextTile);

                    // If it's moveable, add it to moveablePaths and search deeper
                    // If it's attackable, add it to attackPaths, but don't search any deeper
                    if (nextTile.isMoveable(unit)) {
                        addPath(moveablePaths, newPath);
                        populatePaths(newPath, unit, range - 1); // Search further out
                    } else if (nextTile.isAttackable(unit)) {
                        addPath(attackablePaths, newPath);
                    }
                }
            }
        }
    }

    /**
     * Adds the given path to the given map. If the map already contains a path leading to the same
     * tile as the given path, then the shorter of the two will stay in the map.
     *
     * @param map  the map to be added to
     * @param path the path to be added
     */
    private void addPath(Map<Tile, Path> map, Path path) {
        final Tile destination = path.getDestination();
        final Path currentPath = map.get(path.getDestination());

        // If there is no current path to the tile, or the new path is shorter
        if (currentPath == null || path.getLength() < currentPath.getLength()) {
            map.put(destination, path); // Put it in the list
        }
    }
}
