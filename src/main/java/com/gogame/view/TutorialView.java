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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

/**
 * The TutorialView acts as the interaction class for tutorials.
 */
public class TutorialView extends View {

    private final GoBoardModel goBoardModel;
    private final TutorialController tutorialController;
    private final GoBoardController goBoardController;

    private final GoBoardView goBoardView;
    private BorderPane pane;

    private final GameStateField gameState;
    private final CaptureStatus captureStatus;


    /**
     * Constructs a new TutorialView. This view includes a GoBoardView, a GameState and a CaptureStatus UI element
     * @param saveGame SaveGameHandler with the corresponding tutorial.
     */
    public TutorialView(SaveGameHandler saveGame) {
        this.tutorialController = new TutorialController(this, saveGame);

        this.goBoardModel = saveGame.createTutorialModel();
        this.goBoardView = new GoBoardView(goBoardModel);
        this.goBoardController = goBoardView.getController();

        gameState = new GameStateField(goBoardModel);
        captureStatus = new CaptureStatus(goBoardModel);

        //eventHandler for keyboard interaction
        goBoardView.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT) {
                tutorialController.lastMove();
            } else if(keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT) {
                tutorialController.nextMove();
            }
        });

        drawScene();
    }

    @Override
    public BorderPane getPane() {
        pane.setFocusTraversable(true);
        pane.getCenter().requestFocus();
        return this.pane;
    }

    /**
     * Draws the Scene used to display the current tutorial and its information.
     */
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
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/left-arrow.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(35);
        imageView.setFitHeight(35);
        backButton.setGraphic(imageView);
        backButton.setOnMouseClicked(e -> tutorialController.lastMove());
        backButton.setFocusTraversable(false);
        Button forwardButton = new Button("");
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/right-arrow.png")));
        imageView = new ImageView(image);
        imageView.setFitWidth(35);
        imageView.setFitHeight(35);
        forwardButton.setGraphic(imageView);
        forwardButton.setOnMouseClicked(e -> tutorialController.nextMove());
        forwardButton.setFocusTraversable(false);

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
        Menu help = new Menu("_Help");

        menuBar.getMenus().add(file);
        menuBar.getMenus().add(game);
        menuBar.getMenus().add(help);

        MenuItem restartButton = new MenuItem("_Restart Tutorial");
        restartButton.setOnAction(e -> tutorialController.resetTutorial());
        file.getItems().add(restartButton);
        restartButton.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));

        MenuItem openGame = new MenuItem("_Open Own Tutorial");
        openGame.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File selectedFile = fileChooser.showOpenDialog(this.pane.getScene().getWindow());
            if (selectedFile != null) {
                tutorialController.changeTutorial(selectedFile);
            }
        });
        file.getItems().add(openGame);
        file.getItems().add(new SeparatorMenuItem());
        openGame.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

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
        lastMove.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        game.getItems().add(lastMove);

        MenuItem nextMove = new MenuItem("_Next Move");
        nextMove.setOnAction(e -> tutorialController.nextMove());
        nextMove.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        game.getItems().add(nextMove);
        game.getItems().add(new SeparatorMenuItem());

        CheckMenuItem showMoveHistory = new CheckMenuItem("Show Move _History");
        showMoveHistory.setOnAction(e -> goBoardController.setDrawMoveHistory(showMoveHistory.isSelected()));
        showMoveHistory.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        game.getItems().add(showMoveHistory);

        MenuItem howToPlay = new MenuItem("_How to use");
        Alert howToPlayDialog = new Alert(Alert.AlertType.INFORMATION);
        howToPlayDialog.setTitle("How to use tutorials");
        howToPlayDialog.setHeaderText(null);
        howToPlayDialog.setContentText("""
                Controls:
                Show next move: D, Right-Arrow
                Show last move: A, Left-Arrow
                
                Restart: CTRL+R
                """);

        howToPlay.setOnAction(e -> howToPlayDialog.showAndWait());
        help.getItems().add(howToPlay);


        MenuItem aboutUs = new MenuItem("_About us");
        Alert aboutUsDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutUsDialog.setTitle("About us");
        aboutUsDialog.setHeaderText("Go Game - PR SE SS2023 - Group 5");
        aboutUsDialog.setContentText("Made by: \nDominik Niederberger, Felix Stadler, Simon Ulmer");
        aboutUs.setOnAction(e -> aboutUsDialog.showAndWait());
        help.getItems().add(aboutUs);



        pane.setTop(menuBar);
    }

    /**
     * Shows the tutorials dialog and allows the user to select a tutorial. If the user confirms the selection,
     * the scene is changed to the tutorial scene corresponding to the selected tutorial.
     */
    private void showTutorials() {
        TutorialDialog tutorialDialog = new TutorialDialog();
        Optional<ButtonType> result = tutorialDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && tutorialDialog.getSelectedTutorial() != null) {
            tutorialController.changeTutorial(tutorialDialog.getSelectedTutorial());

        }
    }
}
