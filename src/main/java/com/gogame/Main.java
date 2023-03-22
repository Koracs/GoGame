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
    private GoBoardView board;
    private GoBoardController controller;
    private GoBoardModel model;

    private BorderPane pane;


    private int size = 19;



    @Override
    public void start(Stage stage) throws Exception {
        StartScreenView board = new StartScreenView();
        Scene scene = new Scene(board.getPane(),500,600);
        stage.setScene(scene);
        stage.setTitle("Go Game");
        stage.show();

        /*board = new GoBoardView(size);
        controller = new GoBoardController();
        model = new GoBoardModel(size);

        model.registerView(board);
        controller.setModel(model);
        board.setActionListener(controller);
        board.setModel(model);

        stage.setTitle("Go Game");
        pane = new BorderPane();
        pane.setCenter(board);
        FlowPane gameplayButtonPane = new FlowPane();
        pane.setBottom(gameplayButtonPane);

        // Buttons for gameplay
        Button resetButton = new Button("Reset");
        resetButton.setOnMouseClicked(e -> controller.resetModel());
        Button passButton = new Button("Pass");
        passButton.setOnMouseClicked(e -> controller.passPlayer());
        Button resignButton = new Button("Resign");
        resignButton.setOnMouseClicked(e -> controller.resignCurrentPlayer());

        gameplayButtonPane.setPadding(new Insets(30,30,30,30));
        gameplayButtonPane.setHgap(10);
        gameplayButtonPane.setVgap(10);
        gameplayButtonPane.setAlignment(Pos.CENTER);
        gameplayButtonPane.getChildren().add(resetButton);
        gameplayButtonPane.getChildren().add(passButton);
        gameplayButtonPane.getChildren().add(resignButton);

        // Buttons to import/export games
        // --------------------- vielleicht ändern auf menu bar ---------------------
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Game");

        MenuItem importButton = new MenuItem("Import game");
        importButton.setOnAction(e -> controller.openImportFile());
        MenuItem exportButton = new MenuItem("Export game");
        exportButton.setOnAction(e -> System.out.println("Export game"));

        menu.getItems().add(importButton);
        menu.getItems().add(exportButton);


        menuBar.getMenus().add(menu);
        pane.setTop(menuBar);

        pane.widthProperty().addListener((obs, oldVal, newVal) -> {
            resize(newVal.doubleValue(), pane.getHeight());
        });
        pane.heightProperty().addListener((obs, oldVal, newVal) -> {
            resize(pane.getWidth(), newVal.doubleValue());
        });

        stage.setScene(new Scene(pane,500,600));
        stage.show();*/
    }

    /*
    public void resize(double width, double height) {
        board.setScale(Math.min(width, height));
        //System.out.println("Scale:" + board.getScale());
        board.draw();
    }*/

    public static void main(String[] args) {
        launch();
    }
}
