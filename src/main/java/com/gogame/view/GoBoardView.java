package com.gogame.view;

import com.gogame.controller.*;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import com.gogame.model.GoField;
import com.gogame.model.Stone;

import javafx.geometry.Pos;
import javafx.scene.Group;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GoBoardView extends Pane implements GameListener{ //todo interface for views? (registerView())
    //region Fields
    // Pane of this class
    private final GoBoardController controller;
    private int boardSize;
    private double tileSize;
    private GoBoardModel model;
    private GoField[][] fields;
    private Circle hover;
    private int currentRow;
    private int currentCol;

    private final boolean[][] marked;
    //endregion

    // Constructor
    public GoBoardView(GoBoardModel model) {
        this.boardSize = model.getSize();
        this.model = model;
        controller = new GoBoardController(model, this);

        fields = model.getFields();
        marked = new boolean[model.getSize()][model.getSize()];

        setPrefSize(600,600);
        
        widthProperty().addListener(e -> {
            setScale();
            draw();
        });
        heightProperty().addListener(e -> {
            setScale();
            draw();
        });

        model.addGameListener(this);
        draw();
    }

    //region Getter/Setter

    public void setModel(GoBoardModel model) {
        this.model = model;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }


    public void setScale() {
        double scale = Math.min(getWidth(), getHeight());
        this.tileSize = scale / (boardSize + 1);
    }

    public double getScale() {
        return this.tileSize;
    }

    public GoBoardController getController() {
        return this.controller;
    }

    //endregion


    //region Methods

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

    @Override
    public void gameEnded(GameEvent event) {

    }

    public void draw() {
        getChildren().clear();
        drawBoard();
        drawCoordinates();
        drawStones();
        drawMarkings();
    }

    public void moveHoverMouse(MouseEvent e) {
        try {
            currentRow = controller.getNearestRow(e.getY());
            currentCol = controller.getNearestCol(e.getX());

            drawHover();
        } catch (ArrayIndexOutOfBoundsException ignore) {
        }
    }

    public void moveHoverKeyboard(KeyEvent e) {
        switch (e.getCode()) {
            case W, UP -> {
                if (currentRow >= 1) currentRow -= 1;
            }
            case S, DOWN -> {
                if (currentRow < (boardSize - 1)) currentRow += 1;
            }
            case A, LEFT -> {
                if (currentCol >= 1) currentCol -= 1;
            }
            case D, RIGHT -> {
                if (currentCol < (boardSize - 1)) currentCol += 1;
            }
        }

        drawHover();
    }

    public void setStoneKeyboard() {
        controller.makeMove(currentRow, currentCol);
        drawHover();
    }

    private void drawHover() {
        if (currentRow < 0 || currentCol < 0 || currentRow >= boardSize || currentCol >= boardSize) {
            return;
        }

        getChildren().remove(hover);

        hover = new Circle((currentCol + 1) * getScale(), (currentRow + 1) * getScale(), tileSize / 2.5);

        String hoverColor;

        if (model.getField(currentRow, currentCol).isEmpty())
            hoverColor = model.getCurrentPlayer() == Stone.BLACK ? "rgba(0,0,0,0.5)" : "rgba(255,255,255,0.5)";
        else hoverColor = "rgba(255,0,0,0.5)";

        hover.setFill(Color.web(hoverColor));
        getChildren().add(hover);
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
        fields = model.getFields();
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


    public void setMarking(int row, int col) {
        marked[row][col] = !marked[row][col];
    }
    private void drawMarkings() {
        for (int row = 0; row < marked.length; row++) {
            for (int col = 0; col < marked[row].length; col++) {
                if(marked[row][col]){
                    Circle marking = new Circle((col + 1) * tileSize,(row + 1) * tileSize, tileSize/4);
                    marking.setFill(Color.TRANSPARENT);

                    if(fields[row][col].getStone() == Stone.BLACK) marking.setStroke(Color.WHITE);
                    else marking.setStroke(Color.BLACK);
                    getChildren().add(marking);
                }
            }
        }
    }
    //endregion
}