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

    private GoField[][] fields;

    private final List<GameListener> listeners;
    //endregion
    private StringBuilder gameDataStorage;

    public GoBoardModel(int size, double komi, int handicap) {
        this.size = size;
        this.komi = komi;
        this.handicap = handicap;
        currentPlayer = Stone.BLACK;
        listeners = new LinkedList<>();
        gameState = GameState.GAME_START;

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
        this.gameDataStorage = new StringBuilder(this.size + ";" + this.handicap + ";" + this.komi + "\n");
    }

    public int getSize() {
        return size;
    }

    public GoField[][] getFields() {
        return fields;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getGameDataStorage() {
        return gameDataStorage.toString();
    }

    public void makeMove(int row, int col) {
        if (gameState == GameState.PLACE_HANDICAP) {
            makeHandicapMove(row, col);
            return;
        }

        if (fields[row][col].isEmpty()) {
            fields[row][col].setStone(currentPlayer);
            switchPlayer();

            checkCapture(row, col);

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

    private List<GoField> neighbours;

    /**
     * Check field or surrounding fields are captured
     * @param row
     * @param col
     */
    private void checkCapture(int row, int col) { //todo implement scoring
        neighbours = new ArrayList<>();

        //center
        findNeighboursOfSameColor(row, col, fields[row][col].getStone());
        if (!chainContainsLiberties()) {
            neighbours.forEach(GoField::removeStone);
        }
        neighbours.clear();
        //top
        findNeighboursOfSameColor(row-1, col, fields[row-1][col].getStone());
        if (!chainContainsLiberties()) {
            neighbours.forEach(GoField::removeStone);
        }
        neighbours.clear();
        //right
        findNeighboursOfSameColor(row, col+1, fields[row][col+1].getStone());
        if (!chainContainsLiberties()) {
            neighbours.forEach(GoField::removeStone);
        }
        neighbours.clear();
        //bottom
        findNeighboursOfSameColor(row+1, col, fields[row+1][col].getStone());
        if (!chainContainsLiberties()) {
            neighbours.forEach(GoField::removeStone);
        }
        neighbours.clear();
        //left
        findNeighboursOfSameColor(row, col-1, fields[row][col-1].getStone());
        if (!chainContainsLiberties()) {
            neighbours.forEach(GoField::removeStone);
        }
        neighbours.clear();
    }

    private void findNeighboursOfSameColor(int row, int col, Stone currentColor) {
        if (col < 0 || col >= fields.length || row < 0 || row >= fields.length) {
            return;
        }
        if (neighbours.contains(fields[row][col])) return;

        if (fields[row][col].getStone().equals(currentColor)) neighbours.add(fields[row][col]);
        else return;

        //top
        if (row > 0) findNeighboursOfSameColor(row - 1, col, currentColor);
        //right
        if (col < fields.length - 1) findNeighboursOfSameColor(row, col + 1, currentColor);
        //bottom
        if (row < fields.length - 1) findNeighboursOfSameColor(row + 1, col, currentColor);
        //left
        if (col > 0) findNeighboursOfSameColor(row, col - 1, currentColor);
    }

    private boolean chainContainsLiberties() {
        for (GoField field : neighbours) {
            int row = field.getRow();
            int col = field.getCol();

            if (row > 0 && fields[row - 1][col].isEmpty()) return true; //top
            if (col < fields.length - 1 && fields[row][col + 1].isEmpty()) return true; //right
            if (row < fields.length - 1 && fields[row + 1][col].isEmpty()) return true; //bottom
            if (col > 0 && fields[row][col - 1].isEmpty()) return true; //left
        }

        return false;
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
        gameState = currentPlayer == Stone.BLACK ? GameState.BLACK_PASSED : GameState.WHITE_PASSED;
        gameDataStorage.append(gameState.toString() + "\n");
        switchPlayer();
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

    public void storeData(String s) {
        this.gameDataStorage.append(s);
    }

    public void addGameListener(GameListener l) {
        listeners.add(l);
    }

    public void removeGameListener(GameListener l) {
        listeners.remove(l);
    }

    //endregion
}
