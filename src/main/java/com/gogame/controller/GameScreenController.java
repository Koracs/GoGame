package com.gogame.controller;

import com.gogame.listener.GameState;
import com.gogame.model.GoBoardModel;
import com.gogame.model.Stone;
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


    public void changeSceneToWinScreen(Stone currentPlayer) {
        // Switch player to get the winner
        //model.switchPlayer();
        WinScreenView nextView = new WinScreenView(currentPlayer); //Todo implement winner via gameState
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }
    //endregion
}
