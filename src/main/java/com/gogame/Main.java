package com.gogame;


import com.gogame.view.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler(Main::handleException);

        StartScreenView startScreenView = new StartScreenView();
        Scene scene = new Scene(startScreenView.getPane(), 500, 500);
        scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Go Game");
        stage.show();
        stage.setMinHeight(500);
        stage.setMinWidth(500);
    }


    private static void handleException(Thread t, Throwable e) {
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Runtime Exception");
            alert.setHeaderText(null);
            alert.initStyle(StageStyle.UTILITY);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } else {
            System.err.println("An unexpected error occurred in Thread: " + t + " Error:" + e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
