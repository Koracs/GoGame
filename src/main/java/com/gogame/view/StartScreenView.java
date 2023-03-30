package com.gogame.view;

import com.gogame.controller.StartScreenController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class StartScreenView extends View {
    //region Fields
    // Set style of button
    private final String STYLE = """
                        -fx-background-color:
                        linear-gradient(#ffd65b, #e68400),
                        linear-gradient(#ffef84, #f2ba44),
                        linear-gradient(#ffea6a, #efaa22),
                        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),
                        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));
                -fx-background-radius: 30;
                -fx-background-insets: 0,1,2,3,0;
                -fx-text-fill: #654b00;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                -fx-padding: 10 20 10 20;
            """;

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
        startGame.setStyle(STYLE);
        startGame.setOnMouseClicked(e -> controller.changeSceneToGameSettingsScreen());

        Button tutorial = new Button("Tutorial");
        tutorial.setStyle(STYLE);
        tutorial.setOnMouseClicked(e -> System.out.println("Tutorial button clicked!"));

        FlowPane pa = new FlowPane(startGame, tutorial);
        pa.setAlignment(Pos.CENTER);
        pane.setCenter(pa);
    }
    //endregion
}
