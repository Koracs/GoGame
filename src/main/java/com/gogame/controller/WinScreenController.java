package com.gogame.controller;

import com.gogame.view.StartScreenView;
import com.gogame.view.WinScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class WinScreenController {
    //region Fields
    // MVC variables
    private final WinScreenView view;
    //endregion

    // Constructor
    public WinScreenController(WinScreenView view) {
        this.view = view;
    }

    //region Getter/Setter

    //endregion

    //region Methods
    public void changeSceneToStartScreen() {
        StartScreenView nextView = new StartScreenView();
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage s) {
            s.setScene(new Scene(nextView.getPane(),500,600));
        }
    }
    //endregion
}
