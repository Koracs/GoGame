package com.gogame.model;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.listener.GameState;
import com.gogame.view.*;
import javafx.scene.Parent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GoBoardModel {
    //region Fields
    // MVC variables

    // Model variables
    private int size;
    private Stone currentPlayer; //todo currentplayer and gameState both needed?
    private GameState gameState;

    private GoField[][] fields;

    private final List<GameListener> listeners;
    //endregion

    public GoBoardModel(int size) {
        this.size = size;
        currentPlayer = Stone.BLACK;
        listeners = new LinkedList<>();

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
    }

    //region Getter/Setter
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
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public GoField[][] getFields() {
        return fields;
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

    public void addGameListener(GameListener l) {
        listeners.add(l);
    }

    public void removeGameListener(GameListener l) {
        listeners.remove(l);
    }
}
