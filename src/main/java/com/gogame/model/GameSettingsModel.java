package com.gogame.model;

import com.gogame.view.GameSettingsView;
import com.gogame.view.GoBoardView;

public class GameSettingsModel {
    //region Fields
    // MVC variables
    private GameSettingsView view;

    // Model variables
    private int boardSize;
    private int handicap;
    private double komi;
    //endregion

    // Constructor - set all settings to default value
    public GameSettingsModel(GameSettingsView view) {
        this.view = view;
        this.boardSize = 19;
        this.komi = 0.5;
        this.handicap = 0;
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
    //endregion

    //region Methods
    public void registerView(GameSettingsView view) {
        this.view = view;
    }

    //endregion
}
