package com.gogame.view;

import com.gogame.adapter.*;
import com.gogame.controller.*;

import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import com.gogame.model.GoField;
import com.gogame.model.Stone;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GoBoardView extends Parent {
    //region Fields
    // Pane of this class
    private BorderPane pane;

    private GoBoardController controller;
    private int boardSize;
    private double tileSize;
    private GoBoardModel model;

        // -------------------- init ---------------------
        model = new GoBoardModel(this, size);
        controller = new GoBoardController(model, this);
        this.setActionListener(controller);
        drawScene();
    }

    //region Getter/Setter
    public void setScale(double scale){
        this.TILE_SIZE = scale/(model.getSize()+1);
    }

    public double getScale(){
        return this.TILE_SIZE;
    }

    public BorderPane getPane() {
        return this.pane;
    }

    public void setActionListener(GoBoardController controller) {
        this.controller = controller;
        controller.setView(this);
    }

    public void setModel(GoBoardModel model){
        this.model = model;
    }
    //endregion

    //region Methods
    private void drawScene() {
        pane = new BorderPane();
        pane.setCenter(this);
        FlowPane gameplayButtonPane = new FlowPane();
        pane.setBottom(gameplayButtonPane);

    public void registerView(GoBoardModel model){
        this.model = model;

        model.addGameListener(new GameListener() {
            @Override
            public void moveCompleted() {
                draw();
            }

            @Override
            public void resetGame() {
                draw();
            }
        });
    }

        // Buttons for gameplay
        Button resetButton = new Button("Reset");
        resetButton.setOnMouseClicked(e -> controller.resetModel());
        Button passButton = new Button("Pass");
        passButton.setOnMouseClicked(e -> controller.passPlayer());
        Button resignButton = new Button("Resign");
        resignButton.setOnMouseClicked(e -> controller.changeSceneToWinScreen());

        gameplayButtonPane.setPadding(new Insets(30,30,30,30));
        gameplayButtonPane.setHgap(10);
        gameplayButtonPane.setVgap(10);
        gameplayButtonPane.setAlignment(Pos.CENTER);
        gameplayButtonPane.getChildren().add(resetButton);
        gameplayButtonPane.getChildren().add(passButton);
        gameplayButtonPane.getChildren().add(resignButton);

        // Buttons to import/export games
        // --------------------- vielleicht ändern auf menu bar ---------------------
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Game");

        MenuItem importButton = new MenuItem("Import game");
        importButton.setOnAction(e -> controller.openImportFile());
        MenuItem exportButton = new MenuItem("Export game");
        exportButton.setOnAction(e -> System.out.println("Export game"));

        menu.getItems().add(importButton);
        menu.getItems().add(exportButton);


        menuBar.getMenus().add(menu);
        pane.setTop(menuBar);

        pane.widthProperty().addListener((obs, oldVal, newVal) -> {
            resize(newVal.doubleValue(), pane.getHeight());
        });
        pane.heightProperty().addListener((obs, oldVal, newVal) -> {
            resize(pane.getWidth(), newVal.doubleValue());
        });
    }

    public void resize(double width, double height) {
        this.setScale(Math.min(width, height));
        this.draw();
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

        // Add horizontal lines to the board
        for (int i = 1; i <= boardSize; i++) {
            Line line = new Line(tileSize, tileSize * i, tileSize * boardSize, tileSize * i);
            if (i == 1 || i == boardSize) line.setStrokeWidth(3);
            getChildren().add(line);
        }

        // Add vertical lines to the board
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
        for (int y = 0; y < fields.length; y++) {
            for (int x = 0; x < fields[y].length; x++) {
                if (fields[y][x].getStone() != Stone.NONE) {
                    Circle stone = StoneView.createStone((x + 1) * tileSize,
                            (y + 1) * tileSize, fields[y][x].getStone(), tileSize);

                    getChildren().add(stone);
                }
            }
        }
    }


    public void setActionListener(GoBoardController controller) {
        this.controller = controller;
        controller.setView(this);
    }


    public void setScale(double scale) {
        this.tileSize = scale / (boardSize + 1);
    }

    public double getScale() {
        return this.tileSize;
    }

}