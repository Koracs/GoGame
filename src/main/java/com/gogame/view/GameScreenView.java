package com.gogame.view;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import com.gogame.model.SaveGame;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

public class GameScreenView extends View {
    //region Fields
    private GoBoardModel goBoardModel;
    private final GameScreenController gameScreenController;
    private final GoBoardController goBoardController;
    private final GoBoardView goBoardView;
    private final SaveGame saveGame;

    private BorderPane pane;

    private final GameStateField gameState;
    private final CaptureStatus captureStatus;
    //endregion

    public GameScreenView(GoBoardModel model) {
        gameScreenController = new GameScreenController(this, model);

        goBoardModel = model;
        goBoardView = new GoBoardView(model);
        goBoardController = goBoardView.getController();

        gameState = new GameStateField(model);
        captureStatus = new CaptureStatus(model);

        saveGame = new SaveGame(goBoardController, gameScreenController);

        //eventHandler for placing stones
        EventHandler<MouseEvent> clickHandler = goBoardController::mouseClicked;
        goBoardView.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);

        //eventHandler for mouse hovering
        EventHandler<MouseEvent> moveHandler = goBoardView::moveHoverMouse;
        goBoardView.addEventHandler(MouseEvent.MOUSE_MOVED, moveHandler);

        //eventHandler for keyboard interaction
        goBoardView.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case SPACE, ENTER:
                    goBoardView.setStoneKeyboard();
                default:
                    goBoardView.moveHoverKeyboard(keyEvent);
            }
        });


        drawScene();

        goBoardModel.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {
                saveGame.storeData(event.getRow() + ";" + event.getCol() + "- " + event.getState() + "\n");
            }

            @Override
            public void resetGame(GameEvent event) {
                saveGame.resetData();
            }

            @Override
            public void playerPassed(GameEvent event) {
                saveGame.storeData(event.getState().toString() + "\n");
            }

            @Override
            public void gameEnded(GameEvent event) {
            }
        });

        /*if (!importGame.equals("")) {
            saveGame.importGameFile(importGame);
        }*/
    }

    public void setModel(GoBoardModel goBoardModel) {
        this.goBoardModel = goBoardModel;
    }

    @Override
    protected void drawScene() {
        pane = new BorderPane();
        pane.setCenter(goBoardView);

        VBox interactionField = new VBox();
        FlowPane gameplayButtons = new FlowPane(Orientation.VERTICAL);
        interactionField.getChildren().add(captureStatus);
        interactionField.getChildren().add(gameplayButtons);
        pane.setLeft(interactionField);

        pane.setBottom(gameState);

        // Buttons for gameplay
        Button passButton = new Button("Pass");
        passButton.setFocusTraversable(false);
        passButton.setOnMouseClicked(e -> goBoardController.passPlayer());

        Button resignButton = new Button("Resign");
        resignButton.setFocusTraversable(false);
        resignButton.setOnMouseClicked(e -> {
            goBoardController.resign();
        });

        gameplayButtons.setPadding(new Insets(30));
        gameplayButtons.setHgap(10);
        gameplayButtons.setVgap(10);
        gameplayButtons.setAlignment(Pos.CENTER);
        gameplayButtons.getChildren().add(passButton);
        gameplayButtons.getChildren().add(resignButton);

        GameMenuBar menuBar = new GameMenuBar(goBoardController,gameScreenController,saveGame);
        pane.setTop(menuBar);
    }

    @Override
    public BorderPane getPane() {
        pane.setFocusTraversable(true);
        pane.getCenter().requestFocus();
        return this.pane;
    }
}
