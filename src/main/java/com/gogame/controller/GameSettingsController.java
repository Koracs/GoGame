package com.gogame.controller;

import com.gogame.model.GameSettingsModel;
import com.gogame.model.GoBoardModel;
import com.gogame.view.GameSettingsView;
import com.gogame.view.GameScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GameSettingsController {
    //region Fields
    // MVC variables
    private GameSettingsModel model;
    private GameSettingsView view;
    //endregion

    // Constructor
    public GameSettingsController(GameSettingsModel model, GameSettingsView view) {
        this.model = model;
        this.view = view;
    }

    //region Getter/Setter
    public void setModel(GameSettingsModel model) {
        this.model = model;
    }

    public void setView(GameSettingsView view) {
        this.view = view;
    }
    //endregion

    //region Methods
    public void changeSceneToGameScene() {
        GoBoardModel goBoardModel = new GoBoardModel(model.getBoardSize());
        GameScreenView nextView = new GameScreenView(goBoardModel);
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage) {
            Stage s = (Stage) w;
            s.setScene(new Scene(nextView.getPane(),500,600));
        }
    }
    //endregion
}
