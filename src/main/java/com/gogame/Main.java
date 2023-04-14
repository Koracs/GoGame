package com.gogame;


import com.gogame.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        StartScreenView startScreenView = new StartScreenView();
        Scene scene = new Scene(startScreenView.getPane(),500,500);
        scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Go Game");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
