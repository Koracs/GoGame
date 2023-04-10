package com.gogame.controller;

import com.gogame.listener.GameState;
import com.gogame.view.GameScreenView;
import com.gogame.view.WinScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GameScreenController {
    private final GameScreenView view;

    public GameScreenController(GameScreenView view){
        this.view = view;
    }


    public void changeSceneToWinScreen(GameState gameState) {
        // Switch player to get the winner
        //model.switchPlayer();
        WinScreenView nextView = new WinScreenView(gameState.toString()); //Todo implement winner via gameState
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage) {
            Stage s = (Stage) w;
            Scene scene = new Scene(nextView.getPane(), 500, 600);
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            s.setScene(scene);
        }
    }

    public void openImportFile() {

    }
}
