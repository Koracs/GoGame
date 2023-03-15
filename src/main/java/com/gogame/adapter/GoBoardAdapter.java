package com.gogame.adapter;

import com.gogame.controller.*;
import com.gogame.view.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;


public class GoBoardAdapter {
    private GoBoardController controller;
    private GoBoardView view;

    public GoBoardAdapter(GoBoardController controller, GoBoardView view) {
        this.controller = controller;
        this.view = view;
    }

    public void handleMouseClickEvent(MouseEvent e) {
        System.out.println("adapter clicked");
        controller.mouseClicked(e);
    }
}
