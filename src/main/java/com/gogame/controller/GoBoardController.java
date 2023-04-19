package com.gogame.controller;

import com.gogame.model.*;
import com.gogame.view.GoBoardView;

import javafx.scene.input.MouseEvent;

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

    public void setViewModel(GoBoardModel model) {
        this.model = model;
        this.view.setModel(model);
    }

    public void setView(GoBoardView view) {
        this.view = view;
    }

    public int getSize() {
        return model.getSize();
    }

    public int getHandicap() {
        return model.getHandicap();
    }

    public double getKomi() {
        return model.getKomi();
    }

    public GoBoardView getView() {
        return view;
    }

    public GoBoardModel getModel() {
        return model;
    }

    //endregion

    //region Methods
    public void mouseClicked(MouseEvent e) {
        model.makeMove(getNearestRow(e.getY()), getNearestCol(e.getX()));

    }

    public int getNearestRow(double y){
        return (int) Math.round(y / view.getScale() - 1);
    }

    public int getNearestCol(double x){
        return (int) Math.round(x / view.getScale() - 1);
    }

    public void resetModel() {
        model.reset();
    }

    public void passPlayer() {
        model.pass();
    }

    public void makeMove(int row, int col) {
        model.makeMove(row, col);
    }
    //endregion
}
