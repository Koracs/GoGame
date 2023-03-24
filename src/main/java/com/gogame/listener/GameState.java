package com.gogame.listener;

public enum GameState {

    GAME_START("Game started."),
    PLACE_HANDICAP("Place handicap stones."),
    BLACK_TURN("Black player's turn."),
    BLACK_PASSED("Black player passed."),
    BLACK_WON("Black player won!"),
    WHITE_TURN("White player's turn."),
    WHITE_PASSED("White player passed."),
    WHITE_WON("White player won!"),
    DRAW("Draw!"),

    RESET("Game was reset.");

    private final String message;

    private GameState(String message){ this.message = message;}

    public String toString(){return message;}
}
