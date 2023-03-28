package com.gogame.listener;

import com.gogame.model.GoBoardModel;

import java.util.EventObject;

public class GameEvent extends EventObject {

    private GameState state;

    private int row;
    private int col;


    public GameEvent(GoBoardModel source, GameState state){ //todo add game interface?
        super(source);
        this.state = state;
    }

    public GameEvent(GoBoardModel source, GameState state, int row, int col){
        this(source,state);
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public GameState getState() {
        return state;
    }
}
