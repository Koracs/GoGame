package com.gogame.view;

import com.gogame.controller.*;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import com.gogame.model.GoField;
import com.gogame.model.Stone;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GoBoardView extends Parent { //todo interface for views? (registerView())
    //region Fields
    // Pane of this class
    private GoBoardController controller;
    private final int boardSize;
    private double tileSize;
    private GoBoardModel model;


    public GoBoardView(GoBoardModel model) {
        this.boardSize = model.getSize();
        this.model = model;
        controller = new GoBoardController(model, this);

        model.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {
                draw();
            }

            @Override
            public void resetGame(GameEvent event) {
                draw();
            }
        });


        draw();
    }

    public void draw() {
        getChildren().clear();
        drawBoard();
        drawCoordinates();
        drawStones();
    }


    private void drawBoard() {
        //draw background rectangle
        Rectangle background = new Rectangle(0, 0, (boardSize + 1) * tileSize, (boardSize + 1) * tileSize);
        background.setFill(Color.valueOf("#DDBB6D"));
        getChildren().add(background);

        //Add horizontal lines to the board
        for (int i = 1; i <= boardSize; i++) {
            Line line = new Line(tileSize, tileSize * i, tileSize * boardSize, tileSize * i);
            if (i == 1 || i == boardSize) line.setStrokeWidth(3);
            getChildren().add(line);
        }

        //Add vertical lines to the board
        for (int i = 1; i <= boardSize; i++) {
            Line line = new Line(tileSize * i, tileSize, tileSize * i, tileSize * boardSize);
            if (i == 1 || i == boardSize) line.setStrokeWidth(3);
            getChildren().add(line);
        }
    }

    private void drawCoordinates() {
        Group coordinates = new Group();
        //horizontal coordinates
        for (int i = 1; i <= boardSize; i++) {
            drawCoordinate(coordinates, i, (i * tileSize) - (tileSize / 2), 0);
            drawCoordinate(coordinates, i, (i * tileSize) - (tileSize / 2), (boardSize * tileSize));
        }
        //vertical coordinates
        for (int i = 1; i <= boardSize; i++) {
            drawCoordinate(coordinates, boardSize + 1 - i, 0, (i * tileSize) - (tileSize / 2));
            drawCoordinate(coordinates, boardSize + 1 - i, (boardSize * tileSize), (i * tileSize) - (tileSize / 2));
        }

        getChildren().add(coordinates);
    }


    private void drawCoordinate(Group parent, int value, double x, double y) {
        StackPane coordinate = new StackPane();
        coordinate.setAlignment(Pos.CENTER);
        coordinate.setTranslateX(x);
        coordinate.setTranslateY(y);
        coordinate.setMinWidth(tileSize);
        coordinate.setMinHeight(tileSize);
        //coordinate.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Text text; //add Number or Character depending on position
        if (y == 0 || y == (boardSize * tileSize)) {
            text = new Text(String.valueOf((char) (value + 64)));
        } else {
            text = new Text(String.valueOf(value));
        }
        text.setFont(Font.font(tileSize / 2.0));
        coordinate.getChildren().add(text);
        parent.getChildren().add(coordinate);
    }

    private void drawStones() {
        GoField[][] fields = model.getFields();
        for (int row = 0; row < fields.length; row++) {
            for (int col = 0; col < fields[row].length; col++) {
                if (fields[row][col].getStone() != Stone.NONE) {
                    Circle stone = createStone((col + 1) * tileSize,
                            (row + 1) * tileSize, fields[row][col].getStone(), tileSize);

                    getChildren().add(stone);
                }
            }
        }
    }

    private Circle createStone(double centerX, double centerY, Stone stone, double tileSize) {
        double radius = switch (stone) {
            case BLACK, WHITE -> tileSize / 4;
            case PRESET -> tileSize / 8;
            default -> 0;
        };

        Circle circle = new Circle(centerX, centerY, radius);
        circle.setFill(stone.getColor());
        //circle.setStroke(Color.BLACK);

        return circle;
    }

    public void setScale(double scale) {
        this.tileSize = scale / (boardSize + 1);
    }

    public double getScale() {
        return this.tileSize;
    }

    public GoBoardController getController() {
        return controller;
    }

}