package com.gogame.view;

import com.gogame.controller.TutorialSettingsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TutorialSettingsView extends View {
    //region Fields
    private BorderPane pane;
    private final TutorialSettingsController controller;
    private List<String> tutorials;
    //endregion

    // Constructor
    public TutorialSettingsView() {
        controller = new TutorialSettingsController(this);

        tutorials = new ArrayList<>();
        tutorials.add("tutorial 1"); //todo replace with parser of game files (felix)
        tutorials.add("tutorial 2");
        tutorials.add("tutorial 3");
        tutorials.add("tutorial 4");

        drawScene();
    }

    //region Getter/Setter
    @Override
    public BorderPane getPane() {
        return this.pane;
    }
    //endregion


    //region Methods
    @Override
    protected void drawScene() {
        pane = new BorderPane();
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.TOP_CENTER);
        flowPane.setPadding(new Insets(30));
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        pane.setCenter(flowPane);

        ToggleGroup tutorialGroup = new ToggleGroup();

        for (String tutorial : tutorials) {
            ToggleButton button = new ToggleButton(tutorial);
            button.setToggleGroup(tutorialGroup);
            flowPane.getChildren().add(button);
            //todo implement controller (or select only on "start game")
        }

        // Buttons
        Button startGame = new Button("Start game");
        startGame.setOnMouseClicked(e -> controller.changeSceneToTutorialScene()); //todo Implement logic

        Button importGame = new Button("Import game");
        importGame.setOnMouseClicked(e -> System.out.println("Import game")); //todo Implement logic

        FlowPane pa = new FlowPane(startGame, importGame);
        pa.setPadding(new Insets(30));
        pa.setHgap(10);
        pa.setVgap(10);
        pa.setAlignment(Pos.CENTER);

        pane.setBottom(pa);
    }
    //endregion
}
