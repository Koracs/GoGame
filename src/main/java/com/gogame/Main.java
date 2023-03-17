package com.gogame;


import com.gogame.view.*;
import com.gogame.controller.*;
import com.gogame.model.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        GoBoardView view = new GoBoardView();
        GoBoardController controller = new GoBoardController();
        GoBoardModel model = new GoBoardModel();

        model.registerView(view);
        controller.setModel(model);
        view.setActionListener(controller);

        stage.setTitle("Go Game");
        stage.setScene(new Scene(view.getView()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
