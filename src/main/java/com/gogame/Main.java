package com.gogame;


import com.gogame.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        StartScreenView board = new StartScreenView();
        Scene scene = new Scene(board.getPane(),500,600);
        stage.setScene(scene);
        stage.setTitle("Go Game");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
