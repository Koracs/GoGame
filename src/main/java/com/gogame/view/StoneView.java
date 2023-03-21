package com.gogame.view;

import com.gogame.model.Stone;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StoneView extends Circle { //todo constructor instead of static?
    static Circle createStone(double centerX, double centerY, Stone stone, double tileSize){
        double radius = switch (stone) {
            case BLACK, WHITE -> tileSize / 4;
            case PRESET -> tileSize / 8;
            default -> 0;
        };

        Circle circle = new Circle(centerX,centerY,radius);
        circle.setFill(stone.getColor());
        //circle.setStroke(Color.BLACK);

        return circle;
    }
}
