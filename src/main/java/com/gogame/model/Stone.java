package com.gogame.model;

import javafx.scene.paint.Color;

public enum Stone {
    BLACK(Color.BLACK), WHITE(Color.WHITE), NONE(Color.TRANSPARENT), PRESET(Color.BLACK);

    private final Color color;

    private Stone(Color color){ this.color = color;}

    public Color getColor(){return color;}
}
