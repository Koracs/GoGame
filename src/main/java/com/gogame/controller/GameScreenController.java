package com.gogame.controller;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import com.gogame.view.GameScreenView;
import com.gogame.view.TutorialSettingsView;
import com.gogame.view.WinScreenDialog;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;

import java.util.Optional;

public class GameScreenController implements GameListener {
    //region Fields
    private final GameScreenView view;
    private GoBoardModel model;
    private int boardSize;
    private int handicap;
    private double komi;
    private boolean komiActive;
    private boolean handicapActive;
    //endregion

    // Constructor
    public GameScreenController(GameScreenView view, GoBoardModel model) {
        model.addGameListener(this);
        this.view = view;
        this.model = model;
        this.boardSize = 19;
        this.komi = 0.5;
        this.handicap = 0;
        this.komiActive = false;
        this.handicapActive = false;
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
    public GameScreenView getView() {
        return view;
    }
    public GoBoardModel getModel() {
        return model;
    }

    public void setViewModel(GoBoardModel model) {
        this.model = model;
        this.view.setModel(model);
    }
    //endregion

    //region Methods
    public GoBoardModel initGoBoardModel() {
        return new GoBoardModel(boardSize,
                komiActive ? komi : 0,
                handicapActive ? handicap : 0);
    }

    public void showWinScreen() {
        WinScreenDialog winScreenDialog = new WinScreenDialog(model);
        Optional<ButtonType> result = winScreenDialog.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE){
            model.reset();
        }
    }

    public void changeToTutorialSettingScreen() {
        TutorialSettingsView nextView = new TutorialSettingsView();
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }

    public void changeGameModel() {
        GameScreenView nextView = new GameScreenView(initGoBoardModel());
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);

            BorderPane root = (BorderPane) stage.getScene().getRoot();
            root.getCenter().requestFocus();
        }
    }

    @Override
    public void moveCompleted(GameEvent event) {

    }

    @Override
    public void resetGame(GameEvent event) {

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
