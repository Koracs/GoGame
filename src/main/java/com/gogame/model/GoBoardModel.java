package com.gogame.model;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.listener.GameState;
import com.gogame.savegame.MoveHistory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GoBoardModel {
    //region Fields
    private static final int[] sizes = new int[]{9, 13, 19};
    private final double komi;
    private final int handicap;
    private final int size;
    private int handicapCount;
    private Stone currentPlayer;
    private GameState gameState;

    private int capturedByWhite;
    private int capturedByBlack;
    private boolean prevPassed;

    private double pointsWhite;
    private double pointsBlack;

    private GoField[][] fields;
    private boolean[][] visited;
    private final List<GameListener> listeners;

    private GoField lastCapture;

    private MoveHistory moveHistory;
    //endregion


    public GoBoardModel(int size, double komi, int handicap) {
        if (size <= 0) throw new IllegalArgumentException("Size must be a positive value");
        if (komi < 0.0) throw new IllegalArgumentException("Komi must be a non-negative value.");
        if (handicap < 0) throw new IllegalArgumentException("Handicap must be a non-negative integer.");

        this.size = size;
        this.komi = komi;
        this.handicap = handicap;

        currentPlayer = Stone.BLACK;
        listeners = new LinkedList<>();
        gameState = GameState.GAME_START;
        prevPassed = false;
        moveHistory = new MoveHistory(this);

        initModel();
        initHandicapFields();
    }

    private void initModel() {
        fields = new GoField[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                fields[row][col] = new GoField(row, col);
            }
        }

        currentPlayer = Stone.BLACK;
        pointsBlack = 0;
        pointsWhite = 0;
        capturedByBlack = 0;
        capturedByWhite = 0;
    }

    private void initHandicapFields() {
        int[] handicapFields = new int[0];
        switch (size) {
            case 19 -> handicapFields = new int[]{3, 9, 15};
            case 13 -> handicapFields = new int[]{3, 6, 9};
            case 9 -> {
                handicapFields = new int[]{2, 6};
                fields[4][4].setStone(Stone.PRESET);
            }
        }
        for (int row : handicapFields) {
            for (int col : handicapFields) {
                fields[row][col].setStone(Stone.PRESET);
            }
        }
        if (handicap > 0) {
            gameState = GameState.PLACE_HANDICAP;
            handicapCount = handicap;
        }
    }

    //region Getter/Setter

    public static int[] getSizes() {
        return sizes;
    }

    public int getHandicap() {
        return handicap;
    }

    public double getKomi() {
        return komi;
    }

    public Stone getCurrentPlayer() {
        return this.currentPlayer;
    }

    private Stone getOtherPlayer() {
        return currentPlayer == Stone.BLACK ? Stone.WHITE : Stone.BLACK;
    }

    public double getPointsWhite() {
        return pointsWhite;
    }

    public double getPointsBlack() {
        return pointsBlack;
    }


    public int getSize() {
        return size;
    }

    public GoField[][] getFields() {
        return fields;
    }

    public GoField getField(int row, int col) {
        return fields[row][col];
    }

    public GameState getGameState() {
        return gameState;
    }


    public int getCapturedByWhite() {
        return capturedByWhite;
    }

    public int getCapturedByBlack() {
        return capturedByBlack;
    }

    public MoveHistory getHistory() {
        return moveHistory;
    }

    //endregion

    /**
     * Places a stone only on the game fields.
     * Suicide moves will be ignored.
     * Captures are checked for adjacent fields after placement.
     *
     * @param row Row of the board
     * @param col Column of the board
     */
    public void makeMove(int row, int col) {
        prevPassed = false;

        if (gameState == GameState.PLACE_HANDICAP) {
            makeHandicapMove(row, col);
            return;
        }

        if (fields[row][col].isEmpty()) {
            fields[row][col].setStone(currentPlayer);
            if (moveIsSuicide(row, col) || moveIsKo(row, col)) {
                fields[row][col].removeStone();
                return;
            }

            checkCapture(row, col);
            switchPlayer();

            for (GameListener listener : listeners) {
                listener.moveCompleted(new GameEvent(this, gameState, row, col));
            }
        }
    }

    /**
     * Places a stone only on given handicap fields (Only allowed for black player if handicap is greater than 0)
     *
     * @param row Row of the board
     * @param col Column of the board
     */
    private void makeHandicapMove(int row, int col) {
        if (fields[row][col].isPreset()) {
            fields[row][col].setStone(currentPlayer);
            handicapCount -= 1;

            if (handicapCount == 0) switchPlayer();

            for (GameListener listener : listeners) {
                listener.moveCompleted(new GameEvent(this, gameState, row, col));
            }

        }
    }

    /**
     * Check if field or surrounding fields are captured.
     * If a capture move happened, adds points to the corresponding player
     *
     * @param row Row of the board
     * @param col Column of the board
     */
    private void checkCapture(int row, int col) {
        List<GoField> neighbours = new ArrayList<>();

        //top
        if (row > 0) {
            findNeighboursOfSameColor(row - 1, col, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) {
                neighbours.forEach(e -> addCapturePoints(e.getStone()));
                neighbours.forEach(GoField::removeStone);
            }
            neighbours.clear();
        }
        //right
        if (col < fields.length - 1) {
            findNeighboursOfSameColor(row, col + 1, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) {
                neighbours.forEach(e -> addCapturePoints(e.getStone()));
                neighbours.forEach(GoField::removeStone);
            }
            neighbours.clear();
        }
        //bottom
        if (row < fields.length - 1) {
            findNeighboursOfSameColor(row + 1, col, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) {
                neighbours.forEach(e -> addCapturePoints(e.getStone()));
                neighbours.forEach(GoField::removeStone);
            }
            neighbours.clear();
        }
        //left
        if (col > 0) {
            findNeighboursOfSameColor(row, col - 1, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) {
                neighbours.forEach(e -> addCapturePoints(e.getStone()));
                neighbours.forEach(GoField::removeStone);
            }
            neighbours.clear();
        }
    }

    /**
     * Adds a Capture point to the player which captured the given Stone. Increases score by one point
     *
     * @param stone Stone that has been captured
     */
    private void addCapturePoints(Stone stone) {
        if (stone == Stone.BLACK) capturedByWhite++;
        else if (stone == Stone.WHITE) capturedByBlack++;
    }

    /**
     * Find neighbours of current stone position with the same color. Recursive function
     *
     * @param row          Row of the board
     * @param col          Column of the board
     * @param neighbours   List of neighbouring GoFields
     * @param currentColor Color of the current Stone
     */
    private void findNeighboursOfSameColor(int row, int col, List<GoField> neighbours, Stone currentColor) {
        if (col < 0 || col >= fields.length || row < 0 || row >= fields.length) {
            return;
        }
        if (fields[row][col].isPreset()) return;
        if (neighbours.contains(fields[row][col])) return;

        if (fields[row][col].getStone().equals(currentColor)) neighbours.add(fields[row][col]);
        else return;

        //top
        if (row > 0) findNeighboursOfSameColor(row - 1, col, neighbours, currentColor);
        //right
        if (col < fields.length - 1) findNeighboursOfSameColor(row, col + 1, neighbours, currentColor);
        //bottom
        if (row < fields.length - 1) findNeighboursOfSameColor(row + 1, col, neighbours, currentColor);
        //left
        if (col > 0) findNeighboursOfSameColor(row, col - 1, neighbours, currentColor);
    }

    /**
     * Check if a given chain contains liberties. Used to determine if a Stone or chain of stones is captured.
     *
     * @param chain List of GoFields in a chain
     * @return True if the chain contains liberties, false if it is captured
     */
    private boolean chainHasLiberties(List<GoField> chain) {
        if (chain.isEmpty()) return true;
        for (GoField field : chain) {
            int row = field.getRow();
            int col = field.getCol();

            if (row > 0 && fields[row - 1][col].isEmpty()) return true; //top
            if (col < fields.length - 1 && fields[row][col + 1].isEmpty()) return true; //right
            if (row < fields.length - 1 && fields[row + 1][col].isEmpty()) return true; //bottom
            if (col > 0 && fields[row][col - 1].isEmpty()) return true; //left
        }

        return false;
    }

    /**
     * Check if a given move is considered suicide.
     *
     * @param row Row of the board
     * @param col Column of the board
     * @return True if the move is considered suicide, False if the move is valid
     */
    private boolean moveIsSuicide(int row, int col) {
        List<GoField> neighbours = new ArrayList<>();

        //top
        if (row > 0) {
            findNeighboursOfSameColor(row - 1, col, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) {
                return false;
            }
            neighbours.clear();
        }
        //right
        if (col < fields.length - 1) {
            findNeighboursOfSameColor(row, col + 1, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) {
                return false;
            }
            neighbours.clear();
        }
        //bottom
        if (row < fields.length - 1) {
            findNeighboursOfSameColor(row + 1, col, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) {
                return false;
            }
            neighbours.clear();
        }
        //left
        if (col > 0) {
            findNeighboursOfSameColor(row, col - 1, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) {
                return false;
            }
            neighbours.clear();
        }

        findNeighboursOfSameColor(row, col, neighbours, currentPlayer);
        if (!chainHasLiberties(neighbours)) {
            return true;
        }
        neighbours.clear();

        return false;
    }

    /**
     * Determine if a Move is considered Ko.
     *
     * @param row Row of the board
     * @param col Column of the board
     * @return True if the move is considered Ko, False if the move is valid
     */
    private boolean moveIsKo(int row, int col) {
        List<GoField> neighbours = new ArrayList<>();
        List<GoField> allNeighbours = new ArrayList<>();

        //top
        if (row > 0) {
            findNeighboursOfSameColor(row - 1, col, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) allNeighbours.addAll(neighbours);
            neighbours.clear();
        }
        //right
        if (col < fields.length - 1) {
            findNeighboursOfSameColor(row, col + 1, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) allNeighbours.addAll(neighbours);
            neighbours.clear();
        }
        //bottom
        if (row < fields.length - 1) {
            findNeighboursOfSameColor(row + 1, col, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) allNeighbours.addAll(neighbours);
            neighbours.clear();
        }
        //left
        if (col > 0) {
            findNeighboursOfSameColor(row, col - 1, neighbours, getOtherPlayer());
            if (!chainHasLiberties(neighbours)) allNeighbours.addAll(neighbours);
            neighbours.clear();
        }

        if (allNeighbours.size() == 1) {
            if (lastCapture == getField(row, col)) return true;
            lastCapture = allNeighbours.get(0);
        } else lastCapture = null;
        return false;
    }

    /**
     * Private function to switch the current player and also set the game state to the current player
     */
    private void switchPlayer() {
        currentPlayer = getOtherPlayer();
        gameState = currentPlayer == Stone.BLACK ? GameState.BLACK_TURN : GameState.WHITE_TURN;
    }

    /**
     * Reset the current game - clear the game field array and also the UI game field with the listener
     */
    public void reset() {
        gameState = GameState.RESET;
        initModel();
        initHandicapFields();

        for (GameListener listener : listeners) {
            listener.resetGame(new GameEvent(this, gameState));
        }
    }

    /**
     * Current player passed. Check if the previous player also passed (game ends) and continue game (switch player)
     */
    public void pass() {
        if (prevPassed) {
            // Player in previous round passed - game ends
            gameEnds();
            return;
        }

        gameState = currentPlayer == Stone.BLACK ? GameState.BLACK_PASSED : GameState.WHITE_PASSED;

        for (GameListener listener : listeners) {
            listener.playerPassed(new GameEvent(this, gameState));
        }
        switchPlayer();
        prevPassed = true;
    }

    /**
     * Game ended (both players passed or one player resigned). Calculate the current score and set the game state to display the winner
     */
    private void gameEnds() {
        calculateScores();

        if (pointsBlack == pointsWhite) {
            gameState = GameState.DRAW;
        } else {
            gameState = pointsWhite > pointsBlack ? GameState.WHITE_WON : GameState.BLACK_WON;
        }

        for (GameListener listener : listeners) {
            listener.gameEnded(new GameEvent(this, gameState));
        }
    }

    /**
     * Calculate the score for both players - score = captured stones + stones on game field + captured areas
     */
    private void calculateScores() {
        pointsBlack += capturedByBlack;
        pointsWhite += capturedByWhite;

        // Add points for the stones on the field
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (fields[row][col].getStone() == Stone.BLACK) {
                    pointsBlack++;
                } else if (fields[row][col].getStone() == Stone.WHITE) {
                    pointsWhite++;
                }
            }
        }

        // When no stone is played -> do not count area
        if(pointsWhite == 0 && pointsBlack == 0) {
            return;
        }

        // Initialize visited array
        visited = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                visited[i][j] = false;
            }
        }

        // Add points for the controlled areas on the board
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (!visited[row][col] && fields[row][col].isEmpty()) {
                    List<Integer> area = new ArrayList<>();
                    boolean isSurroundedBlack = true;
                    boolean isSurroundedWhite = true;
                    findEmptyArea(row, col, area);
                    for (int pos : area) {
                        int r = pos / size;
                        int c = pos % size;
                        if (!isSurrounded(r, c, Stone.BLACK)) {
                            isSurroundedBlack = false;
                        }
                        if (!isSurrounded(r, c, Stone.WHITE)) {
                            isSurroundedWhite = false;
                        }
                        if (!isSurroundedWhite && !isSurroundedBlack) {
                            break;
                        }
                    }
                    if (isSurroundedWhite) {
                        pointsWhite += area.size();
                    } else if (isSurroundedBlack) {
                        pointsBlack += area.size();
                    }
                }
            }
        }
    }

    /**
     * Determine the empty (no white or black stone) area starting from the transfered row and column
     *
     * @param row Row of the board
     * @param col Column of the board
     * @param area One dimensional store of the empty tiles
     */
    private void findEmptyArea(int row, int col, List<Integer> area) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return;
        }
        if (visited[row][col]) {
            return;
        }
        if (!fields[row][col].isEmpty()) {
            return;
        }
        visited[row][col] = true;

        int position = row * size + col;
        area.add(position);
        findEmptyArea(row + 1, col, area);
        findEmptyArea(row - 1, col, area);
        findEmptyArea(row, col + 1, area);
        findEmptyArea(row, col - 1, area);
    }

    /**
     * Determine if a tile is not surrounded by stones of the other player
     *
     * @param row Row of the board
     * @param col Column of the board
     * @param stone Stone of the current player
     * @return True, if it is only surrounded by stones of the same color or empty tiles
     */
    private boolean isSurrounded(int row, int col, Stone stone) {
        boolean isSurrounded = true;
        isSurrounded &= checkNeighbours(row + 1, col, stone);
        isSurrounded &= checkNeighbours(row - 1, col, stone);
        isSurrounded &= checkNeighbours(row, col + 1, stone);
        isSurrounded &= checkNeighbours(row, col - 1, stone);
        return isSurrounded;
    }

    /**
     * Determine if this tile is the same color or empty
     *
     * @param row Row of the board
     * @param col Column of the board
     * @param stone Checked stone color
     * @return True if on the current tile is no stone or a stone of the same color as well as if the tile is outside the game area
     */
    private boolean checkNeighbours(int row, int col, Stone stone) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return true;
        }
        if (fields[row][col].isEmpty() || fields[row][col].getStone() == stone) {
            return true;
        }
        return false;
    }

    /**
     * On player resigned - check for the score
     */
    public void playerResigned() {
        gameEnds();
    }

    /**
     * Function to display the game field on the console
     */
    public void printModel() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                switch (fields[row][col].getStone()) {
                    case BLACK -> System.out.print("\033[1;30m" + "B " + "\033[0m");
                    case WHITE -> System.out.print("\033[1;33m" + "W " + "\033[0m");
                    case PRESET -> System.out.print("\033[0;35m" + "P " + "\033[0m");
                    case NONE -> System.out.print("\033[0;32m" + "N " + "\033[0m");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    //endregion

    //region Listeners
    public void addGameListener(GameListener l) {
        listeners.add(l);
    }


    public void removeGameListener(GameListener l) {
        listeners.remove(l);
    }


    public List<GameListener> getGameListeners() {
        return this.listeners;
    }

    public void setGameListeners(List<GameListener> listeners) { //todo rename to addGameListeners?
        this.listeners.addAll(listeners);
    }
    //endregion
}
