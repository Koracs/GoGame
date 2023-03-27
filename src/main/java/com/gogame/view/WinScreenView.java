package com.gogame.view;

import com.gogame.controller.WinScreenController;
import com.gogame.model.WinScreenModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

public class WinScreenView extends Parent {
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
    private WinScreenController controller;
    private WinScreenModel model;
    //endregion

    // Constructor
    public WinScreenView(String winner) {
        model = new WinScreenModel(this, winner);
        controller = new WinScreenController(model, this);
        drawScene();
    }

    //region Getter/Setter
    public BorderPane getPane() {
        return pane;
    }

    public void setController(WinScreenController controller) {
        this.controller = controller;
    }

    public void setModel(WinScreenModel model) {
        this.model = model;
    }
    //endregion


    //region Methods
    private void drawScene() {
        pane = new BorderPane();

        Text winText = new Text("Player " + model.getWinner() + " wins!");
        Button homeMenu = new Button("Home menu");
        homeMenu.setStyle(STYLE);
        homeMenu.setOnMouseClicked(e -> controller.changeSceneToStartScreen());

        pane.setCenter(winText);
        pane.setBottom(homeMenu);
    }

    //endregion
}
