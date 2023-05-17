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
            goBoardModel.playerResigned();
        });

        gameplayButtons.setPadding(new Insets(30));
        gameplayButtons.setHgap(10);
        gameplayButtons.setVgap(10);
        gameplayButtons.setAlignment(Pos.CENTER);
        gameplayButtons.getChildren().add(passButton);
        gameplayButtons.getChildren().add(resignButton);

        // Buttons to import/export games
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("_File");

        Menu game = new Menu("_Game");
        Menu help = new Menu("_Help");
        menuBar.getMenus().add(file);
        menuBar.getMenus().add(game);
        menuBar.getMenus().add(help);

        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(e -> goBoardController.resetModel());
        file.getItems().add(newGame);
        newGame.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));


        MenuItem loadGame = new MenuItem("Open Game");
        loadGame.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File selectedFile = fileChooser.showOpenDialog(this.getPane().getScene().getWindow());
            if(selectedFile != null) {
                saveGame.importGameFile(selectedFile.getAbsolutePath(), false);
                goBoardView.autosize();
            }
        });
        file.getItems().add(loadGame);

        MenuItem saveButton = new MenuItem("Save Game");
        saveButton.setOnAction(e -> {
            File newFile = new File(System.getProperty("user.dir"));
            saveGame.exportGameFile(newFile);
        });
        file.getItems().add(saveButton);
        saveButton.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        MenuItem saveAsButton = new MenuItem("SaveAs Game");
        saveAsButton.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setInitialFileName("mySaveGame.txt");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
            File selectedFile = chooser.showSaveDialog(this.getPane().getScene().getWindow());
            saveGame.exportGameFile(selectedFile);
        });
        file.getItems().add(saveAsButton);

        MenuItem exitGame = new MenuItem("Exit");
        exitGame.setOnAction(e -> Platform.exit());
        file.getItems().add(new SeparatorMenuItem());
        file.getItems().add(exitGame);
        exitGame.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

        MenuItem changeSettings = new MenuItem("Change Settings");
        changeSettings.setOnAction(e -> {
            SettingsDialog settingsDialog = new SettingsDialog(gameScreenController);
            Optional<ButtonType> result = settingsDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                gameScreenController.changeGameModel();
            }
        });
        game.getItems().add(changeSettings);

        MenuItem howToPlay = new MenuItem("How to play");
        Alert howToPlayDialog = new Alert(Alert.AlertType.INFORMATION);
        howToPlayDialog.setTitle("How to play GO");
        howToPlayDialog.setHeaderText(null);
        howToPlayDialog.setContentText("""
        Controls:
        Move Stone:     Mouse, WASD or Arrow Keys
        Place Stone:     Left Mouse Button, Enter, Spacebar
        Place Marker:   Right Mouse Button
        
        Rules:
        Capture other stones by surrounding them.
        Gain territory control for points.
        The player with the most points wins!
        """);

        howToPlay.setOnAction(e -> howToPlayDialog.showAndWait());
        help.getItems().add(howToPlay);

        MenuItem showTutorials = new MenuItem("Show Tutorials");
        showTutorials.setOnAction(e -> gameScreenController.changeToTutorialSettingScreen());
        help.getItems().add(showTutorials);

        MenuItem aboutUs = new MenuItem("About us");
        Alert aboutUsDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutUsDialog.setTitle("About us");
        aboutUsDialog.setHeaderText("Go Game - PR SE SS2023 - Group 5");
        aboutUsDialog.setContentText("Made by: \nDominik Niederberger, Felix Stadler, Simon Ulmer");
        //aboutUsDialog.initOwner(); TODO pass Stage?
        aboutUs.setOnAction(e -> aboutUsDialog.showAndWait());
        help.getItems().add(aboutUs);

        pane.setTop(menuBar);
    }

    @Override
    public BorderPane getPane() {
        pane.setFocusTraversable(true);
        pane.getCenter().requestFocus();
        return this.pane;
    }
}
