package com.gogame.view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class GoBoard extends Application {

    private final int BOARD_SIZE = 19;
    private final int TILE_SIZE = 50;

    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();

        Rectangle background = new Rectangle(TILE_SIZE,TILE_SIZE,(BOARD_SIZE-1)*TILE_SIZE,(BOARD_SIZE-1)*TILE_SIZE);
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
        int[] dotPositions = new int[] { 3, 9, 15 };
        for (int i : dotPositions) {
            for (int j : dotPositions) {
                Circle dot = new Circle(TILE_SIZE / 8.0);
                dot.setFill(Color.BLACK);
                dot.setCenterX((j + 1) * TILE_SIZE);
                dot.setCenterY((i + 1) * TILE_SIZE);
                root.getChildren().add(dot);
            }
        }

        // Add coordinates to the board //todo divide into 2 methods
        /*for (int i = 0; i < BOARD_SIZE; i++) {
            int x = i + 1;
            int y = BOARD_SIZE - i;
            addCoordinate(root, x, 0, y);
            addCoordinate(root, x, BOARD_SIZE + 1, y);
            addCoordinate(root, 0, x, y);
            addCoordinate(root, BOARD_SIZE + 1, x, y);
        }*/

        // Set the scene
        Scene scene = new Scene(root, (BOARD_SIZE + 2) * TILE_SIZE, (BOARD_SIZE + 2) * TILE_SIZE);
        stage.setTitle("Go Game Gruppe 5");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void addCoordinate(Group group, int x, int y, int value) {
        StackPane coordinate = new StackPane();
        coordinate.setTranslateX(x * TILE_SIZE);
        coordinate.setTranslateY(y * TILE_SIZE);

        Text text;
        if (y == 0 || y == BOARD_SIZE + 1) {
            // Add letter for horizontal coordinates
            char c = (char) (value + 64);
            text = new Text(String.valueOf(c));
        } else {
            // Add number for vertical coordinates
            text = new Text(String.valueOf(value));
        }
        text.setFont(Font.font(TILE_SIZE / 2.0));
        coordinate.getChildren().add(text);

        group.getChildren().add(coordinate);
    }
}