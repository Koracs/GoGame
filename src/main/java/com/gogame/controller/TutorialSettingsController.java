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
    private final String TUTORIAL1 = "/gameData/Tutorial1.txt";
    private final String TUTORIAL2 = "/gameData/Tutorial2.txt";
    private final String TUTORIAL3 = "/gameData/Tutorial3.txt";
    private final String TUTORIAL4 = "/gameData/Tutorial4.txt";
    //endregion

    // Constructor
    public TutorialSettingsController(TutorialSettingsView view) {
        this.view = view;
        //todo weg mit dem
        this.selectedTutorial = TUTORIAL1;
    }

    //region Getter/Setter

    public String getSelectedTutorial() {
        return selectedTutorial;
    }

    //endregion

    //region Methods
    public void selectTutorial(int tut) {
        switch (tut) {
            case 1:
                selectedTutorial = TUTORIAL1;
                break;
            case 2:
                selectedTutorial = TUTORIAL2;
                break;
            case 3:
                selectedTutorial = TUTORIAL3;
                break;
            case 4:
                selectedTutorial = TUTORIAL4;
                break;
        }
    }

    public void importGameData() {

    }

    public void selectFile() {

    }

    public void changeSceneToTutorialScene() {
        String path = getClass().getResource(selectedTutorial).getPath().substring(1);
        TutorialView nextView = new TutorialView(path);
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage) {
            Stage s = (Stage) w;
            Scene scene = new Scene(nextView.getPane());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            s.setScene(scene);
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
