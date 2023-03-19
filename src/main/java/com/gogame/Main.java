package com.gogame;


import com.gogame.view.*;
import com.gogame.controller.*;
import com.gogame.model.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class Main extends Application {
    private GoBoardView board;
    private GoBoardController controller;
    private GoBoardModel model;

    private int size = 19;



    @Override
    public void start(Stage stage) throws Exception {
        board = new GoBoardView(size);
        controller = new GoBoardController();
        model = new GoBoardModel(size);

        model.registerView(board);
        controller.setModel(model);
        board.setActionListener(controller);

        stage.setTitle("Go Game");
        BorderPane pane = new BorderPane();
        pane.setCenter(board);
        FlowPane buttonPane = new FlowPane();
        pane.setBottom(buttonPane);


        Button resetButton = new Button("Reset");
        resetButton.setOnMouseClicked(e -> System.out.println("Reset"));
        Button passButton = new Button("Pass");
        passButton.setOnMouseClicked(e -> System.out.println("Pass"));

        buttonPane.setPadding(new Insets(30,30,30,30));
        buttonPane.setHgap(10);
        buttonPane.setVgap(10);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.getChildren().add(resetButton);
        buttonPane.getChildren().add(passButton);


        pane.widthProperty().addListener((obs, oldVal, newVal) -> {
            resize(newVal.doubleValue(), pane.getHeight());
        });
        pane.heightProperty().addListener((obs, oldVal, newVal) -> {
            resize(pane.getWidth(), newVal.doubleValue());
        });



        stage.setScene(new Scene(pane,Screen.getPrimary().getBounds().getWidth()/2,Screen.getPrimary().getBounds().getHeight()/2));
        stage.show();
    }

    public void resize(double width, double height) {
        board.setScale(Math.min(width, height));
        //System.out.println("Scale:" + board.getScale());
        board.draw();
    }

    public static void main(String[] args) {
        launch();
    }
}
