package com.gogame.view;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.model.GoBoardModel;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * The GameScreenView acts as the main interaction class for the user. It represents the starting point of the game
 */
public class GameScreenView extends View {
    //region Fields
    private final GoBoardModel goBoardModel;
    private final GameScreenController gameScreenController;
    private final GoBoardController goBoardController;
    private final GoBoardView goBoardView;

    private BorderPane pane;

    private final GameMenuBar menuBar;
    private final GameStateField gameState;
    private final CaptureStatus captureStatus;
    //endregion

    /**
     * Constructs a GameScreenView that is used as the main interaction point of the game
     * @param model GoBoardModel to be interacted with
     * @param file File that stores the Models information as a save game.
     */
    public GameScreenView(GoBoardModel model, File file) {
        gameScreenController = new GameScreenController(this, model, file);

        goBoardModel = model;
        goBoardView = new GoBoardView(goBoardModel);
        goBoardController = goBoardView.getController();

        menuBar = new GameMenuBar(goBoardController,gameScreenController);
        captureStatus = new CaptureStatus(goBoardModel);
        gameState = new GameStateField(goBoardModel);


        //eventHandler for placing stones
        EventHandler<MouseEvent> clickHandler = goBoardController::mouseClicked;
        goBoardView.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);

        //eventHandler for mouse hovering
        EventHandler<MouseEvent> moveHandler = goBoardView::moveHoverMouse;
        goBoardView.addEventHandler(MouseEvent.MOUSE_MOVED, moveHandler);

        //eventHandler for keyboard interaction
        goBoardView.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case SPACE, ENTER:
                    goBoardView.setStoneKeyboard();
                    break;
                case E, M:
                    goBoardView.setMarkingKeyboard();
                    // fallthrough
                default:
                    goBoardView.moveHoverKeyboard(keyEvent);
            }
        });

        drawScene();
    }

    /**
     * Constructs a GameScreenView that is used as the main interaction point of the game
     * @param model GoBoardModel to be interacted with
     */
    public GameScreenView(GoBoardModel model) {
        this(model,null);
    }

    @Override
    protected void drawScene() {
        pane = new BorderPane();
        pane.setTop(menuBar);
        pane.setCenter(goBoardView);

        VBox interactionField = new VBox();
        FlowPane gameplayButtons = new FlowPane(Orientation.VERTICAL);
        interactionField.getChildren().add(captureStatus);
        interactionField.getChildren().add(gameplayButtons);
        VBox.setVgrow(gameplayButtons, Priority.ALWAYS);
        pane.setLeft(interactionField);

        pane.setBottom(gameState);

        // Buttons for gameplay
        Button passButton = new Button("Pass");
        passButton.setFocusTraversable(false);
        passButton.setOnMouseClicked(e -> goBoardController.passPlayer());
        passButton.setMaxWidth(Double.MAX_VALUE);

        Button resignButton = new Button("Resign");
        resignButton.setFocusTraversable(false);
        resignButton.setOnMouseClicked(e -> goBoardController.resign());
        resignButton.setMaxWidth(Double.MAX_VALUE);

        gameplayButtons.setPadding(new Insets(30));
        gameplayButtons.setHgap(10);
        gameplayButtons.setVgap(10);
        gameplayButtons.setAlignment(Pos.CENTER);
        gameplayButtons.setColumnHalignment(HPos.CENTER);
        gameplayButtons.getChildren().add(passButton);
        gameplayButtons.getChildren().add(resignButton);
    }

    @Override
    public BorderPane getPane() {
        pane.setFocusTraversable(true);
        pane.getCenter().requestFocus();
        return this.pane;
    }
}
