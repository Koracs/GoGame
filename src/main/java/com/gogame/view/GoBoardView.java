package com.gogame.view;

import com.gogame.adapter.*;
import com.gogame.controller.*;

import com.gogame.model.GoBoardModel;
import com.gogame.model.GoField;
import com.gogame.model.Stone;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GoBoardView extends Parent {

    private GoBoardController controller;

    private int BOARD_SIZE;
    private double TILE_SIZE;
    private GoBoardModel model;

    public void setScale(double scale){
        this.TILE_SIZE = scale/(BOARD_SIZE+1);
    }

    public double getScale(){
        return this.TILE_SIZE;
    }


    public GoBoardView(int size){
        this.BOARD_SIZE = size;
        EventHandler<MouseEvent> clickHandler = mouseEvent -> controller.mouseClicked(mouseEvent);

        addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
    }


    public void draw() {
        getChildren().clear();
        drawBoard();
        drawCoordinates();
        drawStones();
    }



    private void drawBoard() {
        //draw background rectangle
        Rectangle background = new Rectangle(0, 0, (BOARD_SIZE + 1) * TILE_SIZE, (BOARD_SIZE + 1) * TILE_SIZE);
        background.setFill(Color.valueOf("#DDBB6D"));
        getChildren().add(background);

        // Add horizontal lines to the board
        for (int i = 1; i <= BOARD_SIZE; i++) {
            Line line = new Line(TILE_SIZE, TILE_SIZE * i, TILE_SIZE * BOARD_SIZE, TILE_SIZE * i);
            if(i == 1 || i == BOARD_SIZE) line.setStrokeWidth(3);
            getChildren().add(line);
        }

        // Add vertical lines to the board
        for (int i = 1; i <= BOARD_SIZE; i++) {
            Line line = new Line(TILE_SIZE * i, TILE_SIZE, TILE_SIZE * i, TILE_SIZE * BOARD_SIZE);
            if(i == 1 || i == BOARD_SIZE) line.setStrokeWidth(3);
            getChildren().add(line);
        }

        // Add dots to the board
        int[] dotPositions = new int[]{3, 9, 15};
        for (int i : dotPositions) {
            for (int j : dotPositions) {
                Circle dot = new Circle(TILE_SIZE / 8.0);
                dot.setFill(Color.BLACK);
                dot.setCenterX((j + 1) * TILE_SIZE);
                dot.setCenterY((i + 1) * TILE_SIZE);
                getChildren().add(dot);
            }
        }
    }

    private void drawCoordinates() {
        Group coordinates = new Group();
        //horizontal coordinates
        for (int i = 1; i <= BOARD_SIZE; i++) {
            drawCoordinate(coordinates,i,(i * TILE_SIZE)-(TILE_SIZE/2),0);
            drawCoordinate(coordinates,i,(i * TILE_SIZE)-(TILE_SIZE/2),(BOARD_SIZE*TILE_SIZE));
        }
        //vertical coordinates
        for (int i = 1; i <= BOARD_SIZE; i++) {
            drawCoordinate(coordinates,BOARD_SIZE+1-i,0,(i * TILE_SIZE)-(TILE_SIZE/2));
            drawCoordinate(coordinates,BOARD_SIZE+1-i,(BOARD_SIZE*TILE_SIZE),(i * TILE_SIZE)-(TILE_SIZE/2));
        }

        getChildren().add(coordinates);
    }

    private void drawCoordinate(Group parent,int value, double x, double y){
        StackPane coordinate = new StackPane();
        coordinate.setAlignment(Pos.CENTER);
        coordinate.setTranslateX(x);
        coordinate.setTranslateY(y);
        coordinate.setMinWidth(TILE_SIZE);
        coordinate.setMinHeight(TILE_SIZE);
        //coordinate.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Text text; //add Number or Character depending on position
        if (y == 0 || y == (BOARD_SIZE * TILE_SIZE)) {
            text = new Text(String.valueOf((char) (value + 64)));
        } else {
            text = new Text(String.valueOf(value));
        }
        text.setFont(Font.font(TILE_SIZE / 2.0));
        coordinate.getChildren().add(text);
        parent.getChildren().add(coordinate);
    }

    private void drawStones() {
        GoField[][] fields = model.getFields();
        for (int x = 1; x < fields.length; x++) {
            for (int y = 1; y < fields[x].length; y++) {
                if(fields[y][x].getStone() != Stone.NONE) {
                    Circle stone = new Circle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE/4);
                    if(fields[y][x].getStone() == Stone.BLACK) {
                        stone.setFill(Color.BLACK);
                    } else {
                        stone.setFill(Color.WHITE);
                        stone.setStroke(Color.WHITE);
                    }
                    getChildren().add(stone);
                }
            }
        }
    }


    public void setActionListener(GoBoardController controller) {
        this.controller = controller;
        controller.setView(this);
    }

    public void setModel(GoBoardModel model){
        this.model = model;
    }

}