package com.gogame.model;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.listener.GameState;
import com.gogame.view.*;
import javafx.scene.Parent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GoBoardModel {
    //region Fields
    // MVC variables

    // Model variables
    private int size;
    private int handicap;
    private double komi;
    private Stone currentPlayer; //todo curremtplayer and gameState both needed?
    private GameState gameState;

    private GoField[][] fields;

    private final List<GameListener> listeners;
    private StringBuilder gameDataStorage;


    public GoBoardModel(int size) {
        this.size = size;
        this.handicap = 0;
        this.komi = 0.5;
        currentPlayer = Stone.BLACK;
        listeners = new LinkedList<>();

        initModel();
        initHandicapFields();
    }

    private void initHandicapFields() {
        int[] handicapFields = new int[0];
        switch (size) {
            case 19 -> {
                handicapFields = new int[]{3, 9, 15};
            }
            case 13 -> {
                handicapFields = new int[]{3, 6, 9};
            }
            case 9 -> {
                handicapFields = new int[]{2, 6};
                fields[4][4].setStone(Stone.PRESET);
            }
        }
        for (int i : handicapFields) {
            for (int j : handicapFields) {
                fields[i][j].setStone(Stone.PRESET);
            }
        }
    }

    //region Getter/Setter
    public Stone getCurrentPlayer() {
        return this.currentPlayer;
    }


    private void initModel() {
        fields = new GoField[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                fields[y][x] = new GoField();
            }
        }
        this.gameDataStorage = new StringBuilder(this.size + ";" + this.handicap + ";" + this.komi + "\n");
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

    public String getGameDataStorage() {
        return gameDataStorage.toString();
    }

    //endregion

    //region Methods
    public void makeMove(int x, int y) {
        if (fields[y][x].isEmpty()) {
            fields[y][x].setStone(currentPlayer);
            switchPlayer();

            for (GameListener listener : listeners) {
                listener.moveCompleted(new GameEvent(this, gameState, x, y));
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
        gameDataStorage.append(gameState.toString() + "\n");
        switchPlayer();
    }

    public void printModel() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                System.out.print(fields[y][x].toString());
            }
            System.out.println();
        }
    }

    public void storeData(String s) {
        this.gameDataStorage.append(s );
    }

    public void addGameListener(GameListener l) {
        listeners.add(l);
    }

    public void removeGameListener(GameListener l) {
        listeners.remove(l);
    }

    //endregion
}
