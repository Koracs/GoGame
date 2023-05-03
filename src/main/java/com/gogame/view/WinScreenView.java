package com.gogame.view;

import com.gogame.controller.WinScreenController;
import com.gogame.listener.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class WinScreenView extends View {
    //region Fields
    private final GameState winner;
    private final double scoreBlack;
    private final double scoreWhite;

    // Pane of this class
    private BorderPane pane;

    // MVC variables
    private WinScreenController controller;
    //endregion

    // Constructor
    public WinScreenView(GameState winner, double scoreBlack, double scoreWhite) {
        this.winner = winner;
        this.scoreBlack = scoreBlack;
        this.scoreWhite = scoreWhite;
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
        winText.setFont(Font.font ("Verdana", FontWeight.BOLD, 40));

        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        winText.setEffect(ds);

        Text scoreWhiteText = new Text("White: " + scoreWhite);
        scoreWhiteText.setFont(Font.font ("Verdana", 15));
        Text scoreBlackText = new Text("Black: " + scoreBlack);
        scoreBlackText.setFont(Font.font ("Verdana", 15));
        HBox hBox = new HBox(scoreBlackText, scoreWhiteText);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);

        Button homeMenu = new Button("Home menu");
        homeMenu.setOnMouseClicked(e -> controller.changeSceneToStartScreen());

        Button exitGame = new Button("Exit Game");
        exitGame.setOnMouseClicked(e -> controller.exitGame());

        FlowPane pa = new FlowPane(homeMenu,exitGame);
        pa.setAlignment(Pos.CENTER);
        pa.setPadding(new Insets(30));
        pa.setHgap(10);
        pa.setVgap(10);

        VBox vBox = new VBox(winText, hBox);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        pane.setCenter(vBox);
        pane.setBottom(pa);
    }

    //endregion
}
