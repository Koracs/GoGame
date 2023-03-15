package com.gogame.view;

import com.gogame.adapter.*;
import com.gogame.controller.*;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class GoBoardView {

    private GoBoardAdapter adapter;
    private Scene scene;

    private final int BOARD_SIZE = 19;
    private final int TILE_SIZE = 50;

    public GoBoardView(){
        scene = createScene();
    }

    public Scene getScene() {
        return scene;
    }

    private Scene createScene(){
        Group root = new Group();

        Rectangle background = new Rectangle(TILE_SIZE, TILE_SIZE, (BOARD_SIZE - 1) * TILE_SIZE, (BOARD_SIZE - 1) * TILE_SIZE);
        background.setFill(Color.valueOf("#DDBB6D"));
        root.getChildren().add(background);

        // Add horizontal lines to the board
        for (int i = 1; i <= BOARD_SIZE; i++) {
            Line line = new Line(TILE_SIZE, TILE_SIZE * i, TILE_SIZE * BOARD_SIZE, TILE_SIZE * i);
            root.getChildren().add(line);
        }

        // Add vertical lines to the board
        for (int i = 1; i <= BOARD_SIZE; i++) {
            Line line = new Line(TILE_SIZE * i, TILE_SIZE, TILE_SIZE * i, TILE_SIZE * BOARD_SIZE);
            root.getChildren().add(line);
        }

        // Add dots to the board
        int[] dotPositions = new int[]{3, 9, 15};
        for (int i : dotPositions) {
            for (int j : dotPositions) {
                Circle dot = new Circle(TILE_SIZE / 8.0);
                dot.setFill(Color.BLACK);
                dot.setCenterX((j + 1) * TILE_SIZE);
                dot.setCenterY((i + 1) * TILE_SIZE);
                root.getChildren().add(dot);
            }
        }

        EventHandler<MouseEvent> clickHandler = mouseEvent -> adapter.handleMouseClickEvent(mouseEvent);


        // Set the scene
        Scene scene = new Scene(root, (BOARD_SIZE + 2) * TILE_SIZE, (BOARD_SIZE + 2) * TILE_SIZE);
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);

        return scene;
    }

    public void setActionListener(GoBoardController controller) {
        this.adapter = new GoBoardAdapter(controller, this);
    }
}