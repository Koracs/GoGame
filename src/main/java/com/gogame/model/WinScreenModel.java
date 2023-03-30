package com.gogame.model;

import com.gogame.view.WinScreenView;
@Deprecated

public class WinScreenModel {
    //region Fields
    // MVC variables
    private WinScreenView view;

    // Model variables
    private String winner;
    //endregion

    // Constructor
    public WinScreenModel(WinScreenView view, String winner) {
        this.view = view;
        this.winner = winner;
    }

    //region Getter/Setter
    public String getWinner() {
        return winner;
    }

    public void setView(WinScreenView view) {
        this.view = view;
    }
    //endregion
}
