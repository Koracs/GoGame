package com.gogame.controller;

import com.gogame.model.*;
import com.gogame.view.GoBoardView;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class GoBoardController {

    private GoBoardModel model;
    private GoBoardView view;

    public void setModel(GoBoardModel model) {
        this.model = model;
    }

    public void setView(GoBoardView view) { this.view = view;}

    public void mouseClicked(MouseEvent e) {
        System.out.println("controller clicked at X: " + e.getX() + " Y: " + e.getY());

    }
}
