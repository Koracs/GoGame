package com.gogame.controller;

import com.gogame.model.GoBoardModel;
import com.gogame.view.GameSettingsView;
import com.gogame.view.GameScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GameSettingsController {
    //region Fields
    private final GameSettingsView view;
    private int boardSize;
    private int handicap;
    private double komi;
    private boolean komiActive;
    private boolean handicapActive;
    //endregion

    //region Constructor
    public GameSettingsController(GameSettingsView view) {
        this.view = view;
        this.boardSize = 19;
        this.komi = 0.5;
        this.handicap = 0;
        this.komiActive = false;
        this.handicapActive = false;
    }
    //endregion

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

    //endregion

    //region Methods

    private GoBoardModel initGoBoardModel(){
        return new GoBoardModel(boardSize,
                komiActive ? komi : 0,
                handicapActive ? handicap : 0);
    }
    public void changeSceneToGameScene() {
        GameScreenView nextView = new GameScreenView(initGoBoardModel());
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage) {
            Stage s = (Stage) w;
            s.setScene(new Scene(nextView.getPane(),500,650));
        }
    }
    //endregion
}
