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

    private GoBoardController controller;


    private Group view;

    private int BOARD_SIZE = 19;
    private int TILE_SIZE = 50;
    public GoBoardView(){
        view = createView();
    }

    public Group getView() {
        return view;
    }

    private Group createView() {
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

        EventHandler<MouseEvent> clickHandler = mouseEvent -> controller.mouseClicked(mouseEvent);

        //todo let scene react to tile size changes
        EventHandler<MouseEvent> tileSizeChanger = mouseEvent -> {
            TILE_SIZE++;
            System.out.println(TILE_SIZE);
        };

        root.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
        root.addEventHandler(MouseEvent.MOUSE_CLICKED,tileSizeChanger);

        return root;
    }

    public void setActionListener(GoBoardController controller) {
        this.controller = controller;
    }

}