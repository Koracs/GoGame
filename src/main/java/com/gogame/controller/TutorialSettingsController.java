package com.gogame.controller;

import com.gogame.model.GoBoardModel;
import com.gogame.view.GameScreenView;
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
    private final String TUTORIAL1 = " /Tutorial1";
    private final String TUTORIAL2 = " /Tutorial2";
    private final String TUTORIAL3 = " /Tutorial3";
    private final String TUTORIAL4 = " /Tutorial4";
    //endregion

    // Constructor
    public TutorialSettingsController(TutorialSettingsView view) {
        this.view = view;
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

    private GoBoardModel initGoBoardModel(){
        return new GoBoardModel(19,0,0); //todo
    }
    public void changeSceneToTutorialScene() {
        TutorialView nextView = new TutorialView(initGoBoardModel());
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
        Stage stageStart = (Stage) s.getWindow();
        Window w = s.getWindow();
        StartScreenView nextView = new StartScreenView(stageStart);
        if(w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(),s.getWidth(),s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }
    //endregion
}
