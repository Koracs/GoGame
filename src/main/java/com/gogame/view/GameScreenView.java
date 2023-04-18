package com.gogame.view;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import com.gogame.model.SaveGame;
import com.gogame.model.Stone;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameScreenView extends View {
    private final GoBoardModel goBoardModel;
    private final GameScreenController gameScreenController;
    private final GoBoardController goBoardController;
    private final GoBoardView goBoardView;
    private final SaveGame saveGame;

    private BorderPane pane;

    private final TextField gameState;

    public GameScreenView(GoBoardModel model) {
        gameScreenController = new GameScreenController(this, model);

        goBoardModel = model;
        goBoardView = new GoBoardView(model);
        goBoardController = goBoardView.getController();
        gameState = new TextField();

        saveGame = new SaveGame(goBoardController, gameScreenController);

        //add eventHandler to allow gameplay interaction
        EventHandler<MouseEvent> clickHandler = goBoardController::mouseClicked;
        goBoardView.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);

        EventHandler<MouseEvent> moveHandler = goBoardView::drawHover;
        goBoardView.addEventHandler(MouseEvent.MOUSE_MOVED,moveHandler);

        drawScene();
        model.addGameListener(new GameListener() { //todo outsource into own class?
            @Override
            public void moveCompleted(GameEvent event) {
                String lastPlayer = goBoardModel.getCurrentPlayer() == Stone.BLACK? "White" : "Black";
                //todo convert to ABC/123
                gameState.setText("Player " + lastPlayer + " placed at: " + event.getColLetter()
                        + " " + event.getRowLetter() + ". " + event.getState().toString());
            }

            @Override
            public void resetGame(GameEvent event) {
                gameState.setText(event.getState().toString());
            }

            @Override
            public void playerPassed(GameEvent event) {
                gameState.setText(event.getState().toString());
            }
        });

        model.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {
                saveGame.storeData(event.getRow() + ";" + event.getCol() + "/ " + event.getState() + "\n");
            }

            @Override
            public void resetGame(GameEvent event) {
                saveGame.resetData();
            }

            @Override
            public void playerPassed(GameEvent event) {
                saveGame.storeData(event.getState().toString() + "\n");
            }
        });
    }

    @Override
    protected void drawScene() {
        pane = new BorderPane();
        pane.setCenter(goBoardView);

        VBox interactionField = new VBox();
        FlowPane captureStatus = new FlowPane();
        FlowPane gameplayButtons = new FlowPane(Orientation.VERTICAL);
        interactionField.getChildren().add(captureStatus);
        interactionField.getChildren().add(gameplayButtons);
        pane.setLeft(interactionField);


        pane.setBottom(gameState);


        // Buttons for gameplay
        Button passButton = new Button("Pass");
        passButton.setOnMouseClicked(e -> goBoardController.passPlayer());
        Button resignButton = new Button("Resign");
        resignButton.setOnMouseClicked(e -> gameScreenController.changeSceneToWinScreen(goBoardModel.getCurrentPlayer())); //todo get Data over Controller

        gameplayButtons.setPadding(new Insets(30));
        gameplayButtons.setHgap(10);
        gameplayButtons.setVgap(10);
        gameplayButtons.setAlignment(Pos.CENTER);
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
        MenuBar menuBar = new MenuBar();
        SeparatorMenuItem separator = new SeparatorMenuItem();
        Menu game = new Menu("Game");
        Menu file = new Menu("File");

        MenuItem restartButton = new MenuItem("Restart game");
        restartButton.setOnAction(e -> goBoardController.resetModel());
        MenuItem mainMenuButton = new MenuItem("Main menu");
        mainMenuButton.setOnAction(e -> gameScreenController.changeSceneToStartScreen());

        game.getItems().add(restartButton);
        game.getItems().add(separator);
        game.getItems().add(mainMenuButton);


        MenuItem importButton = new MenuItem("Import game");
        importButton.setOnAction(e -> saveGame.importGameFile());
        MenuItem exportButton = new MenuItem("Export game");
        exportButton.setOnAction(e -> saveGame.exportGameFile());

        file.getItems().add(importButton);
        file.getItems().add(exportButton);

        menuBar.getMenus().add(game);
        menuBar.getMenus().add(file);
        pane.setTop(menuBar);
    }

    @Override
    public BorderPane getPane() {
        return this.pane;
    }
}
