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
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }
    //endregion
}
