package com.gogame.listener;

import com.gogame.model.GoBoardModel;
import javafx.scene.text.Text;

import java.util.EventObject;

/**
 * The GameEvent class represents an event that occurs during a game on the Go board.
 * It extends the EventObject class.
 */
public class GameEvent extends EventObject {

    private final GameState state;
    private int row;
    private int col;

    /**
     * Constructs a GameEvent object with the current GoBoardModel and GameState.
     * @param source The GoBoardModel object that is the source of the event.
     * @param state  The GameState associated with the event.
     */
    public GameEvent(GoBoardModel source, GameState state){
        super(source);
        this.state = state;
    }

    /**
     * Constructs a GameEvent object with the current GoBoardModel, GameState, row, and column.
     * @param source The GoBoardModel object that is the source of the event.
     * @param state  The GameState associated with the event.
     * @param row    The row index associated with the event.
     * @param col    The column index associated with the event.
     */
    public GameEvent(GoBoardModel source, GameState state, int row, int col){
        this(source,state);
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row index associated with the event.
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index associated with the event.
     * @return The column index.
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the letter representation of the row index associated with the event.
     * The row index is converted to its corresponding letter based on the board size.
     * @return The letter representation of the row index.
     */
    public String getRowLetter(){
        GoBoardModel model = (GoBoardModel) source;
        return String.valueOf(model.getSize()-row);
    }

    /**
     * Returns the letter representation of the column index associated with the event.
     * @return The letter representation of the column index.
     */
    public String getColLetter(){
        return String.valueOf((char) (col + 65));
    }

    /**
     * Returns the current GameState associated with the event.
     * @return The current GameState.
     */
    public GameState getState() {
        return state;
    }
}
