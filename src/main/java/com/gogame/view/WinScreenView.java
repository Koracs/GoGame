package com.gogame.view;

import com.gogame.controller.WinScreenController;
import com.gogame.listener.GameState;
import com.gogame.model.Stone;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class WinScreenView extends View {
    //region Fields
    private final GameState winner;

    // Pane of this class
    private BorderPane pane;

    // MVC variables
    private WinScreenController controller;
    //endregion

    // Constructor
    public WinScreenView(GameState winner) {
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

        Text winText = new Text(winner.toString());


        Button homeMenu = new Button("Home menu");
        homeMenu.setOnMouseClicked(e -> controller.changeSceneToStartScreen());

        Button exitGame = new Button("Exit Game");
        exitGame.setOnMouseClicked(e -> controller.exitGame());

        FlowPane pa = new FlowPane(homeMenu,exitGame);
        pa.setAlignment(Pos.CENTER);
        pa.setPadding(new Insets(30));
        pa.setHgap(10);
        pa.setVgap(10);


        pane.setCenter(winText);
        pane.setBottom(pa);
    }

    //endregion
}
