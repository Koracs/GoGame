package com.gogame;


import com.gogame.view.*;
import com.gogame.controller.*;
import com.gogame.model.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
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
