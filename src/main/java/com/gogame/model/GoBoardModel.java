package com.gogame.model;

import com.gogame.listener.GameListener;
import com.gogame.view.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GoBoardModel {
    private GoBoardView view;
    private int size;
    private Stone currentPlayer;
    private GoField[][] fields;

    private final List<GameListener> listeners;


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


    public void registerView(GoBoardView view) {
        this.view = view;
    }

    public void makeMove(int x, int y) {
        if (fields[y][x].isEmpty()) {
            fields[y][x].setStone(currentPlayer);
            switchPlayer();
            //System.out.println(Arrays.deepToString(fields));
            for (GameListener listener : listeners) {
                listener.moveCompleted();
            }
        }

    }

    private void switchPlayer() {
        if (currentPlayer == Stone.BLACK) currentPlayer = Stone.WHITE;
        else if (currentPlayer == Stone.WHITE) currentPlayer = Stone.BLACK;
    }

    public void reset() {
        initModel();
        initHandicapFields();

        for (GameListener listener : listeners) {
            listener.resetGame();
        }
    }

    public void pass() {
        switchPlayer();
    }

    public void printModel(){
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                System.out.print(fields[y][x].toString());
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
