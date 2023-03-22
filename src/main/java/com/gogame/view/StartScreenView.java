package com.gogame.view;

import com.gogame.controller.StartscreenController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StartScreenView extends Parent {
    //region Fields
    // Pane of this class
    private BorderPane pane;
    //endregion

    // Constructor
    public StartScreenView() {
        createScreen();
    }

    //region Getter/Setter
    public BorderPane getPane() {
        return this.pane;
    }
    //endregion

    //region Methods
    public void createScreen () {
        pane = new BorderPane();

        // Create buttons for selecting game mode
        Button startGame = new Button("Start game");
        startGame.setOnMouseClicked(e -> {
            GameSettingsView view = new GameSettingsView();
            Window w = pane.getScene().getWindow();
            if(w instanceof Stage) {
                Stage s = (Stage) w;
                System.out.println("Change scene");
                s.setScene(new Scene(view.getPane(),500,600));
            }
        });

        Button tutorial = new Button("Tutorial");
        tutorial.setOnMouseClicked(e -> System.out.println("Tutorial button clicked!"));

        FlowPane pa = new FlowPane(startGame,tutorial);
        pane.setCenter(pa);
    }
    //endregion
}
