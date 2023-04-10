package com.gogame.controller;

import com.gogame.model.GoBoardModel;
import com.gogame.view.TutorialView;

public class TutorialController {

    private final TutorialView view;
    private final GoBoardModel model;

    public TutorialController(TutorialView view, GoBoardModel model){
        this.view = view;
        this.model = model;
        loadGame();
    }

    private void loadGame() {
        /*
        //todo Check if the input file is in right format
        while (line != null) {
            String[] temp = line.split(";");
            if(temp.length == 1) {
                passPlayer();
            } else {
                model.makeMove(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
            }

            line = reader.readLine();
        }*/
    }
}
