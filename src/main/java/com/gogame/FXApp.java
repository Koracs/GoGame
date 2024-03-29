package com.gogame;


import com.gogame.model.GoBoardModel;
import com.gogame.view.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * Stating class of the JavaFX Application. Launches the game view with a default model.
 */
public class FXApp extends Application {
    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler(FXApp::handleException);

        GameScreenView gameScreenView = new GameScreenView(GoBoardModel.getDefaultModel());
        Scene scene = new Scene(gameScreenView.getPane());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylesheet.css")).toExternalForm());
        stage.setScene(scene);

        BorderPane root = (BorderPane) stage.getScene().getRoot();
        root.getCenter().requestFocus();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/icon.png"))));
        stage.setTitle("Go Game");

        stage.show();
        stage.setMinHeight(500);
        stage.setMinWidth(575);
    }

    /**
     * Displays uncaught exceptions in the application through an error dialog.
     * @param t The thread in which the exception occurred.
     * @param e The Throwable object representing the exception.
     */
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

    /**
     * Launches the JavaFX Application
     * @param args Arguments from the Main Method
     */
    public static void main(String[] args) {
        launch(args);
    }
}
