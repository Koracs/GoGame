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
        homeMenu.setOnMouseClicked(e -> controller.changeSceneToStartScreen());

        pane.setCenter(winText);
        pane.setBottom(homeMenu);
    }

    //endregion
}
