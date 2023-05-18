package com.gogame.view;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.model.SaveGame;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

public class GameMenuBar extends MenuBar {

    private final GameScreenController gameScreenController;

    public GameMenuBar(GoBoardController goBoardController, GameScreenController gameScreenController, SaveGame saveGame) {
        this.gameScreenController = gameScreenController;

        Menu file = new Menu("_File");
        Menu game = new Menu("_Game");
        Menu help = new Menu("_Help");
        this.getMenus().add(file);
        this.getMenus().add(game);
        this.getMenus().add(help);

        MenuItem newGame = new MenuItem("_New Game");
        newGame.setOnAction(e -> {
            askForSave();
            changeSettings();
        });
        file.getItems().add(newGame);
        newGame.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));


        MenuItem openGame = new MenuItem("_Open Game");
        openGame.setOnAction(e -> {
            askForSave();

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (selectedFile != null) {
                saveGame.importGameFile(selectedFile.getAbsolutePath(), false);
                // todo goBoardView.autosize();
            }
        });
        file.getItems().add(openGame);
        file.getItems().add(new SeparatorMenuItem());
        openGame.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        MenuItem saveButton = new MenuItem("_Save Game");
        saveButton.setOnAction(e -> {
            saveGame();
        });
        file.getItems().add(saveButton);
        saveButton.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        MenuItem saveAsButton = new MenuItem("Save Game as");
        saveAsButton.setOnAction(e -> {
            saveGameAs();
        });
        file.getItems().add(saveAsButton);
        saveAsButton.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));


        MenuItem exitGame = new MenuItem("_Exit");
        exitGame.setOnAction(e -> {
            askForSave();
            Platform.exit();
        });
        file.getItems().add(new SeparatorMenuItem());
        file.getItems().add(exitGame);
        exitGame.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

        MenuItem pass = new MenuItem("_Pass");
        pass.setOnAction(e -> goBoardController.passPlayer());
        game.getItems().add(pass);

        MenuItem resign = new MenuItem("_Resign");
        resign.setOnAction(e -> goBoardController.resign());
        game.getItems().add(resign);
        game.getItems().add(new SeparatorMenuItem());

        MenuItem changeSettings = new MenuItem("_Change Settings");
        changeSettings.setOnAction(e -> {
            askForSave();
            changeSettings();
        });
        game.getItems().add(changeSettings);


        MenuItem howToPlay = new MenuItem("_How to play");
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

        MenuItem showTutorials = new MenuItem("Show _Tutorials");
        showTutorials.setOnAction(e -> {
            askForSave();
            showTutorials();
        });
        help.getItems().add(showTutorials);

        MenuItem aboutUs = new MenuItem("_About us");
        Alert aboutUsDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutUsDialog.setTitle("About us");
        aboutUsDialog.setHeaderText("Go Game - PR SE SS2023 - Group 5");
        aboutUsDialog.setContentText("Made by: \nDominik Niederberger, Felix Stadler, Simon Ulmer");
        aboutUs.setOnAction(e -> aboutUsDialog.showAndWait());
        help.getItems().add(aboutUs);
    }

    private void askForSave() {
        Alert save = new Alert(Alert.AlertType.CONFIRMATION);
        save.setTitle("Save Game?");
        save.setHeaderText("Would you like to save the current game?");
        Optional<ButtonType> result = save.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            saveGame();
        }
    }

    private void saveGame() {
        if (gameScreenController.getCurrentFile() == null) {
            saveGameAs();
        } else {
            gameScreenController.createSaveFile(gameScreenController.getCurrentFile());
        }
    }

    private void saveGameAs() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName("mySaveGame.txt");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File selectedFile = chooser.showSaveDialog(this.getScene().getWindow());
        gameScreenController.createSaveFile(selectedFile);
    }

    private void changeSettings() {
        SettingsDialog settingsDialog = new SettingsDialog(gameScreenController);
        Optional<ButtonType> result = settingsDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            gameScreenController.changeGameModel();
        }
    }

    private void showTutorials() {
        TutorialDialog tutorialDialog = new TutorialDialog(gameScreenController);
        Optional<ButtonType> result = tutorialDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            gameScreenController.changeSceneToTutorialScene();

        }
    }
}