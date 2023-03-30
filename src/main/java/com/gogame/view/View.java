package com.gogame.view;

import javafx.scene.layout.BorderPane;

abstract class View {
    public abstract BorderPane getPane();
    protected abstract void drawScene();
}
