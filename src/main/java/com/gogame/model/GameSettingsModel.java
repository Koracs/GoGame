package com.gogame.model;

import com.gogame.view.GameSettingsView;
import com.gogame.view.GoBoardView;

import java.util.ArrayList;
import java.util.List;
@Deprecated
public class GameSettingsModel {
    //region Fields
    // MVC variables
    private GameSettingsView view;

    // Model variables
    private int boardSize;
    int[] sizes;
    private int handicap;
    private double komi;
    private boolean komiActive;
    private boolean handicapActive;
    //endregion

    // Constructor - set all settings to default value
    public GameSettingsModel(GameSettingsView view) {
        this.view = view;
        this.boardSize = 19;
        this.komi = 0.5;
        this.handicap = 0;
        this.komiActive = false;
        this.handicapActive = false;
        sizes = new int[]{9,13,19};
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

    public void setView(GameSettingsView view) {
        this.view = view;
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

    public int[] getSizes() {
        return sizes;
    }

    //endregion

    //region Methods

    //endregion
}
