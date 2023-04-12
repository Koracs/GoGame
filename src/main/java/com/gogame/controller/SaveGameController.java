package com.gogame.controller;

import com.gogame.model.SaveGameModel;
import com.gogame.view.GameScreenView;

public class SaveGameController {

    private final GameScreenView gameScreenView;
    private final SaveGameModel saveGameModel;

    public SaveGameController(GameScreenView view, SaveGameModel model){
        this.view = view;
        this.model = model;
    }

    public void importGameFile() {

    }
}
