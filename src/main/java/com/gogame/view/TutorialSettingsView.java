package com.gogame.view;

import com.gogame.controller.TutorialSettingsController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TutorialSettingsView extends View{
    //region Fields
    private BorderPane pane;

    private final TutorialSettingsController controller;

    private List<Button> tutorialButtons;
    //endregion

    // Constructor
    public TutorialSettingsView() {
        controller = new TutorialSettingsController(this);
        tutorialButtons = new ArrayList<>();

        drawScene();
    }

    //region Getter/Setter

    //endregion
    @Override
    public BorderPane getPane() {
        return this.pane;
    }
    //region Methods

    @Override
    protected void drawScene() {
        pane = new BorderPane();

        // Implement test tutorials
        //todo CSS implementieren - auswählen und icon
        Button tutorial1 = new Button("Tutorial 1");
        tutorialButtons.add(tutorial1);
        tutorial1.getStyleClass().add("tutorialButton");
        tutorial1.setOnMouseClicked(e -> {
            controller.selectTutorial(1);
            selectTutorialButton(1);
        });

        Button tutorial2 = new Button("Tutorial 2");
        tutorialButtons.add(tutorial2);
        tutorial2.getStyleClass().add("tutorialButton");
        tutorial2.setOnMouseClicked(e -> {
            controller.selectTutorial(2);
            selectTutorialButton(2);
        });

        Button tutorial3 = new Button("Tutorial 3");
        tutorialButtons.add(tutorial3);
        tutorial3.getStyleClass().add("tutorialButton");
        tutorial3.setOnMouseClicked(e -> {
            controller.selectTutorial(3);
            selectTutorialButton(3);
        });

        Button tutorial4 = new Button("Tutorial 4");
        tutorialButtons.add(tutorial4);
        tutorial4.getStyleClass().add("tutorialButton");
        tutorial4.setOnMouseClicked(e -> {
            controller.selectTutorial(4);
            selectTutorialButton(4);
        });

        VBox vBox1 = new VBox(tutorial1, tutorial2);
        vBox1.setSpacing(20);
        VBox vBox2 = new VBox(tutorial3, tutorial4);
        vBox2.setSpacing(20);
        HBox hBox = new HBox(vBox1, vBox2);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);

        pane.setCenter(hBox);

        // Buttons
        Button startGame = new Button("Start game");
        startGame.setOnMouseClicked(e -> System.out.println("Start game with " + controller.getSelectedTutorial())); //todo Implement logic

        Button importGame = new Button("Import game");
        importGame.setOnMouseClicked(e -> System.out.println("Import game")); //todo Implement logic

        FlowPane pa = new FlowPane(startGame, importGame);
        pa.setHgap(10);
        pa.setAlignment(Pos.CENTER);

        pane.setBottom(pa);
    }

    private void selectTutorialButton(int i) {
        tutorialButtons.forEach(b -> {
            b.getStyleClass().remove("tutorialButtonSelected");
            b.getStyleClass().add("tutorialButton");
        });
        tutorialButtons.get(i-1).getStyleClass().add("tutorialButtonSelected");
    }
    //endregion
}
