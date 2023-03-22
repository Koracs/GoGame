package com.gogame.controller;

import com.gogame.model.GameSettingsModel;
import com.gogame.model.GoBoardModel;
import com.gogame.view.GameSettingsView;
import com.gogame.view.GoBoardView;

public class GameSettingsController {
    //region Fields
    // MVC variables
    private GameSettingsModel model;
    private GameSettingsView view;
    //endregion

    // Constructor
    public GameSettingsController() {

    }

    //region Getter/Setter
    public void setModel(GameSettingsModel model) {
        this.model = model;
    }

    public void setView(GameSettingsView view) {
        this.view = view;
    }
    //endregion
}
