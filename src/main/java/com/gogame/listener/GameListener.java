package com.gogame.listener;

import java.util.EventListener;

/**
 * The GameListener class is part of the observer pattern and informs every
 * registered listener about changes in the game model via GameEvents
 */
public interface GameListener extends EventListener {

    /**
     * Event listener that triggers when a move is completed
     * @param event Event that contains information about the current game
     */
    void moveCompleted(GameEvent event);

    /**
     * Event listener that triggers when a game is reset
     * @param event Event that contains information about the current game
     */
    void resetGame(GameEvent event);

    /**
     * Event listener that triggers when a player passed
     * @param event Event that contains information about the current game
     */
    void playerPassed(GameEvent event);

    /**
     * Event listener that triggers when a game ends
     * @param event Event that contains information about the current game
     */
    void gameEnded(GameEvent event);
}
