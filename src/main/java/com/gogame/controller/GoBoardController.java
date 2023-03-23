package com.gogame.controller;

import com.gogame.model.*;
import com.gogame.view.GoBoardView;
import com.gogame.view.StartScreenView;
import com.gogame.view.WinScreenView;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GoBoardController {
    //region Fields
    // MVC variables
    private GoBoardModel model;
    private GoBoardView view;
    //endregion

    // Constructor
    public GoBoardController(GoBoardModel model, GoBoardView view) {
        this.model = model;
        this.view = view;
    }

    //region Getter/Setter
    public void setModel(GoBoardModel model) {
        this.model = model;
    }

    public void setView(GoBoardView view) {
        this.view = view;
    }
    //endregion

    //region Methods
    public void mouseClicked(MouseEvent e) {
        //System.out.println("controller clicked at X: " + e.getX() + " Y: " + e.getY() + " TileSize: " + view.getScale());
        int x = (int) ((e.getX()) / view.getScale());
        int y = (int) ((e.getY()) / view.getScale());
        System.out.println(x + " " + y);
        model.makeMove(x,y);
        view.draw();
    }

    public void resetModel(){
        model.reset();
        view.draw();
    }

    public void passPlayer() {
        System.out.println("Player " + model.getCurrentPlayer() + " passed!");
        model.pass();
    }

    public void changeSceneToWinScreen() {
        // Switch player to get the winner
        model.switchPlayer();
        WinScreenView nextView = new WinScreenView(model.getCurrentPlayer().toString());
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage) {
            Stage s = (Stage) w;
            s.setScene(new Scene(nextView.getPane(),500,600));
        }
    }

    public void openImportFile() {

    }
    //endregion
}
