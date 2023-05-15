package com.gogame.model;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.listener.GameState;
import com.gogame.view.GameScreenView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GoBoardModelTest {
    private GoBoardModel goBoardModel19x0x0;
    private GoBoardModel goBoardModel19x05x3;
    private GoBoardModel goBoardModel13x0x0;
    private GoBoardModel goBoardModel9x0x0;


    @BeforeEach
    void setUp() {
        goBoardModel19x0x0 = new GoBoardModel(19, 0, 0);
        goBoardModel19x05x3 = new GoBoardModel(19, 0.5, 3);
        goBoardModel13x0x0 = new GoBoardModel(13, 0, 0);
        goBoardModel9x0x0 = new GoBoardModel(9, 0, 0);
    }

    @Test
    void testCorrectBoard() {
        //TestAssaySizes
        assertArrayEquals(new int[]{9, 13, 19}, goBoardModel19x0x0.getSizes());

        //TestSize
        assertEquals(19, goBoardModel19x0x0.getSize());
        assertEquals(13, goBoardModel13x0x0.getSize());
        assertEquals(9, goBoardModel9x0x0.getSize());

        //TestHandicap
        assertEquals(0, goBoardModel19x0x0.getHandicap());
        assertEquals(3, goBoardModel19x05x3.getHandicap());

        //TestKomi
        assertEquals(0, goBoardModel19x0x0.getKomi());
        assertEquals(0.5, goBoardModel19x05x3.getKomi());

        //TestFirstPlayerBlack
        assertEquals(Stone.BLACK, goBoardModel19x0x0.getCurrentPlayer());

        //TestFirstPoints0
        assertEquals(0, goBoardModel9x0x0.getPointsWhite());
        assertEquals(0, goBoardModel9x0x0.getPointsBlack());
        assertEquals(0, goBoardModel19x0x0.getCapturedByBlack());
        assertEquals(0, goBoardModel19x0x0.getCapturedByWhite());

        //TestFields
        GoField[][] goField = new GoField[19][19];
        GoField field = new GoField(1, 1);
        assertEquals(goField.length, goBoardModel19x0x0.getFields().length);
        assertEquals(field.isEmpty(), goBoardModel19x0x0.getField(1, 1).isEmpty());

        //TestGameState
        assertEquals(GameState.GAME_START, goBoardModel19x0x0.getGameState());

        //TestHandicapField
        assertEquals(Stone.PRESET, goBoardModel19x0x0.getField(3, 3).getStone());
        assertEquals(GameState.PLACE_HANDICAP, goBoardModel19x05x3.getGameState());
    }

    @Test
    void TestMove() {
        //TestNormalMove
        goBoardModel19x0x0.makeMove(0, 0);
        goBoardModel19x0x0.makeMove(0, 1);
        assertEquals(Stone.BLACK, goBoardModel19x0x0.getField(0, 0).getStone());
        assertEquals(Stone.WHITE, goBoardModel19x0x0.getField(0, 1).getStone());

        //TestHandicapMove
        for (int i = 3; i <= 15; i += 6) {
            goBoardModel19x05x3.makeMove(3, i);
        }
        assertEquals(Stone.BLACK, goBoardModel19x05x3.getField(3, 9).getStone());
        assertEquals(GameState.WHITE_TURN, goBoardModel19x05x3.getGameState());

        //TestIfFieldIsUsed
        goBoardModel19x0x0.makeMove(0, 1);
        assertEquals(Stone.WHITE, goBoardModel19x0x0.getField(0, 1).getStone());

        //TestCheckSuicide
        goBoardModel13x0x0.makeMove(1, 1);
        goBoardModel13x0x0.makeMove(5, 1);
        goBoardModel13x0x0.makeMove(1, 2);
        goBoardModel13x0x0.makeMove(5, 2);
        goBoardModel13x0x0.makeMove(1, 3);
        goBoardModel13x0x0.makeMove(5, 3);
        goBoardModel13x0x0.makeMove(2, 3);
        goBoardModel13x0x0.makeMove(5, 4);
        goBoardModel13x0x0.makeMove(3, 3);
        goBoardModel13x0x0.makeMove(5, 5);
        goBoardModel13x0x0.makeMove(3, 2);
        goBoardModel13x0x0.makeMove(5, 6);
        goBoardModel13x0x0.makeMove(3, 1);
        goBoardModel13x0x0.makeMove(5, 7);
        goBoardModel13x0x0.makeMove(2, 1);
        goBoardModel13x0x0.makeMove(2, 2);
        assertEquals(Stone.NONE, goBoardModel13x0x0.getField(2, 2).getStone());
    }

    @Test
    void testGameListeners() {
        goBoardModel9x0x0.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {
                assertEquals(1, event.getRow());
            }

            @Override
            public void resetGame(GameEvent event) {

            }

            @Override
            public void playerPassed(GameEvent event) {

            }

            @Override
            public void gameEnded(GameEvent event) {

            }
        });
        goBoardModel9x0x0.makeMove(1, 1);
        assertEquals(1, goBoardModel9x0x0.getGameListeners().size());

        goBoardModel19x05x3.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {
                assertEquals(GameState.PLACE_HANDICAP, event.getState());
            }

            @Override
            public void resetGame(GameEvent event) {

            }

            @Override
            public void playerPassed(GameEvent event) {

            }

            @Override
            public void gameEnded(GameEvent event) {

            }
        });
        goBoardModel19x05x3.makeMove(3, 3);

    }


    @Test
    void checkCapture() {
        goBoardModel19x0x0.makeMove(1, 1);
        goBoardModel19x0x0.makeMove(1, 2);
        goBoardModel19x0x0.makeMove(2, 1);
        goBoardModel19x0x0.makeMove(2, 2);
        goBoardModel19x0x0.makeMove(3, 2);
        goBoardModel19x0x0.makeMove(3, 1);
        goBoardModel19x0x0.makeMove(3, 5);
        goBoardModel19x0x0.makeMove(2, 0);
        goBoardModel19x0x0.makeMove(3, 6);
        goBoardModel19x0x0.makeMove(1, 0);
        goBoardModel19x0x0.makeMove(3, 8);
        goBoardModel19x0x0.makeMove(0, 1);
        assertEquals(2, goBoardModel19x0x0.getCapturedByWhite());

        goBoardModel13x0x0.makeMove(3, 3);
        goBoardModel13x0x0.makeMove(3, 4);
        goBoardModel13x0x0.makeMove(12, 0);
        goBoardModel13x0x0.makeMove(4, 3);
        goBoardModel13x0x0.makeMove(12, 1);
        goBoardModel13x0x0.makeMove(3, 2);
        goBoardModel13x0x0.makeMove(12, 2);
        goBoardModel13x0x0.makeMove(2, 3);
        assertEquals(1, goBoardModel13x0x0.getCapturedByWhite());

        goBoardModel13x0x0.makeMove(3, 9);
        goBoardModel13x0x0.makeMove(2, 9);
        goBoardModel13x0x0.makeMove(12, 3);
        goBoardModel13x0x0.makeMove(3, 10);
        goBoardModel13x0x0.makeMove(12, 4);
        goBoardModel13x0x0.makeMove(3, 8);
        goBoardModel13x0x0.makeMove(12, 5);
        goBoardModel13x0x0.makeMove(4, 9);
        assertEquals(2, goBoardModel13x0x0.getCapturedByWhite());

        goBoardModel13x0x0.makeMove(9, 3);
        goBoardModel13x0x0.makeMove(8, 3);
        goBoardModel13x0x0.makeMove(12, 6);
        goBoardModel13x0x0.makeMove(9, 2);
        goBoardModel13x0x0.makeMove(12, 7);
        goBoardModel13x0x0.makeMove(10, 3);
        goBoardModel13x0x0.makeMove(12, 8);
        goBoardModel13x0x0.makeMove(9, 4);
        assertEquals(3, goBoardModel13x0x0.getCapturedByWhite());
        goBoardModel13x0x0.makeMove(12, 9);
        goBoardModel13x0x0.makeMove(9, 9);
        goBoardModel13x0x0.makeMove(8, 9);
        goBoardModel13x0x0.makeMove(12, 10);
        goBoardModel13x0x0.makeMove(9, 10);
        goBoardModel13x0x0.makeMove(12, 11);
        goBoardModel13x0x0.makeMove(10, 9);
        goBoardModel13x0x0.makeMove(11, 12);
        goBoardModel13x0x0.makeMove(9, 8);
        assertEquals(1, goBoardModel13x0x0.getCapturedByBlack());


    }

    /*
    @Test
    void findNeighboursOfSameColor() {

    }

    @Test
    void chainHasLiberties() {

    }

    @Test
    void moveIsSuicide() {

    }
    @Test
    void moveIsKo() {

    }

    @Test
    void switchPlayer() {
    */


    @Test
    void reset() {
        goBoardModel13x0x0.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {

            }

            @Override
            public void resetGame(GameEvent event) {
                assertEquals(GameState.RESET, event.getState());
            }

            @Override
            public void playerPassed(GameEvent event) {

            }

            @Override
            public void gameEnded(GameEvent event) {

            }
        });
        goBoardModel13x0x0.makeMove(1, 1);
        goBoardModel13x0x0.makeMove(1, 2);
        goBoardModel13x0x0.reset();
        assertEquals(Stone.NONE, goBoardModel13x0x0.getField(1, 1).getStone());
    }


    @Test
    void changeSettings() {
        goBoardModel19x0x0.makeMove(1, 1);
        goBoardModel19x0x0.makeMove(1, 2);
        goBoardModel19x0x0.changeSettings(19, 0.5, 2);
        assertEquals(Stone.NONE, goBoardModel19x0x0.getField(1, 1).getStone());
        assertEquals(2, goBoardModel19x0x0.getHandicap());
        //Punkte werden im View nicht zurück gesetzt
    }


    @Test
    void pass() {
        goBoardModel19x0x0.makeMove(1, 1); //Black
        assertEquals(GameState.WHITE_TURN, goBoardModel19x0x0.getGameState());
        goBoardModel19x0x0.makeMove(1, 2); //White
        goBoardModel19x0x0.makeMove(1, 3); //Black
        goBoardModel19x0x0.pass(); //White
        assertEquals(GameState.BLACK_TURN, goBoardModel19x0x0.getGameState());
        goBoardModel19x0x0.pass(); //Black
        assertEquals(GameState.BLACK_WON, goBoardModel19x0x0.getGameState());
        goBoardModel9x0x0.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {

            }

            @Override
            public void resetGame(GameEvent event) {

            }

            @Override
            public void playerPassed(GameEvent event) {
                assertEquals(GameState.BLACK_PASSED, event.getState());
            }

            @Override
            public void gameEnded(GameEvent event) {

            }
        });
        goBoardModel9x0x0.pass();
    }

    @Test
    void gameEnds() {
        goBoardModel9x0x0.makeMove(1, 1);
        goBoardModel9x0x0.makeMove(1, 2);
        goBoardModel9x0x0.makeMove(1, 3);
        goBoardModel9x0x0.makeMove(1, 4);
        goBoardModel9x0x0.playerResigned();
        assertEquals(GameState.DRAW, goBoardModel9x0x0.getGameState());
        goBoardModel19x0x0.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {

            }

            @Override
            public void resetGame(GameEvent event) {

            }

            @Override
            public void playerPassed(GameEvent event) {

            }

            @Override
            public void gameEnded(GameEvent event) {
                assertEquals(GameState.WHITE_WON, event.getState());
            }
        });
        goBoardModel19x0x0.playerResigned();
    }

    /*
    @Test
    void calculateScores() {

    }

    @Test
    void removeDeadStones() {

    }

    @Test
    void findEmptyArea() {

    }

    @Test
    void isSurrounded() {

    }

    @Test
    void checkNeighbours() {

    }

    @Test
    void playerResigned() {

    }

    @Test
    void printModel() {

    }

    @Test
    void addGameListener() {
    }

    @Test
    void removeGameListener() {
    }

    @Test
    void getGameListeners() {
    }

    @Test
    void setGameListeners() {
    }
    */
}