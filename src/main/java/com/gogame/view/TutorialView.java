package com.gogame.view;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.controller.TutorialController;
import com.gogame.model.GoBoardModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class TutorialView extends View{

    private final GoBoardModel model;
    private final TutorialController sceneController;
    private final GoBoardController goBoardController;

    private final GoBoardView goBoard;
    private BorderPane pane;


    public TutorialView(GoBoardModel model) {
        sceneController = new TutorialController(this);
        goBoard = new GoBoardView(model);
        goBoardController = goBoard.getController();

        this.model = model;

    }
    @Override
    public BorderPane getPane() {
        return this.pane;
    }

    @Override
    protected void drawScene() {

    }
}
