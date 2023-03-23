package com.gogame.controller;

import com.gogame.model.WinScreenModel;
import com.gogame.view.StartScreenView;
import com.gogame.view.WinScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class WinScreenController {
    //region Fields
    // MVC variables
    private WinScreenModel model;
    private WinScreenView view;
    //endregion

    // Constructor
    public WinScreenController(WinScreenModel model, WinScreenView view) {
        this.model = model;
        this.view = view;
    }

    //region Getter/Setter
    public void setModel(WinScreenModel model) {
        this.model = model;
    }

    public void setView(WinScreenView view) {
        this.view = view;
    }
    //endregion

    //region Methods
    public void changeSceneToStartScreen() {
        StartScreenView nextView = new StartScreenView();
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage) {
            Stage s = (Stage) w;
            s.setScene(new Scene(nextView.getPane(),500,600));
        }
    }
    //endregion
}
