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

    // Constructor
    public GameScreenController(GameScreenView view, GoBoardModel model, File file) {
        model.addGameListener(this);
        this.view = view;
        this.model = model;
        this.boardSize = 19;
        this.komi = 0.5;
        this.handicap = 0;
        this.komiActive = false;
        this.handicapActive = false;
        currentFile = file;
        this.isFileSaved = true;
    }

    //region Getter/Setter

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getHandicap() {
        return handicap;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    public double getKomi() {
        return komi;
    }

    public void setKomi(double komi) {
        this.komi = komi;
    }

    public boolean isKomiActive() {
        return komiActive;
    }

    public void changeKomiActive() {
        this.komiActive = !this.komiActive;
    }

    public boolean isHandicapActive() {
        return handicapActive;
    }

    public void changeHandicapActive() {
        this.handicapActive = !this.handicapActive;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public boolean isFileSaved() {
        return isFileSaved;
    }
    //endregion

    //region Methods
    private GoBoardModel initGoBoardModel() {
        return new GoBoardModel(boardSize,
                komiActive ? komi : 0,
                handicapActive ? handicap : 0);
    }

    private void showWinScreen() {
        WinScreenDialog winScreenDialog = new WinScreenDialog(model);
        winScreenDialog.showAndWait();
    }

    public void changeGameModel() {
        changeGameModel(initGoBoardModel(), null);
    }

    public void changeGameModel(File file) {
        SaveGameHandler s = new SaveGameHandler(file);
        changeGameModel(s.createGameModel(), file);
    }

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
