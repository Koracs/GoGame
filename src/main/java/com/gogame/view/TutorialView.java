package com.gogame.view;

import com.gogame.controller.GoBoardController;
import com.gogame.controller.TutorialController;
import com.gogame.model.GoBoardModel;
import com.gogame.savegame.SaveGameHandler;
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

import java.util.Optional;

public class TutorialView extends View {

    private final GoBoardModel goBoardModel;
    private final TutorialController tutorialController;
    private final GoBoardController goBoardController;

    private final GoBoardView goBoardView;
    private BorderPane pane;

    private final GameStateField gameState;
    private final CaptureStatus captureStatus;


    public TutorialView(SaveGameHandler saveGame) {
        this.tutorialController = new TutorialController(this, saveGame);

        this.goBoardModel = saveGame.createTutorialModel();
        this.goBoardView = new GoBoardView(goBoardModel);
        this.goBoardController = goBoardView.getController();

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
        VBox.setVgrow(interactionButtons, Priority.ALWAYS);
        pane.setLeft(interactionField);


        // Buttons for tutorial interaction
        Button backButton = new Button("");
        Image image = new Image(getClass().getResourceAsStream("/pictures/left-arrow.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(35);
        imageView.setFitHeight(35);
        backButton.setGraphic(imageView);
        backButton.setOnMouseClicked(e -> tutorialController.lastMove());
        Button forwardButton = new Button("");
        image = new Image(getClass().getResourceAsStream("/pictures/right-arrow.png"));
        imageView = new ImageView(image);
        imageView.setFitWidth(35);
        imageView.setFitHeight(35);
        forwardButton.setGraphic(imageView);
        forwardButton.setOnMouseClicked(e -> tutorialController.nextMove());

        interactionButtons.setPadding(new Insets(30));
        interactionButtons.setPrefWidth(20);
        interactionButtons.setHgap(15);
        interactionButtons.setVgap(15);
        interactionButtons.setAlignment(Pos.CENTER);

        interactionButtons.getChildren().add(backButton);
        interactionButtons.getChildren().add(forwardButton);

        // Buttons to import/export games
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("_File");
        Menu game = new Menu("_Game");

        MenuItem restartButton = new MenuItem("_Restart Tutorial");
        restartButton.setOnAction(e -> {
            tutorialController.resetTutorial();
        });
        file.getItems().add(restartButton);
        restartButton.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));

        MenuItem tutorialScreenButton = new MenuItem("Show _Tutorials");
        tutorialScreenButton.setOnAction(e -> showTutorials());
        tutorialScreenButton.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        file.getItems().add(tutorialScreenButton);

        MenuItem gameScreenButton = new MenuItem("_Game Screen");
        gameScreenButton.setOnAction(e -> tutorialController.changeSceneToStartScreen());
        file.getItems().add(gameScreenButton);
        file.getItems().add(new SeparatorMenuItem());

        MenuItem exitGame = new MenuItem("E_xit");
        exitGame.setOnAction(e -> Platform.exit());
        exitGame.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        file.getItems().add(exitGame);


        MenuItem lastMove = new MenuItem("_Last Move");
        lastMove.setOnAction(e -> tutorialController.lastMove());
        lastMove.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
        game.getItems().add(lastMove);

        MenuItem nextMove = new MenuItem("_Next Move");
        nextMove.setOnAction(e -> tutorialController.nextMove());
        nextMove.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        game.getItems().add(nextMove);
        game.getItems().add(new SeparatorMenuItem());

        CheckMenuItem showMoveHistory = new CheckMenuItem("Show Move _History");
        showMoveHistory.setOnAction(e -> goBoardController.setDrawMoveHistory(showMoveHistory.isSelected()));
        showMoveHistory.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        game.getItems().add(showMoveHistory);


        menuBar.getMenus().add(file);
        menuBar.getMenus().add(game);

        pane.setTop(menuBar);
    }

    private void showTutorials() {
        TutorialDialog tutorialDialog = new TutorialDialog();
        Optional<ButtonType> result = tutorialDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            tutorialController.changeSceneToTutorialScene(tutorialDialog.getSelectedTutorial());

        }
    }
}
