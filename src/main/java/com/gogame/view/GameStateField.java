package com.gogame.view;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import com.gogame.model.Stone;
import javafx.scene.control.TextField;

public class GameStateField extends TextField implements GameListener {

    public GameStateField(GoBoardModel model){
        super();
        model.addGameListener(this);
    }
    @Override
    public void moveCompleted(GameEvent event) {
        GoBoardModel model = (GoBoardModel) event.getSource();
        String lastPlayer = model.getCurrentPlayer() == Stone.BLACK? "White" : "Black";
        this.setText("Player " + lastPlayer + " placed at: " + event.getColLetter()
                          + " " + event.getRowLetter() + ". " + event.getState().toString());
    }

    @Override
    public void resetGame(GameEvent event) {
        this.setText(event.getState().toString());
    }

    @Override
    public void playerPassed(GameEvent event) {
        this.setText(event.getState().toString());
    }

    @Override
    public void gameEnded(GameEvent event) {
        this.setText(event.getState().toString());
    }

}
