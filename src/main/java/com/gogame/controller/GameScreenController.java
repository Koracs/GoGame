package com.gogame.controller;

import com.gogame.listener.GameState;
import com.gogame.model.GoBoardModel;
import com.gogame.view.GameScreenView;
import com.gogame.view.WinScreenView;
import javafx.scene.Scene;
import javafx.stage.*;

public class GameScreenController {
    //region Fields
    private final GameScreenView view;
    private final GoBoardModel model;
    //endregion

    // Constructor
    public GameScreenController(GameScreenView view, GoBoardModel model) {
        this.view = view;
        this.model = model;
    }

    //region Getter/Setter
    public GameScreenView getView() {
        return this.view;
    }
    //endregion

    //region Methods


    public void changeSceneToWinScreen(GameState gameState) {
        // Switch player to get the winner
        //model.switchPlayer();
        WinScreenView nextView = new WinScreenView(gameState.toString()); //Todo implement winner via gameState
        Window w = view.getPane().getScene().getWindow();
        if (w instanceof Stage) {
            Stage s = (Stage) w;
            Scene scene = new Scene(nextView.getPane(), 500, 600);
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            s.setScene(scene);
        }
    }
    //endregion
}
