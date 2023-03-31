package com.gogame.model;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.listener.GameState;

import java.util.LinkedList;
import java.util.List;

public class
GoBoardModel {
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

        if (handicap != 0) handicapMode();
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
                fields[row][col] = new GoField();
            }
        }
        this.gameDataStorage = new StringBuilder(this.size + ";" + this.handicap + ";" + this.komi + "\n");
    }

    private void handicapMode() {
        gameState = GameState.PLACE_HANDICAP;
        currentPlayer = Stone.BLACK;
        handicapCount = handicap;
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
        if (fields[row][col].isEmpty()) {
            fields[row][col].setStone(currentPlayer);
            switchPlayer();

            for (GameListener listener : listeners) {
                listener.moveCompleted(new GameEvent(this, gameState, row, col));
            }
        }
    }

    public void makeHandicapMove(int row, int col) {
        if (handicapCount == 0) {
            gameState = GameState.WHITE_TURN;
            currentPlayer = Stone.WHITE;

            return;
        }

        if (fields[row][col].isPreset()) {
            fields[row][col].setStone(currentPlayer);

            for (GameListener listener : listeners) {
                listener.moveCompleted(new GameEvent(this, gameState, row, col));
            }
            handicapCount -= 1;
        }
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
                System.out.print(fields[row][col].toString());
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
