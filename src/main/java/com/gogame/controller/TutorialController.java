package com.gogame.controller;

import com.gogame.model.GoBoardModel;
import com.gogame.view.GameScreenView;
import com.gogame.view.TutorialView;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TutorialController {

    private final TutorialView view;
    private final GoBoardModel model;

    //private final SaveGame saveGame;

    public TutorialController(TutorialView view, GoBoardModel model, GoBoardController controller){
        this.view = view;
        this.model = model;
        //this.saveGame = new SaveGame(controller, null); //todo im View anlegen?
    }

    public void changeSceneToStartScreen() {
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        GameScreenView nextView = new GameScreenView(new GoBoardModel(19,0,0));
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);

            BorderPane root = (BorderPane) stage.getScene().getRoot();
            root.getCenter().requestFocus();
        }
    }

    public void changeSceneToTutorialScene(String selectedTutorial) {
        TutorialView nextView = new TutorialView(selectedTutorial);

        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        if (w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }
}
