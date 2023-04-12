package com.gogame.controller;

import com.gogame.model.SaveGameModel;
import com.gogame.view.GameScreenView;

public class SaveGameController {

    private final GameScreenView gameScreenView;
    private final SaveGameModel saveGameModel;
    private final GoBoardController goBoardController;

    public SaveGameController(GameScreenView gameScreenView, GoBoardController goBoardController, GameScreenController gameScreenController){
        this.gameScreenView = gameScreenView;
        this.saveGameModel = new SaveGameModel(goBoardController, gameScreenController);
        this.goBoardController = goBoardController;
    }

    public void importGameFile() {
        saveGameModel.importGameFile();
    }

    public void exportGameFile() {
        saveGameModel.exportGameFile();
    }

    public void storeData(String s) {
        saveGameModel.storeData(s);
    }
}
