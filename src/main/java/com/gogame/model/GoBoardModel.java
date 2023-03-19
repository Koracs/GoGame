package com.gogame.model;

import com.gogame.view.*;
import javafx.scene.Parent;

public class GoBoardModel {
    private GoBoardView view;

    private int size;

    private GoField[][] fields;

    public GoBoardModel(int size){
        this.size = size;
        //initModel();
    }

    private void initModel() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j] = new GoField();
            }
        }
    }

    public void registerView(GoBoardView view) {
        this.view = view;
    }
}
