package com.gogame.view;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.listener.GameState;
import com.gogame.model.GoBoardModel;
import com.gogame.model.Stone;
import javafx.scene.control.TextField;

public class GameStateField extends TextField implements GameListener {

    public GameStateField(GoBoardModel model){
        super(model.getGameState().toString());
        model.addGameListener(this);
        setFocusTraversable(false);
        setEditable(false);
        setMouseTransparent(true);
    }
    @Override
    public void moveCompleted(GameEvent event) {
        GoBoardModel model = (GoBoardModel) event.getSource();
        String lastPlayer;
        if(model.getGameState() == GameState.PLACE_HANDICAP) lastPlayer = "Black";
        else lastPlayer = model.getCurrentPlayer() == Stone.BLACK? "White" : "Black";

        this.setText(lastPlayer + " placed at: " + event.getColLetter()
                          + " " + event.getRowLetter() + ". " + event.getState().toString());
    }

    @Override
    public void resetGame(GameEvent event) {
        this.setText(event.getState().toString());
    }

    @Override
    public void playerPassed(GameEvent event) {
        GoBoardModel model = (GoBoardModel) event.getSource();
        String player = model.getCurrentPlayer() == Stone.BLACK? "White" : "Black";

        this.setText(event.getState().toString() + " " + player + "'s turn.");
    }

    @Override
    public void gameEnded(GameEvent event) {
        this.setText(event.getState().toString());
    }

}
