package com.gogame.model;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.listener.GameState;
import com.gogame.savegame.MoveHistory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    @DisplayName("Verifying expected behavior with different values for the GoBoardModel constructor")
    void testConstructor() {
        assertDoesNotThrow(() -> new GoBoardModel(19, 0, 0));

        assertThrowsExactly(IllegalArgumentException.class, () -> new GoBoardModel(0, 0, 0));
        assertThrowsExactly(IllegalArgumentException.class, () -> new GoBoardModel(1, -0.5, 0));
        assertThrowsExactly(IllegalArgumentException.class, () -> new GoBoardModel(1, 0, -1));
    }

    @Test
    @DisplayName("Testing static method for default model")
    void testGetDefaultModel() {
        GoBoardModel defaultModel = GoBoardModel.getDefaultModel();
        GoBoardModel manualModel = new GoBoardModel(19,0,0);
        assertEquals(defaultModel.getSize(),manualModel.getSize());
        assertEquals(defaultModel.getKomi(),manualModel.getKomi());
        assertEquals(defaultModel.getHandicap(),manualModel.getHandicap());
    }

    @Test
    @DisplayName("Testing that correct preferred sizes are given by the Class")
    void testGetSizes() {
        int[] sizes = new int[]{9, 13, 19};
        assertArrayEquals(sizes, GoBoardModel.getSizes());
    }

    @Test
    @DisplayName("Testing getter getSize()")
    void testGetSize() {
        assertEquals(19, goBoardModel19x0x0.getSize());
        assertEquals(13, goBoardModel13x0x0.getSize());
        assertEquals(9, goBoardModel9x0x0.getSize());
    }

    @Test
    @DisplayName("Testing getter getKomi()")
    void testGetKomi() {
        assertEquals(0, goBoardModel19x0x0.getKomi());
        assertEquals(0.5, goBoardModel19x05x3.getKomi());
    }

    @Test
    @DisplayName("Testing getter getHandicap()")
    void testGetHandicap() {
        assertEquals(0, goBoardModel19x0x0.getHandicap());
        assertEquals(3, goBoardModel19x05x3.getHandicap());
    }

    @Test
    @DisplayName("Testing getter getFields()")
    void testGetFields() {
        GoField[][] fields = goBoardModel19x0x0.getFields();

        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                assertNotNull(fields[i][j]);
            }
        }
    }

    @Test
    @DisplayName("Testing getter getHistory()")
    void testGetHistory() {
        MoveHistory history = goBoardModel9x0x0.getHistory();
        assertEquals(0,history.getEvents().size());

        goBoardModel9x0x0.makeMove(0,0);
        history = goBoardModel9x0x0.getHistory();
        assertEquals(1,history.getEvents().size());

        goBoardModel9x0x0.reset();
        history = goBoardModel9x0x0.getHistory();
        assertEquals(0,history.getEvents().size());
    }

    @Test
    @DisplayName("Testing getter isPlayerResigned()")
    void testIsPlayerResigned() {
        assertFalse(goBoardModel9x0x0.isPlayerResigned());
        goBoardModel9x0x0.pass();
        goBoardModel9x0x0.pass();
        assertFalse(goBoardModel9x0x0.isPlayerResigned());

        goBoardModel13x0x0.playerResigned();
        assertTrue(goBoardModel13x0x0.isPlayerResigned());
    }


    @Test
    @DisplayName("Verifying if the board initialized correctly")
    void testCorrectBoardInit() {
        //TestFirstPlayerBlack
        assertEquals(Stone.BLACK, goBoardModel19x0x0.getCurrentPlayer());

        //TestFirstPoints0
        assertEquals(0, goBoardModel9x0x0.getPointsWhite());
        assertEquals(0, goBoardModel9x0x0.getPointsBlack());
        assertEquals(0, goBoardModel19x0x0.getCapturedByBlack());
        assertEquals(0, goBoardModel19x0x0.getCapturedByWhite());

        //TestGameState
        assertEquals(GameState.GAME_START, goBoardModel19x0x0.getGameState());

        //TestHandicapField
        assertEquals(Stone.PRESET, goBoardModel19x0x0.getField(3, 3).getStone());
        assertEquals(GameState.PLACE_HANDICAP, goBoardModel19x05x3.getGameState());

        //TestFieldInitialization
        GoBoardModel noHandicapFields = new GoBoardModel(5, 0, 0);
        for (int row = 0; row < noHandicapFields.getSize(); row++) {
            for (int col = 0; col < noHandicapFields.getSize(); col++) {
                assertEquals(Stone.NONE, noHandicapFields.getField(row, col).getStone());
            }
        }
    }

    @Test
    @DisplayName("Testing standard moves on the board")
    void TestMove() {
        goBoardModel19x0x0.makeMove(0, 0);
        assertEquals(Stone.BLACK, goBoardModel19x0x0.getField(0, 0).getStone());
        goBoardModel19x0x0.makeMove(0, 1);
        assertEquals(Stone.WHITE, goBoardModel19x0x0.getField(0, 1).getStone());
    }

    @Test
    @DisplayName("Testing Handicap moves on the board")
    void testHandicapMoves() {
        goBoardModel19x05x3.makeMove(1, 1); //cannot be placed
        assertEquals(Stone.NONE, goBoardModel19x05x3.getField(1, 1).getStone());

        assertEquals(GameState.PLACE_HANDICAP, goBoardModel19x05x3.getGameState());
        goBoardModel19x05x3.makeMove(3, 3);
        assertEquals(GameState.PLACE_HANDICAP, goBoardModel19x05x3.getGameState());
        goBoardModel19x05x3.makeMove(3, 9);
        assertEquals(GameState.PLACE_HANDICAP, goBoardModel19x05x3.getGameState());
        goBoardModel19x05x3.makeMove(3, 15);
        assertEquals(GameState.WHITE_TURN, goBoardModel19x05x3.getGameState());

        goBoardModel19x05x3.makeMove(1, 1); //can be placed
        assertEquals(Stone.WHITE, goBoardModel19x05x3.getField(1, 1).getStone());
    }

    @Test
    @DisplayName("Verifying that stones cannot be placed on non-empty fields")
    void testUsedFields() {
        goBoardModel19x0x0.makeMove(0, 1);
        assertEquals(Stone.BLACK, goBoardModel19x0x0.getField(0, 1).getStone());

        goBoardModel19x0x0.makeMove(0, 1);
        assertEquals(Stone.BLACK, goBoardModel19x0x0.getField(0, 1).getStone());
    }

    @Test
    @DisplayName("Testing suicide check by trying to place a white stone inside a black chain")
    void testSuicide() {
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
        goBoardModel13x0x0.printModel();

        System.out.println("Trying to place white stone at 2 2");
        goBoardModel13x0x0.makeMove(2, 2);
        goBoardModel13x0x0.printModel();
        assertEquals(Stone.NONE, goBoardModel13x0x0.getField(2, 2).getStone());
        assertEquals(Stone.WHITE, goBoardModel13x0x0.getCurrentPlayer());
    }

    @Test
    @DisplayName("Testing capture moves on all four possible sides")
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
        goBoardModel19x0x0.printModel();
        System.out.println("Placing white stone at 0 1 to capture black stone");
        goBoardModel19x0x0.makeMove(0, 1);
        goBoardModel19x0x0.printModel();
        assertEquals(2, goBoardModel19x0x0.getCapturedByWhite());

        System.out.println("---------------------------------");
        goBoardModel13x0x0.makeMove(3, 3);
        goBoardModel13x0x0.makeMove(3, 4);
        goBoardModel13x0x0.makeMove(12, 0);
        goBoardModel13x0x0.makeMove(4, 3);
        goBoardModel13x0x0.makeMove(12, 1);
        goBoardModel13x0x0.makeMove(3, 2);
        goBoardModel13x0x0.makeMove(12, 2);
        goBoardModel13x0x0.printModel();
        System.out.println("Placing white stone at 2 3 to capture black stone");
        goBoardModel13x0x0.makeMove(2, 3);
        goBoardModel13x0x0.printModel();
        assertEquals(1, goBoardModel13x0x0.getCapturedByWhite());

        System.out.println("---------------------------------");
        goBoardModel13x0x0.makeMove(3, 9);
        goBoardModel13x0x0.makeMove(2, 9);
        goBoardModel13x0x0.makeMove(12, 3);
        goBoardModel13x0x0.makeMove(3, 10);
        goBoardModel13x0x0.makeMove(12, 4);
        goBoardModel13x0x0.makeMove(3, 8);
        goBoardModel13x0x0.makeMove(12, 5);
        goBoardModel13x0x0.printModel();
        System.out.println("Placing white stone at 4 9 to capture black stone");
        goBoardModel13x0x0.makeMove(4, 9);
        goBoardModel13x0x0.printModel();
        assertEquals(2, goBoardModel13x0x0.getCapturedByWhite());

        System.out.println("---------------------------------");
        goBoardModel13x0x0.makeMove(9, 3);
        goBoardModel13x0x0.makeMove(8, 3);
        goBoardModel13x0x0.makeMove(12, 6);
        goBoardModel13x0x0.makeMove(9, 2);
        goBoardModel13x0x0.makeMove(12, 7);
        goBoardModel13x0x0.makeMove(10, 3);
        goBoardModel13x0x0.makeMove(12, 8);
        goBoardModel13x0x0.printModel();
        System.out.println("Placing white stone at 9 4 to capture black stone");
        goBoardModel13x0x0.makeMove(9, 4);
        goBoardModel13x0x0.printModel();
        assertEquals(3, goBoardModel13x0x0.getCapturedByWhite());

        System.out.println("---------------------------------");
        goBoardModel13x0x0.makeMove(12, 9);
        goBoardModel13x0x0.makeMove(9, 9);
        goBoardModel13x0x0.makeMove(8, 9);
        goBoardModel13x0x0.makeMove(12, 10);
        goBoardModel13x0x0.makeMove(9, 10);
        goBoardModel13x0x0.makeMove(12, 11);
        goBoardModel13x0x0.makeMove(10, 9);
        goBoardModel13x0x0.makeMove(11, 12);
        goBoardModel13x0x0.printModel();
        System.out.println("Placing black stone at 9 8 to capture white stone");
        goBoardModel13x0x0.makeMove(9, 8);
        goBoardModel13x0x0.printModel();
        assertEquals(1, goBoardModel13x0x0.getCapturedByBlack());
    }

    @Test
    @DisplayName("Verifying that a move that captures other players stones cannot be suicide")
    void testComplexCapture() {
        goBoardModel13x0x0.makeMove(1, 2);
        goBoardModel13x0x0.makeMove(0, 2);
        goBoardModel13x0x0.makeMove(2, 1);
        goBoardModel13x0x0.makeMove(1, 1);
        goBoardModel13x0x0.makeMove(3, 2);
        goBoardModel13x0x0.makeMove(3, 1);
        goBoardModel13x0x0.makeMove(2, 3);
        goBoardModel13x0x0.makeMove(2, 4);
        goBoardModel13x0x0.makeMove(6, 6);
        goBoardModel13x0x0.makeMove(2, 0);
        goBoardModel13x0x0.makeMove(6, 7);
        goBoardModel13x0x0.makeMove(4, 2);
        goBoardModel13x0x0.makeMove(6, 8);
        goBoardModel13x0x0.makeMove(3, 3);
        goBoardModel13x0x0.makeMove(6, 9);
        goBoardModel13x0x0.makeMove(1, 3);
        goBoardModel13x0x0.makeMove(6, 10);
        goBoardModel13x0x0.printModel();

        System.out.println("Placing white stone at 2 2 to capture 4 black stones");
        goBoardModel13x0x0.makeMove(2, 2);
        goBoardModel13x0x0.printModel();

        assertEquals(Stone.WHITE, goBoardModel13x0x0.getField(2, 2).getStone());
        assertEquals(4, goBoardModel13x0x0.getCapturedByWhite());
    }

    @Test
    @DisplayName("Testing adding and removing GameListeners")
    void testGameListeners() {
        GameListener gameListener = new GameListener() {
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
            }
        };

        //Listener size is 1 at the beginning because of the Listener "MoveHistory" which is added at model construction
        assertEquals(1, goBoardModel9x0x0.getGameListeners().size());
        goBoardModel9x0x0.addGameListener(gameListener);
        assertEquals(2, goBoardModel9x0x0.getGameListeners().size());

        goBoardModel9x0x0.removeGameListener(gameListener);
        assertEquals(1, goBoardModel9x0x0.getGameListeners().size());
    }

    @Test
    @DisplayName("Testing reset method and if GameListeners are notified by reset")
    void reset() {
        goBoardModel13x0x0.makeMove(1, 1);
        goBoardModel13x0x0.reset();
        assertEquals(Stone.NONE, goBoardModel13x0x0.getField(1, 1).getStone());
    }


    @Test
    @DisplayName("Testing passing mechanic")
    void pass() {
        goBoardModel19x0x0.makeMove(1, 1); //Black
        assertEquals(GameState.WHITE_TURN, goBoardModel19x0x0.getGameState());
        goBoardModel19x0x0.makeMove(1, 2); //White
        goBoardModel19x0x0.makeMove(1, 3); //Black
        goBoardModel19x0x0.pass(); //White
        assertEquals(GameState.BLACK_TURN, goBoardModel19x0x0.getGameState());
        goBoardModel19x0x0.pass(); //Black
        assertEquals(GameState.BLACK_WON, goBoardModel19x0x0.getGameState());
        goBoardModel9x0x0.pass();
    }

    @Test
    @DisplayName("Testing notification of GameListeners")
    void testListenerNotification(){
        GameListener gameListener = new GameListener() {
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
            }
        };

        goBoardModel19x05x3.addGameListener(gameListener);
        goBoardModel19x05x3.makeMove(3,3);
        goBoardModel19x05x3.makeMove(3,9);
        goBoardModel19x05x3.makeMove(3,15);
        goBoardModel19x05x3.makeMove(1,1);
        goBoardModel19x05x3.pass();
        goBoardModel19x05x3.reset();
        goBoardModel19x05x3.playerResigned();

    }

    @Test
    @DisplayName("Verify if game ends correctly with Draw")
    void gameEndsWithDraw() {
        goBoardModel9x0x0.makeMove(1, 0);
        goBoardModel9x0x0.makeMove(0, 7);
        goBoardModel9x0x0.makeMove(0, 1);
        goBoardModel9x0x0.makeMove(1, 8);
        goBoardModel9x0x0.makeMove(1, 1);
        goBoardModel9x0x0.makeMove(1, 7);
        goBoardModel9x0x0.pass();
        goBoardModel9x0x0.pass();
        assertEquals(GameState.DRAW, goBoardModel9x0x0.getGameState());
    }
    @Test
    @DisplayName("Verify if game ends correctly")
    void gameEndsWithResign() {
        goBoardModel9x0x0.makeMove(1, 0);
        goBoardModel9x0x0.makeMove(0, 7);
        goBoardModel9x0x0.makeMove(0, 1);
        goBoardModel9x0x0.makeMove(1, 8);
        goBoardModel9x0x0.makeMove(1, 1);
        goBoardModel9x0x0.makeMove(1, 7);
        goBoardModel9x0x0.playerResigned();
        assertEquals(GameState.WHITE_WON, goBoardModel9x0x0.getGameState());
    }
}