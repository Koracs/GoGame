package com.gogame.view;

import com.gogame.controller.StartScreenController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StartScreenView extends Parent {
    //region Fields
    // Set style of button
    private final String STYLE = "-fx-background-color:\n" +
            "            linear-gradient(#ffd65b, #e68400),\n" +
            "            linear-gradient(#ffef84, #f2ba44),\n" +
            "            linear-gradient(#ffea6a, #efaa22),\n" +
            "            linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n" +
            "            linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));\n" +
            "    -fx-background-radius: 30;\n" +
            "    -fx-background-insets: 0,1,2,3,0;\n" +
            "    -fx-text-fill: #654b00;\n" +
            "    -fx-font-weight: bold;\n" +
            "    -fx-font-size: 14px;\n" +
            "    -fx-padding: 10 20 10 20;";

    // Pane of this class
    private BorderPane pane;

    // MVC variables
    private StartScreenController controller;
    //endregion

    // Constructor
    public StartScreenView() {
        this.controller = new StartScreenController(this);
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
        startGame.setStyle(STYLE);
        startGame.setOnMouseClicked(e -> controller.changeSceneToGameSettingsScreen());

        Button tutorial = new Button("Tutorial"); //todo which requirment mentions a tutorial?
        tutorial.setStyle(STYLE);
        tutorial.setOnMouseClicked(e -> System.out.println("Tutorial button clicked!"));

        FlowPane pa = new FlowPane(startGame,tutorial);
        pane.setCenter(pa);
    }
    //endregion
}
