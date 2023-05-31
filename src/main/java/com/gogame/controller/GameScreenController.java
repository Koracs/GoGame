package com.gogame.controller;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import com.gogame.savegame.SaveGameHandler;
import com.gogame.view.GameScreenView;
import com.gogame.view.TutorialView;
import com.gogame.view.WinScreenDialog;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;

import java.io.File;
import java.io.IOException;

/**
 * The GameScreenController handles interactions between the user / view and the go board model
 */
public class GameScreenController implements GameListener {
    //region Fields
    private final GameScreenView view;
    private final GoBoardModel model;
    private int boardSize;
    private int handicap;
    private double komi;
    private boolean komiActive;
    private boolean handicapActive;
    private File currentFile;
    private boolean isFileSaved;
    //endregion

    /**
     * Constructs a GameScreenController that handles interactions with the game screen
     * @param view View that should be controlled
     * @param model GoBoardModel that should be controlled
     * @param file File that stores information about the current game. can be null
     */
    public GameScreenController(GameScreenView view, GoBoardModel model, File file) {
        model.addGameListener(this);
        this.view = view;
        this.model = model;
        this.boardSize = model.getSize();
        this.komi = model.getKomi();
        this.handicap = model.getHandicap();
        this.komiActive = false;
        this.handicapActive = false;
        currentFile = file;
        this.isFileSaved = true;
    }

    //region Getter/Setter

    /**
     * Getter for the size of the controller
     * @return returns the board size
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Setter for the board size of the controller
     * @param boardSize board size to be set
     */
    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }
    /**
     * Getter for the handicap of the controller
     * @return returns the controllers handicap setting
     */
    public int getHandicap() {
        return handicap;
    }

    /**
     * Setter for the handicap of the controller
     * @param handicap handicap to be set
     */
    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    /**
     * Getter for the komi setting of the controller
     * @return returns the controllers komi setting
     */
    public double getKomi() {
        return komi;
    }

    /**
     * Setter for the komi of the controller
     * @param komi komi to be set
     */
    public void setKomi(double komi) {
        this.komi = komi;
    }

    /**
     * Check if the komi setting is active
     * @return if the komi setting is active
     */
    public boolean isKomiActive() {
        return komiActive;
    }

    /**
     * Change the state of the komi setting
     */
    public void changeKomiActive() {
        this.komiActive = !this.komiActive;
    }

    /**
     * Check if the handicap setting is active
     * @return if the handicap setting is active
     */
    public boolean isHandicapActive() {
        return handicapActive;
    }

    /**
     * Change the state of the handicap setting
     */
    public void changeHandicapActive() {
        this.handicapActive = !this.handicapActive;
    }

    /**
     * Getter for the currently used file for the game
     * @return File that saves the game information
     */
    public File getCurrentFile() {
        return currentFile;
    }

    /**
     * Check if the currently used file is saved
     * @return if the file is saved
     */
    public boolean isFileSaved() {
        return isFileSaved;
    }
    //endregion

    //region Methods

    /**
     * Creates a GoBoardModel with the currently stored settings
     * @return a GoBoardModel with the currently stored settings
     */
    private GoBoardModel initGoBoardModel() {
        return new GoBoardModel(boardSize,
                komiActive ? komi : 0,
                handicapActive ? handicap : 0);
    }

    /**
     * shows the win screen dialog when the game ends
     */
    private void showWinScreen() {
        WinScreenDialog winScreenDialog = new WinScreenDialog(model);
        winScreenDialog.showAndWait();
    }

    /**
     * changes the game model to one with the currently stored settings
     */
    public void changeGameModel() {
        changeGameModel(initGoBoardModel(), null);
    }
    /**
     * changes the game model with the game settings from the file
     * @param file File that stores information about a game
     */
    public void changeGameModel(File file) {
        SaveGameHandler s = new SaveGameHandler(file);
        changeGameModel(s.createGameModel(), file);
    }

    /**
     * private method for creating a new game instance
     * @param model GoBoardModel to be used
     * @param file File where the game data is stored, can be null
     */
    private void changeGameModel(GoBoardModel model, File file) {
        GameScreenView nextView = new GameScreenView(model, file);
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if (w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(), s.getWidth(), s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
            if(file == null) stage.setTitle("Go Game");
            else stage.setTitle("Go Game - " + file.getName());

            BorderPane root = (BorderPane) stage.getScene().getRoot();
            root.getCenter().requestFocus();
        }
    }

    /**
     * Switches the current view to the tutorial view
     * @param selectedTutorial File of the selected tutorial
     */
    public void changeSceneToTutorialScene(File selectedTutorial) {
        currentFile = selectedTutorial;
        SaveGameHandler saveGame = new SaveGameHandler(selectedTutorial);
        TutorialView nextView = new TutorialView(saveGame);

        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if (w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(), s.getWidth(), s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Go Game Tutorial - " + currentFile.getName());
        }
    }

    /**
     * Creates a save file of the current game
     * @param file File where the gama data should be stored
     */
    public void createSaveFile(File file) {
        currentFile = file;
        isFileSaved = true;
        try {
            SaveGameHandler.createSaveFile(model,file);
            Window w = view.getPane().getScene().getWindow();
            if (w instanceof Stage stage) stage.setTitle("Go Game - " + currentFile.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void moveCompleted(GameEvent event) {
        isFileSaved = false;
    }

    @Override
    public void resetGame(GameEvent event) {
        currentFile = null;
        isFileSaved = false;
    }

    @Override
    public void playerPassed(GameEvent event) {
    }

    @Override
    public void gameEnded(GameEvent event) {
        showWinScreen();
    }
    //endregion
}
