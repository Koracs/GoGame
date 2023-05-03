package com.gogame;


import com.gogame.view.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler(Main::handleException);

        StartScreenView startScreenView = new StartScreenView();
        Scene scene = new Scene(startScreenView.getPane(), 500, 500);
        scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
        //scene.setOnKeyPressed(KeyHandler::handleKeyPressed);
        stage.setScene(scene);
        stage.setTitle("Go Game");

        stage.show();
        stage.setMinHeight(500);
        stage.setMinWidth(500);
    }


    private static void handleException(Thread t, Throwable e) {
        if (Platform.isFxApplicationThread()) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Runtime Exception");
            alert.setHeaderText(null);

            alert.setContentText(e.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("Exception stacktrace:");
            TextArea exceptionArea = new TextArea(exceptionText);
            exceptionArea.setEditable(false);
            exceptionArea.setWrapText(true);

            BorderPane stackTracePane = new BorderPane();
            stackTracePane.setMaxWidth(Double.MAX_VALUE);
            stackTracePane.setTop(label);
            stackTracePane.setCenter(exceptionArea);

            alert.getDialogPane().setExpandableContent(stackTracePane);
            alert.showAndWait();
        } else {
            System.err.println("An unexpected error occurred in Thread: " + t + " Error:" + e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
