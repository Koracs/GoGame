package com.gogame.controller;

import com.gogame.listener.GameState;
import com.gogame.model.*;
import com.gogame.view.GoBoardView;

import javafx.scene.input.MouseEvent;

public class GoBoardController {
    //region Fields
    // MVC variables
    private GoBoardModel model;
    private GoBoardView view;

    // Constants
    private final String FILENAME = "gamedata_";
    //endregion

    // Constructor
    public GoBoardController(GoBoardModel model, GoBoardView view) {
        this.model = model;
        this.view = view;
    }

    //region Getter/Setter
    public void setModel(GoBoardModel model) {
        this.model = model;
    }

    public void setView(GoBoardView view) {
        this.view = view;
    }
    //endregion

    //region Methods
    public void mouseClicked(MouseEvent e) {
        int row = (int) Math.round((e.getY()) / view.getScale() - 1);
        int col = (int) Math.round((e.getX()) / view.getScale() - 1);

        model.makeMove(row, col);

    }

    public void resetModel() {
        model.reset();
    }

    public void passPlayer() {
        model.pass();
    }

    //endregion
}
