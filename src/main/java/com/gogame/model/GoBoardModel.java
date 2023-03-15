package com.gogame.model;

import com.gogame.view.*;

public class GoBoardModel {
    private GoBoardView view;

    public void registerView(GoBoardView view) {
        this.view = view;
    }
}
