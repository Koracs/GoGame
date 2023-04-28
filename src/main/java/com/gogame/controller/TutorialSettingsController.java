package com.gogame.controller;

import com.gogame.view.StartScreenView;
import com.gogame.view.TutorialSettingsView;
import com.gogame.view.TutorialView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TutorialSettingsController {
    //region Fields
    private final TutorialSettingsView view;

    private String selectedTutorial;

    // Constants
    //todo change this
    private final String TUTORIAL1 = "/tutorials/Tutorial1.txt";
    private final String TUTORIAL2 = "/tutorials/Tutorial2.txt";
    private final String TUTORIAL3 = "/tutorials/Tutorial3.txt";
    private final String TUTORIAL4 = "/tutorials/Tutorial4.txt";
    //endregion

    // Constructor
    public TutorialSettingsController(TutorialSettingsView view) {
        this.view = view;
        this.selectedTutorial = TUTORIAL1;
    }

    //region Methods
    public void selectTutorial(String tut) {
        //todo implement same as in tutorialSettingsView
        switch (tut) {
            case "tutorial 1":
                selectedTutorial = TUTORIAL1;
                break;
            case "tutorial 2":
                selectedTutorial = TUTORIAL2;
                break;
            case "tutorial 3":
                selectedTutorial = TUTORIAL3;
                break;
            case "tutorial 4":
                selectedTutorial = TUTORIAL4;
                break;
        }
    }

    public void changeSceneToTutorialScene(String selected) {
        selectTutorial(selected);
        String path = getClass().getResource(selectedTutorial).getPath().substring(1);
        TutorialView nextView = new TutorialView(path);
        Scene s = view.getPane().getScene();
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }

    public void changeSceneToStartScreen() {
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        StartScreenView nextView = new StartScreenView();
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }
    //endregion
}
