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
        controller = new GameSettingsController();
        model = new GameSettingsModel();
        model.registerView(this);
        controller.setModel(model);
        this.setActionListener(controller);
        this.setModel(model);
        drawGUI();
    }

    //region Getter/Setter
    public BorderPane getPane() {
        return this.pane;
    }

    public void setModel(GameSettingsModel model) {
        this.model = model;
    }

    public void setActionListener(GameSettingsController controller) {
        this.controller = controller;
        controller.setView(this);
    }
    //endregion

    //region Methods
    private void drawGUI() {
        pane = new BorderPane();

        // Start game button
        Button startGame = new Button("Start Game");
        startGame.setOnMouseClicked(e -> {
            GoBoardView view = new GoBoardView(this.model.getBoardSize());
            Window w = pane.getScene().getWindow();
            if(w instanceof Stage) {
                Stage s = (Stage) w;
                System.out.println("Change scene");
                s.setScene(new Scene(view.getPane(),500,600));
            }
        });

        FlowPane pa = new FlowPane(startGame);
        pane.setCenter(pa);
    }
    //endregion

}
