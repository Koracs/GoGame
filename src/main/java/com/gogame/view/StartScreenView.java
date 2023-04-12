package com.gogame.view;

import com.gogame.controller.StartScreenController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class StartScreenView extends View {
    //region Fields
    // Pane of this class
    private BorderPane pane;

    // MVC variables
    private final StartScreenController controller;
    //endregion

    //region Constructor
    public StartScreenView() {
        this.controller = new StartScreenController(this);
        drawScene();
    }
    //endregion

    //region Getter/Setter
    @Override
    public BorderPane getPane() {
        return this.pane;
    }
    //endregion

    //region Methods
    @Override
    protected void drawScene() {
        pane = new BorderPane();

        // Create buttons for selecting game mode
        Button startGame = new Button("Start game");
        startGame.setOnMouseClicked(e -> controller.changeSceneToGameSettingsScreen());

        Button tutorial = new Button("Tutorial");
        tutorial.setOnMouseClicked(e -> System.out.println("Tutorial button clicked!"));

        FlowPane pa = new FlowPane(startGame, tutorial);
        pa.setHgap(10);
        pa.setAlignment(Pos.CENTER);
        pane.setCenter(pa);
    }
    //endregion
}
