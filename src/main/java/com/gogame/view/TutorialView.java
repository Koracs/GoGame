package com.gogame.view;

import com.gogame.controller.GoBoardController;
import com.gogame.controller.TutorialController;
import com.gogame.model.GoBoardModel;
import com.gogame.model.SaveGame;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;

public class TutorialView extends View{

    private final GoBoardModel goBoardModel;
    private final TutorialController tutorialController;
    private final GoBoardController goBoardController;

    private final GoBoardView goBoardView;
    private final SaveGame saveGame;
    private BorderPane pane;

    private final GameStateField gameState;
    private final CaptureStatus captureStatus;


    public TutorialView(String selectedTutorial) {
        this.saveGame = new SaveGame(selectedTutorial);
        this.goBoardModel = saveGame.importFileData();
        this.goBoardView = new GoBoardView(goBoardModel);
        this.goBoardController = goBoardView.getController();
        this.tutorialController = new TutorialController(this, goBoardModel, goBoardController);
        saveGame.initSaveGame(goBoardController);

        gameState = new GameStateField(goBoardModel);
        captureStatus = new CaptureStatus(goBoardModel);

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
        Button backButton = new Button("");
        Image image = new Image(getClass().getResourceAsStream("/pictures/left-arrow.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(35);
        imageView.setFitHeight(35);
        backButton.setGraphic(imageView);
        backButton.setOnMouseClicked(e -> saveGame.loadGradually(false));
        Button forwardButton = new Button("");
        image = new Image(getClass().getResourceAsStream("/pictures/right-arrow.png"));
        imageView = new ImageView(image);
        imageView.setFitWidth(35);
        imageView.setFitHeight(35);
        forwardButton.setGraphic(imageView);
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
        Menu game = new Menu("Game");

        MenuItem restartButton = new MenuItem("Restart tutorial");
        restartButton.setOnAction(e -> {
            goBoardController.resetModel();
            saveGame.setIndex(1);
        });
        game.getItems().add(restartButton);
        restartButton.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));

        MenuItem tutorialScreenButton = new MenuItem("Tutorial screen");
        //todo tutorialScreenButton.setOnAction(e -> tutorialController.changeSceneToTutorialScreen());
        game.getItems().add(tutorialScreenButton);

        MenuItem gameScreenButton = new MenuItem("Game screen");
        gameScreenButton.setOnAction(e -> tutorialController.changeSceneToStartScreen());
        game.getItems().add(gameScreenButton);

        MenuItem exitGame = new MenuItem("Exit");
        exitGame.setOnAction(e -> Platform.exit());
        game.getItems().add(new SeparatorMenuItem());
        game.getItems().add(exitGame);
        exitGame.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

        menuBar.getMenus().add(game);

        pane.setTop(menuBar);
    }
}
