package com.gogame.listener;

import java.util.EventListener;

public interface GameListener extends EventListener {

    void moveCompleted(GameEvent event);

    void resetGame(GameEvent event);

    void playerPassed(GameEvent event);

    void gameEnded(GameEvent event);
}
