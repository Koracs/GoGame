package com.gogame.view;

import com.gogame.controller.TutorialSettingsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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

        tutorials = controller.getTutorials();

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
        boolean first = true;

        for (String tutorial : tutorials) {
            ToggleButton button = new ToggleButton(tutorial);
            button.setToggleGroup(tutorialGroup);
            if(first) {
                tutorialGroup.selectToggle(button);
                first = false;
            }
            flowPane.getChildren().add(button);
            //todo implement controller (or select only on "start game")
        }

        // Buttons
        Button startGame = new Button("Start game");
        startGame.setOnMouseClicked(e -> controller.changeSceneToTutorialScene(tutorialGroup.getSelectedToggle().toString().split("'")[1]));

        //Button importGame = new Button("Import game"); //todo necessary??
        //importGame.setOnMouseClicked(e -> System.out.println("Import game")); //todo Implement logic

        FlowPane pa = new FlowPane(startGame);
        pa.setPadding(new Insets(30));
        pa.setHgap(10);
        pa.setVgap(10);
        pa.setAlignment(Pos.CENTER);

        // Back Button
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(10));
        Button backButton = new Button("Back");
        Image image = new Image(getClass().getResourceAsStream("/pictures/backArrow.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(backButton.getFont().getSize());
        imageView.setFitHeight(backButton.getFont().getSize());
        backButton.setGraphic(imageView);
        backButton.setOnMouseClicked(e -> controller.changeSceneToStartScreen());
        topBox.getChildren().add(backButton);

        pane.setBottom(pa);
        pane.setTop(topBox);
    }
    //endregion
}
