package com.gogame.controller;

import com.gogame.view.GameSettingsView;
import com.gogame.view.StartScreenView;
import com.gogame.view.TutorialSettingsView;
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
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.setOnKeyPressed(s.getOnKeyPressed());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }

    public void changeSceneToTutorialSettingScreen() {
        TutorialSettingsView nextView = new TutorialSettingsView();
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.setOnKeyPressed(s.getOnKeyPressed());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }
    //endregion
}
