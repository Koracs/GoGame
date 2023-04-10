package com.gogame.view;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameScreenView extends View {
    private final GoBoardModel model;
    private final GameScreenController sceneController;
    private final GoBoardController goBoardController;
    private final GoBoardView goBoard;

    private BorderPane pane;

    private final TextField gameState;

    public GameScreenView(GoBoardModel model) {
        sceneController = new GameScreenController(this);

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
    }

    @Override
    protected void drawScene() {
        pane = new BorderPane();
        pane.setCenter(goBoard);

        VBox interactionField = new VBox();
        FlowPane captureStatus = new FlowPane();
        FlowPane gameplayButtons = new FlowPane(Orientation.VERTICAL);
        interactionField.getChildren().add(captureStatus);
        interactionField.getChildren().add(gameplayButtons);
        pane.setLeft(interactionField);


        pane.setBottom(gameState);


        // Buttons for gameplay
        Button resetButton = new Button("Reset");
        resetButton.setOnMouseClicked(e -> goBoardController.resetModel());
        Button passButton = new Button("Pass");
        passButton.setOnMouseClicked(e -> goBoardController.passPlayer());
        Button resignButton = new Button("Resign");
        resignButton.setOnMouseClicked(e -> sceneController.changeSceneToWinScreen(model.getGameState()));

        gameplayButtons.setPadding(new Insets(30));
        gameplayButtons.setHgap(10);
        gameplayButtons.setVgap(10);
        gameplayButtons.setAlignment(Pos.CENTER);
        gameplayButtons.getChildren().add(resetButton);
        gameplayButtons.getChildren().add(passButton);
        gameplayButtons.getChildren().add(resignButton);

        //Buttons for capture status
        Label labelWhite = new Label("Captured by White: ");
        Text capturedByWhite = new Text("0");
        Label labelBlack = new Label("Captured by Black: ");
        Text capturedByBlack = new Text("0");

        captureStatus.setPadding(new Insets(30));
        captureStatus.setHgap(10);
        captureStatus.setVgap(10);
        captureStatus.getChildren().add(labelWhite);
        captureStatus.getChildren().add(capturedByWhite);
        captureStatus.getChildren().add(labelBlack);
        captureStatus.getChildren().add(capturedByBlack);
        captureStatus.setMaxWidth(120);

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
    }

    @Override
    public BorderPane getPane() {
        return this.pane;
    }
}
