package com.gogame.controller;

import com.gogame.view.GameScreenView;
import com.gogame.view.WinScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GameScreenController {
    private GameScreenView view;

    public GameScreenController(GameScreenView view){
        this.view = view;
    }


    public void changeSceneToWinScreen() {
        // Switch player to get the winner
        //model.switchPlayer();
        WinScreenView nextView = new WinScreenView("IMPLEMENT"); //Todo implement winner via gameState
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage) {
            Stage s = (Stage) w;
            s.setScene(new Scene(nextView.getPane(),500,600));
        }
    }

    public void openImportFile() {

    }
}
