package com.gogame.listener;

import java.util.EventListener;

public interface GameListener extends EventListener {

    void moveCompleted(GameEvent event); //todo use extension of EventObject like Soft2 ConnectFour?

    void resetGame(GameEvent event);
}
