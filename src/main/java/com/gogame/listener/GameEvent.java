package com.gogame.listener;

import com.gogame.model.GoBoardModel;

import java.util.EventObject;

public class GameEvent extends EventObject {

    private GameState state;

    private int x;
    private int y;


    public GameEvent(GoBoardModel source, GameState state){ //todo add game interface?
        super(source);
        this.state = state;
    }

    public GameEvent(GoBoardModel source, GameState state, int x, int y){
        this(source,state);
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public GameState getState() {
        return state;
    }
}
