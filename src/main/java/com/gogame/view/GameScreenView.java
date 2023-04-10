package com.gogame.view;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class GameScreenView extends View {
    private final GoBoardModel model;
    private final GameScreenController sceneController;
    private final GoBoardController goBoardController;
    private final GoBoardView goBoard;

    private BorderPane pane;

    private final TextField gameState;

    public GameScreenView(GoBoardModel model) {
        sceneController = new GameScreenController(this, model);

        this.model = model;
        goBoard = new GoBoardView(model);
        goBoardController = goBoard.getController();
        gameState = new TextField();

        //add eventHandler to allow gameplay interaction
        EventHandler<MouseEvent> clickHandler = goBoardController::mouseClicked;
        goBoard.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);

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

        model.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {
                model.storeData( event.getRow() + ";" + event.getCol() + "\n");
            }

            @Override
            public void resetGame(GameEvent event) {

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

        // Buttons for gameplay
        Button resetButton = new Button("Reset");
        resetButton.setOnMouseClicked(e -> goBoardController.resetModel());
        Button passButton = new Button("Pass");
        passButton.setOnMouseClicked(e -> goBoardController.passPlayer());
        Button resignButton = new Button("Resign");
        resignButton.setOnMouseClicked(e -> sceneController.changeSceneToWinScreen(model.getGameState()));

        gameplayButtonPane.setPadding(new Insets(30, 30, 30, 30));
        gameplayButtonPane.setHgap(10);
        gameplayButtonPane.setVgap(10);
        gameplayButtonPane.setAlignment(Pos.CENTER);
        gameplayButtonPane.getChildren().add(resetButton);
        gameplayButtonPane.getChildren().add(passButton);
        gameplayButtonPane.getChildren().add(resignButton);

        // Buttons to import/export games
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Game");

        MenuItem importButton = new MenuItem("Import game");
        importButton.setOnAction(e -> sceneController.importGameFile());
        MenuItem exportButton = new MenuItem("Export game");
        exportButton.setOnAction(e -> sceneController.exportGameFile());

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
