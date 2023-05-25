package com.gogame.savegame;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;

import java.util.ArrayList;
import java.util.List;

public class MoveHistory implements GameListener {

    private List<GameEvent> events;
    private boolean gameEnded;

    public MoveHistory(GoBoardModel model) {
        model.addGameListener(this);
        events = new ArrayList<>();
        gameEnded = false;
    }

    public MoveHistory() {
        events = new ArrayList<>();
    }

    public void addListener(GoBoardModel model) {
        model.addGameListener(this);
    }

    public List<GameEvent> getEvents() {
        return events;
    }

    @Override
    public void moveCompleted(GameEvent event) {
        if(!gameEnded)
            events.add(event);
    }

    @Override
    public void resetGame(GameEvent event) {
        events.clear();
        gameEnded = false;
    }

    @Override
    public void playerPassed(GameEvent event) {
        if(!gameEnded)
            events.add(event);
    }

    @Override
    public void gameEnded(GameEvent event) {
        gameEnded = true;
    }
}
