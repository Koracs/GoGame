package com.gogame.controller;

import com.gogame.view.TutorialSettingsView;

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
    //endregion
}
