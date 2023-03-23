package com.gogame.view;

import com.gogame.controller.GameSettingsController;
import com.gogame.model.GameSettingsModel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GameSettingsView {
    //region Fields
    // Pane of this class
    private BorderPane pane;

    // MVC variables
    private GameSettingsController controller;
    private GameSettingsModel model;

    // Auxiliary variables


    //endregion

    // Constructor
    public GameSettingsView() {
        model = new GameSettingsModel(this);
        controller = new GameSettingsController(model, this);
        this.setController(controller);
        this.setModel(model);
        drawScene();
    }

    //region Getter/Setter
    public BorderPane getPane() {
        return this.pane;
    }

    public void setModel(GameSettingsModel model) {
        this.model = model;
    }

    public void setController(GameSettingsController controller) {
        this.controller = controller;
        controller.setView(this);
    }
    //endregion

    //region Methods
    private void drawScene() {
        pane = new BorderPane();

        // Start game button
        Button startGame = new Button("Start Game");
        startGame.setOnMouseClicked(e -> controller.changeSceneToGameScene());

        pane.setCenter(startGame);
    }
    //endregion

}
