package com.gogame.view;

import com.gogame.controller.WinScreenController;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class WinScreenView extends View {
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
    private final String winner;

    // Pane of this class
    private BorderPane pane;

    // MVC variables
    private WinScreenController controller;
    //endregion

    // Constructor
    public WinScreenView(String winner) {
        this.winner = winner;
        controller = new WinScreenController(this);
        drawScene();
    }

    //region Getter/Setter
    @Override
    public BorderPane getPane() {
        return pane;
    }

    public void setController(WinScreenController controller) {
        this.controller = controller;
    }

    //endregion


    //region Methods
    @Override
    protected void drawScene() {
        pane = new BorderPane();

        Text winText = new Text("Player " + winner + " wins!");
        Button homeMenu = new Button("Home menu");
        homeMenu.setStyle(STYLE);
        homeMenu.setOnMouseClicked(e -> controller.changeSceneToStartScreen());

        pane.setCenter(winText);
        pane.setBottom(homeMenu);
    }

    //endregion
}
