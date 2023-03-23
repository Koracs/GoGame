package com.gogame.controller;

import com.gogame.model.WinScreenModel;
import com.gogame.view.GameSettingsView;
import com.gogame.view.GoBoardView;
import com.gogame.view.StartScreenView;
import com.gogame.view.WinScreenView;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StartscreenController {
    //region Fields
    // MVC variables
    private StartScreenView view;
    //endregion

    // Constructor
    public StartscreenController(StartScreenView view) {
        this.view = view;
    }

    //region Getter/Setter
    public void setView(StartScreenView view) {
        this.view = view;
    }
    //endregion


    //region Methods
    public void changeSceneToGameSettingsScreen() {
        GameSettingsView nextView = new GameSettingsView();
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage) {
            Stage s = (Stage) w;
            s.setScene(new Scene(nextView.getPane(),500,600));
        }
    }
    //endregion
}
