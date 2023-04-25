package com.gogame.view;

import com.gogame.controller.GoBoardController;
import com.gogame.controller.TutorialController;
import com.gogame.model.GoBoardModel;
import com.gogame.model.SaveGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TutorialView extends View{

    private final GoBoardModel goBoardModel;
    private final TutorialController tutorialScreenController;
    private final GoBoardController goBoardController;

    private final GoBoardView goBoardView;
    private final SaveGame saveGame;
    private BorderPane pane;

    private final GameStateField gameState;
    private final CaptureStatus captureStatus;


    public TutorialView(GoBoardModel model) {
        this.goBoardModel = model;
        this.goBoardView = new GoBoardView(model);
        this.goBoardController = goBoardView.getController();
        this.tutorialScreenController = new TutorialController(this, model, goBoardController);
        this.saveGame = new SaveGame(goBoardController);

        gameState = new GameStateField(model);
        captureStatus = new CaptureStatus(model);

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
        pane.setBottom(gameState);

        VBox interactionField = new VBox();
        FlowPane interactionButtons = new FlowPane();
        interactionField.getChildren().add(captureStatus);
        interactionField.getChildren().add(interactionButtons);
        pane.setLeft(interactionField);




        // Buttons for tutorial interaction
        Button backButton = new Button("<--");
        backButton.setOnMouseClicked(e -> saveGame.loadGradually(false));
        Button forwardButton = new Button("-->");
        forwardButton.setOnMouseClicked(e -> saveGame.loadGradually(true));

        interactionButtons.setPadding(new Insets(30));
        interactionButtons.setPrefWidth(20);
        interactionButtons.setHgap(10);
        interactionButtons.setVgap(10);
        interactionButtons.setAlignment(Pos.CENTER);

        interactionButtons.getChildren().add(backButton);
        interactionButtons.getChildren().add(forwardButton);

        // Buttons to import/export games
        MenuBar menuBar = new MenuBar();
        SeparatorMenuItem separator = new SeparatorMenuItem();
        Menu game = new Menu("Game");

        MenuItem restartButton = new MenuItem("Restart game");
        restartButton.setOnAction(e -> goBoardController.resetModel());
        MenuItem mainMenuButton = new MenuItem("Main menu");
        mainMenuButton.setOnAction(e -> tutorialScreenController.changeSceneToStartScreen());

        game.getItems().add(restartButton);
        game.getItems().add(separator);
        game.getItems().add(mainMenuButton);

        menuBar.getMenus().add(game);

        pane.setTop(menuBar);
    }
}
