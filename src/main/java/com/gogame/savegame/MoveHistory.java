package com.gogame.savegame;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;

import java.util.ArrayList;
import java.util.List;

/**
 * The MoveHistory class keeps track of the game events and provides access to the list of events. Used for SaveGames.
 * It implements the GameListener interface to listen for game events and update the history accordingly.
 */
public class MoveHistory implements GameListener {

    private final List<GameEvent> events;

    /**
     * Constructs a MoveHistory object for the specified GoBoardModel and add itself to the models listeners.
     * @param model The GoBoardModel to be associated with the move history.
     * @throws IllegalArgumentException if the model is null.
     */
    public MoveHistory(GoBoardModel model) {
        if(model == null) throw new IllegalArgumentException("GoBoardModel must not be null.");

        model.addGameListener(this);
        events = new ArrayList<>();
    }

    /**
     * Returns the list of game events in the move history.
     * @return The list of game events.
     */
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
