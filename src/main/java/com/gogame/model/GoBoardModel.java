package com.gogame.model;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.listener.GameState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GoBoardModel {
    //region Fields
    // MVC variables

    // Model variables
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
    private final List<GameListener> listeners;
    //endregion


    public GoBoardModel(int size, double komi, int handicap) {
        this.size = size;
        this.komi = komi;
        this.handicap = handicap;
        currentPlayer = Stone.BLACK;
        listeners = new LinkedList<>();
        gameState = GameState.GAME_START;
        prevPassed = false;

        initModel();
        initHandicapFields();
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

    private void initModel() {
        fields = new GoField[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                fields[row][col] = new GoField(row, col);
            }
        }

        currentPlayer = Stone.BLACK;
        pointsBlack = 0;
        pointsWhite = komi;
        capturedByBlack = 0;
        capturedByWhite = 0;
    }


    public int getSize() {
        return size;
    }

    public GoField[][] getFields() {
        return fields;
    }

    public GoField getField(int row, int col){
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

    //endregion

    public void makeMove(int row, int col) {
        // Set passed to false
        prevPassed = false;

        if (gameState == GameState.PLACE_HANDICAP) {
            makeHandicapMove(row, col);
            return;
        }

        if (fields[row][col].isEmpty()) {
            fields[row][col].setStone(currentPlayer);

            checkCapture(row, col);
            switchPlayer();

            for (GameListener listener : listeners) {
                listener.moveCompleted(new GameEvent(this, gameState, row, col));
            }
        }
    }

    private void makeHandicapMove(int row, int col) {
        if (fields[row][col].isPreset()) {
            fields[row][col].setStone(currentPlayer);
            handicapCount -= 1;

            for (GameListener listener : listeners) {
                listener.moveCompleted(new GameEvent(this, gameState, row, col));
            }

            if (handicapCount == 0) switchPlayer();
        }
    }

    /**
     * Check field or surrounding fields are captured.
     *
     * @param row Row of the board
     * @param col Column of the board
     */
    private void checkCapture(int row, int col) { //todo implement scoring
        List<GoField> neighbours = new ArrayList<>();

        //top
        if (row > 0) {
            findNeighboursOfSameColor(row - 1, col, neighbours, fields[row - 1][col].getStone());
            if (!chainContainsLiberties(neighbours)) {
                neighbours.forEach(GoField::removeStone);
            }
            neighbours.clear();
        }
        //right
        if (col < fields.length - 1) {
            findNeighboursOfSameColor(row, col + 1, neighbours, fields[row][col + 1].getStone());
            if (!chainContainsLiberties(neighbours)) {
                neighbours.forEach(GoField::removeStone);
            }
            neighbours.clear();
        }
        //bottom
        if (row < fields.length - 1) {
            findNeighboursOfSameColor(row + 1, col, neighbours, fields[row + 1][col].getStone());
            if (!chainContainsLiberties(neighbours)) {
                neighbours.forEach(GoField::removeStone);
            }
            neighbours.clear();
        }
        //left
        if (col > 0) {
            findNeighboursOfSameColor(row, col - 1, neighbours, fields[row][col - 1].getStone());
            if (!chainContainsLiberties(neighbours)) {
                neighbours.forEach(GoField::removeStone);
            }
            neighbours.clear();
        }

        //center
        findNeighboursOfSameColor(row, col, neighbours, fields[row][col].getStone());
        if (!chainContainsLiberties(neighbours)) {
            neighbours.forEach(GoField::removeStone);
        }
        neighbours.clear();
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
        if(fields[row][col].isPreset()) return;
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
    private boolean chainContainsLiberties(List<GoField> chain) {
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

    //todo Not yet implemented
    public void deleteLastMove() {
        System.out.println("Delete last move!");
    }

    public void switchPlayer() {
        if (currentPlayer == Stone.BLACK) {
            currentPlayer = Stone.WHITE;
            gameState = GameState.WHITE_TURN;
        } else if (currentPlayer == Stone.WHITE) {
            currentPlayer = Stone.BLACK;
            gameState = GameState.BLACK_TURN;
        }
    }

    public void reset() {
        gameState = GameState.RESET;
        initModel();
        initHandicapFields();

        for (GameListener listener : listeners) {
            listener.resetGame(new GameEvent(this, gameState));
        }
    }

    public void pass() {
        if(prevPassed) {
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

    private void gameEnds() {
        calculateScores();

        if(pointsBlack == pointsWhite) {
            gameState = GameState.DRAW;
        } else {
            gameState = pointsWhite > pointsBlack ? GameState.WHITE_WON : GameState.BLACK_WON;
        }

        for (GameListener listener : listeners) {
            listener.gameEnded(new GameEvent(this, gameState));
        }
    }

    private void calculateScores() {

    }

    public void playerResigned() {
        gameState = currentPlayer == Stone.BLACK ? GameState.WHITE_WON : GameState.BLACK_WON;
    }

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
    }

    public void addGameListener(GameListener l) {
        listeners.add(l);
    }

    public void removeGameListener(GameListener l) {
        listeners.remove(l);
    }

    public List<GameListener> getGameListeners() {
        return this.listeners;
    }

    public void setGameListeners(List<GameListener> listeners) {
        this.listeners.addAll(listeners);
    }
    //endregion
}
