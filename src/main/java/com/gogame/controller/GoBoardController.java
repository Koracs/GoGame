package com.gogame.controller;

import com.gogame.model.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class GoBoardController {

    private GoBoardModel model;

    public void setModel(GoBoardModel model) {
        this.model = model;
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("controller clicked at X: " + e.getX() + " Y: " + e.getY());

    }
}
