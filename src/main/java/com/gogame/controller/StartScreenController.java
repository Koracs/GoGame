package com.gogame.controller;

import com.gogame.view.GameSettingsView;
import com.gogame.view.StartScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StartScreenController {
    //region Fields
    // MVC variables
    private StartScreenView view;
    //endregion

    //region Constructor
    public StartScreenController(StartScreenView view) {
        this.view = view;
    }

    //endregion

    //region Getter/Setter
    public void setView(StartScreenView view) {
        this.view = view;
    }
    //endregion


    //region Methods
    public void changeSceneToGameSettingsScreen() {
        GameSettingsView nextView = new GameSettingsView();
        Window w = view.getPane().getScene().getWindow();
        if (w instanceof Stage) {
            Stage s = (Stage) w;
            s.setScene(new Scene(nextView.getPane(), 500, 600));
        }
    }
    //endregion
}
