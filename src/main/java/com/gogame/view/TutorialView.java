package com.gogame.view;

import com.gogame.controller.GoBoardController;
import com.gogame.controller.TutorialController;
import com.gogame.model.GoBoardModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class TutorialView extends View{

    private final GoBoardModel goBoardModel;
    private final TutorialController tutorialScreenController;
    private final GoBoardController goBoardController;

    private final GoBoardView goBoardView;
    private BorderPane pane;


    public TutorialView(GoBoardModel model) {
        tutorialScreenController = new TutorialController(this, model);

        goBoardModel = model;
        goBoardView = new GoBoardView(model);
        goBoardController = goBoardView.getController();

        drawScene();
    }

    @Override
    public BorderPane getPane() {
        return this.pane;
    }

    @Override
    protected void drawScene() {
        pane = new BorderPane();
        pane.setCenter(goBoardView);


        // Buttons for tutorial interaction
        Button backButton = new Button("<--");
        backButton.setOnMouseClicked(e -> System.out.println("Last Move"));
        Button forwardButton = new Button("-->");
        forwardButton.setOnMouseClicked(e -> System.out.println("Next Move"));

        FlowPane interactionButtons = new FlowPane();
        interactionButtons.setPadding(new Insets(30));
        interactionButtons.setHgap(10);
        interactionButtons.setVgap(10);
        interactionButtons.setAlignment(Pos.CENTER);

        interactionButtons.getChildren().add(backButton);
        interactionButtons.getChildren().add(forwardButton);

        pane.setBottom(interactionButtons);
    }
}
