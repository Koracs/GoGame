package com.gogame.view;

import com.gogame.controller.*;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import com.gogame.model.GoField;
import com.gogame.model.Stone;

import javafx.geometry.Pos;
import javafx.scene.Group;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GoBoardView extends Pane { //todo interface for views? (registerView())
    //region Fields
    // Pane of this class
    private final GoBoardController controller;
    private final int boardSize;
    private double tileSize;
    private final GoBoardModel model;


    public GoBoardView(GoBoardModel model) {
        this.boardSize = model.getSize();
        this.model = model;
        controller = new GoBoardController(model, this);

        setMinSize(500,500);
        setPrefSize(600,600);
        widthProperty().addListener(e -> {
            setScale();
            draw();
        });
        heightProperty().addListener(e -> {
            setScale();
            draw();
        });

        model.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {
                draw();
            }

            @Override
            public void resetGame(GameEvent event) {
                draw();
            }

            @Override
            public void playerPassed(GameEvent event) {

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

    Circle hover;
    public void drawHover(MouseEvent e) {
        try {
            getChildren().remove(hover);
            int row = controller.getNearestRow(e.getY());
            int col = controller.getNearestCol(e.getX());

            hover = new Circle((col+1)*getScale(),(row+1)*getScale(),tileSize/2.5);

            String hoverColor;

            if(model.getField(row,col).isEmpty()) hoverColor = model.getCurrentPlayer() == Stone.BLACK ? "rgba(0,0,0,0.5)" : "rgba(255,255,255,0.5)";
            else hoverColor = "rgba(255,0,0,0.5)";

            hover.setFill(Color.web(hoverColor));
            getChildren().add(hover);
        } catch (ArrayIndexOutOfBoundsException ignore){
        }

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
            case BLACK, WHITE -> tileSize / 2.5;
            case PRESET -> tileSize / 6;
            default -> 0;
        };

        Circle circle = new Circle(centerX, centerY, radius);
        circle.setFill(stone.getColor());

        return circle;
    }

    public void setScale() {
        double scale = Math.min(getWidth(), getHeight());
        this.tileSize = scale / (boardSize + 1);
    }

    public double getScale() {
        return this.tileSize;
    }

    public GoBoardController getController() {
        return controller;
    }

}