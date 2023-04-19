package com.gogame.controller;

import com.gogame.model.GoBoardModel;
import com.gogame.model.SaveGame;
import com.gogame.view.TutorialView;

public class TutorialController {

    private final TutorialView view;
    private final GoBoardModel model;

    private final SaveGame saveGame;

    public TutorialController(TutorialView view, GoBoardModel model, GoBoardController controller){
        this.view = view;
        this.model = model;
        this.saveGame = new SaveGame(controller, null); //todo im View anlegen?
    }

    public void loadMove() {
        saveGame.loadGradually(true);
    }

    public void deleteMove() {
        saveGame.loadGradually(false);
    }
}
