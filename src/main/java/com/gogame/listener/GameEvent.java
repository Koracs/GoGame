package com.gogame.listener;

import com.gogame.model.GoBoardModel;
import javafx.scene.text.Text;

import java.util.EventObject;

public class GameEvent extends EventObject {

    private final GameState state;

    private int row;
    private int col;


    public GameEvent(GoBoardModel source, GameState state){
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

    public String getRowLetter(){
        GoBoardModel model = (GoBoardModel) source;
        return String.valueOf(model.getSize()-row);
    }

    public String getColLetter(){
        return String.valueOf((char) (col + 65));
    }

    public GameState getState() {
        return state;
    }
}
