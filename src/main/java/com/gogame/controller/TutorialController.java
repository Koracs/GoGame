package com.gogame.controller;

import com.gogame.model.GoBoardModel;
import com.gogame.savegame.SaveGameHandler;
import com.gogame.view.GameScreenView;
import com.gogame.view.TutorialView;
import com.gogame.view.WinScreenDialog;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

/**
 * The TutorialController handles interactions between the user / view and the go board model
 */
public class TutorialController {

    private final TutorialView view;
    private final SaveGameHandler saveGame;

    /**
     * Constructs a TutorialController with the given TutorialView and SaveGameHandler instances
     * @param view View to be controlled
     * @param saveGame The SaveGameHandler associated with the tutorial
     */
    public TutorialController(TutorialView view, SaveGameHandler saveGame){
        this.view = view;
        this.saveGame = saveGame;
    }

    /**
     * Switches the view back to the normal game view
     */
    public void changeSceneToStartScreen() {
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        GameScreenView nextView = new GameScreenView(GoBoardModel.getDefaultModel());
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Go Game");

            BorderPane root = (BorderPane) stage.getScene().getRoot();
            root.getCenter().requestFocus();
        }
    }

    /**
     * Changes the tutorial of this view
     * @param selectedTutorial File that contains information about the tutorial
     */
    public void changeTutorial(File selectedTutorial) {
        SaveGameHandler saveGame = new SaveGameHandler(selectedTutorial);
        TutorialView nextView = new TutorialView(saveGame);

        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if (w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(), s.getWidth(), s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Go Game Tutorial - " + selectedTutorial.getName());

            BorderPane root = (BorderPane) stage.getScene().getRoot();
            root.getCenter().requestFocus();
        }
    }

    /**
     * Simulates the last move in the tutorial by invoking the corresponding method in the SaveGameHandler
     */
    public void lastMove() {
        saveGame.simulateLastMove();
    }

    /**
     * Simulates the next move in the tutorial by invoking the corresponding method in the SaveGameHandler.
     * If there are no more moves available, it shows the win screen
     */
    public void nextMove() {
        if(!saveGame.simulateNextMove()) showWinScreen();
    }

    /**
     * Resets the tutorial by invoking the corresponding method in the SaveGameHandler.
     */
    public void resetTutorial() {
        saveGame.resetMoves();
    }

    /**
     * Shows the win screen by updating the game state, creating a WinScreenDialog, and displaying it.
     * This method is called when there are no more moves available in the tutorial
     */
    private void showWinScreen() {
        GoBoardModel model = saveGame.getModel();
        model.gameEnds(false);
        WinScreenDialog winScreenDialog = new WinScreenDialog(model);
        winScreenDialog.showAndWait();
    }
}
