package com.gogame.view;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class GameScreenView extends View {
    private GoBoardModel model;
    private GameScreenController sceneController;
    private GoBoardController goBoardController;
    private GoBoardView goBoard;

    private BorderPane pane;

    private TextField gameState;

    public GameScreenView(GoBoardModel model) {
        this.model = model;
        goBoard = new GoBoardView(model);
        goBoardController = goBoard.getController();
        sceneController = new GameScreenController(this);
        gameState = new TextField();

        drawScene();

        model.addGameListener(new GameListener() { //todo outsource into own class
            @Override
            public void moveCompleted(GameEvent event) {
                gameState.setText(event.getRow() + " " + event.getCol() + ": " + event.getState().toString());
            }

            @Override
            public void resetGame(GameEvent event) {
                gameState.setText(event.getState().toString());
            }
        });
    }

    @Override
    protected void drawScene() {
        pane = new BorderPane();
        pane.setCenter(goBoard);
        FlowPane gameplayButtonPane = new FlowPane();
        VBox vBox = new VBox();
        vBox.getChildren().add(gameplayButtonPane);
        vBox.getChildren().add(gameState);
        pane.setBottom(vBox);
        //pane.setLeft(gameState);

        // Buttons for gameplay
        Button resetButton = new Button("Reset");
        resetButton.setOnMouseClicked(e -> goBoardController.resetModel());
        Button passButton = new Button("Pass");
        passButton.setOnMouseClicked(e -> goBoardController.passPlayer());
        Button resignButton = new Button("Resign");
        resignButton.setOnMouseClicked(e -> sceneController.changeSceneToWinScreen());

        gameplayButtonPane.setPadding(new Insets(30, 30, 30, 30));
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
        importButton.setOnAction(e -> sceneController.openImportFile());
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
        goBoard.setScale(Math.min(width, height));
        goBoard.draw();
    }

    @Override
    public BorderPane getPane() {
        return this.pane;
    }
}
