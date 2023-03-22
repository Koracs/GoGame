package com.gogame.model;

import com.gogame.view.*;
import javafx.scene.Parent;

import java.util.Arrays;

public class GoBoardModel {
    //region Fields
    // MVC variables
    private GoBoardView view;

    // Model variables
    private int size;
    private Stone currentPlayer;
    private GoField[][] fields;

    //endregion

    // Constructor
    public GoBoardModel(int size){
        this.size = size;
        currentPlayer = Stone.BLACK;
        initModel();
    }

    //region Getter/Setter
    public Stone getCurrentPlayer() {
        return this.currentPlayer;
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
    //endregion

    //region Methods
    private void initModel() {
        fields = new GoField[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j] = new GoField();
            }
        }
    }


    public void registerView(GoBoardView view) {
        this.view = view;
    }

    public void makeMove(int x, int y){
        if(fields[y][x].isEmpty()) {
            fields[y][x].setStone(currentPlayer);
            switchPlayer();
            //System.out.println("stone set");
            System.out.println(Arrays.deepToString(fields));
        }
    }

    private void switchPlayer(){
        if (currentPlayer == Stone.BLACK) currentPlayer = Stone.WHITE;
        else if (currentPlayer == Stone.WHITE) currentPlayer = Stone.BLACK;
    }

    public void reset() {
        initModel();
    }

    public void pass() {
        switchPlayer();
    }
    //endregion
}
