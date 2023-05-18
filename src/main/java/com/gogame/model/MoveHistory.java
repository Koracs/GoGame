package com.gogame.model;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;

import java.util.ArrayList;
import java.util.List;

public class MoveHistory implements GameListener {

    private GoBoardModel model;

    private List<GameEvent> events;

    public MoveHistory(GoBoardModel model){
        this.model = model;
        model.addGameListener(this);
        events = new ArrayList<>();
    }

    public List<GameEvent> getEvents() {
        return events;
    }

    @Override
    public void moveCompleted(GameEvent event) {
        events.add(event);
    }

    @Override
    public void resetGame(GameEvent event) {
        events.clear();
    }

    @Override
    public void playerPassed(GameEvent event) {
        events.add(event);
    }

    @Override
    public void gameEnded(GameEvent event) {
        events.add(event);
    }
}
