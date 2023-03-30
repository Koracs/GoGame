package com.gogame.controller;

import com.gogame.model.*;
import com.gogame.view.GoBoardView;
import com.gogame.view.WinScreenView;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GoBoardController {
    //region Fields
    // MVC variables
    private GoBoardModel model;
    private GoBoardView view;
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
        int row = (int)Math.round((e.getY()) / view.getScale()-1);
        int col = (int)Math.round((e.getX()) / view.getScale()-1);

        model.makeMove(row,col);
    }

    public void resetModel(){
        model.reset();
    }

    public void passPlayer() {
        model.pass();
    }

    //endregion
}
