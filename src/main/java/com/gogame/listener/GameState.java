package com.gogame.listener;

/**
 * Enum that represents the current state of the Go game
 */
public enum GameState {

    GAME_START("Game started."),
    PLACE_HANDICAP("Place handicap stones."),
    BLACK_TURN("Black's turn."),
    BLACK_PASSED("Black passed."),
    BLACK_WON("Black won!"),
    WHITE_TURN("White's turn."),
    WHITE_PASSED("White passed."),
    WHITE_WON("White won!"),
    DRAW("Draw!"),
    RESET("Game was reset.");

    private final String message;

    GameState(String message){ this.message = message;}

    @Override
    public String toString(){return message;}
}
