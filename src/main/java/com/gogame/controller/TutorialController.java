package com.gogame.controller;

import com.gogame.Main;
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

public class TutorialController {

    private final TutorialView view;
    private final SaveGameHandler saveGame;


    public TutorialController(TutorialView view, SaveGameHandler saveGame){
        this.view = view;
        this.saveGame = saveGame;
    }

    public void changeSceneToStartScreen() {
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        GameScreenView nextView = new GameScreenView(Main.getDefaultModel());
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Go Game");

            BorderPane root = (BorderPane) stage.getScene().getRoot();
            root.getCenter().requestFocus();
        }
    }

    public void changeSceneToTutorialScene(File selectedTutorial) {
        SaveGameHandler saveGame = new SaveGameHandler(selectedTutorial);
        TutorialView nextView = new TutorialView(saveGame);

        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if (w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(), s.getWidth(), s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Go Game Tutorial - " + selectedTutorial.getName());
        }
    }

    public void lastMove() {
        saveGame.simulateLastMove();
    }

    public void nextMove() {
        if(!saveGame.simulateNextMove()) showWinScreen();
    }

    public void resetTutorial() {
        saveGame.resetMoves();
    }

    private void showWinScreen() {
        GoBoardModel model = saveGame.getModel();
        model.gameEnds(false);
        WinScreenDialog winScreenDialog = new WinScreenDialog(model);
        winScreenDialog.showAndWait();
    }
}
