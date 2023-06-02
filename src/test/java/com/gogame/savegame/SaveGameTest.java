package com.gogame.savegame;

import com.gogame.model.GoBoardModel;
import com.gogame.savegame.MoveHistory;
import com.gogame.savegame.SaveGameHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaveGameTest {
    private SaveGameHandler saveGameHandler;
    private MoveHistory moveHistory;
    private GoBoardModel model;
    private final String METADATA_REGEX = "\\d(\\d)?;\\d;[0-7]\\.0|5";
    private final String PASS_REGEX = "(Black|White) passed.";
    private final String MOVE_REGEX = "\\d(\\d)?;\\d(\\d)?- (White)|(Black)";
    private final String TUTORIAL_DIRECTORY = "src/test/resources/CanBlackLive.txt";
    private final String WRONG_TUTORIAL_DIRECTORY = "src/test/resources/CanBlackLiveWRONG.txt";
    private final String FILE_DIRECTORY = "src/test/resources/testFile.txt";

    @BeforeEach
    void setUp() {
        File file = new File(TUTORIAL_DIRECTORY);
        saveGameHandler = new SaveGameHandler(file);

        model = new GoBoardModel(19, 0, 0);
        moveHistory = new MoveHistory(model);

        File tempFile = new File(FILE_DIRECTORY);
        if(tempFile.exists()) {
            tempFile.delete();
        }
    }

    //region Test SaveGameHandler
    @Test
    @DisplayName("Verifying expected behavior with different values for the SaveGameHandler constructor")
    void testSaveGameHandlerConstructor() {
        assertDoesNotThrow(() -> new SaveGameHandler(new File(TUTORIAL_DIRECTORY)));

        assertThrowsExactly(IllegalArgumentException.class, () -> new SaveGameHandler(null));
    }

    @Test
    @DisplayName("Testing getter getModel()")
    void testGetModel() {
       assertNull(saveGameHandler.getModel());

       saveGameHandler.createGameModel();
       assertNotNull(saveGameHandler.getModel());
    }

    @Test
    @DisplayName("Testing creating tutorial model")
    void testCreateTutorialModel() {
        assertNull(saveGameHandler.getModel());

        saveGameHandler.createTutorialModel();
        assertNotNull(saveGameHandler.getModel());
        assertEquals(9, saveGameHandler.getModel().getSize());
        assertEquals(0, saveGameHandler.getModel().getHandicap());
        assertEquals(0.0, saveGameHandler.getModel().getKomi());
        assertEquals(0, saveGameHandler.getModel().getHistory().getEvents().size());

        SaveGameHandler sgh = new SaveGameHandler(new File(WRONG_TUTORIAL_DIRECTORY));
        assertThrowsExactly(RuntimeException.class, sgh::createTutorialModel);
    }

    @Test
    @DisplayName("Testing creating game model")
    void testCreateGameModel() {
        assertNull(saveGameHandler.getModel());

        saveGameHandler.createGameModel();
        assertNotNull(saveGameHandler.getModel());
        assertEquals(9, saveGameHandler.getModel().getSize());
        assertEquals(0, saveGameHandler.getModel().getHandicap());
        assertEquals(0.0, saveGameHandler.getModel().getKomi());
        assertNotEquals(0, saveGameHandler.getModel().getHistory().getEvents().size());

        SaveGameHandler sgh = new SaveGameHandler(new File(WRONG_TUTORIAL_DIRECTORY));
        assertThrowsExactly(RuntimeException.class, sgh::createGameModel);
    }

    @Test
    @DisplayName("Testing simulating next move")
    void testSimulateNextMove() {
        saveGameHandler.createTutorialModel();
        assertEquals(0, saveGameHandler.getModel().getHistory().getEvents().size());

        assertTrue(saveGameHandler.simulateNextMove());
        assertEquals(1, saveGameHandler.getModel().getHistory().getEvents().size());

        assertTrue(saveGameHandler.simulateNextMove());
        assertTrue(saveGameHandler.simulateNextMove());
        assertTrue(saveGameHandler.simulateNextMove());
        assertTrue(saveGameHandler.simulateNextMove());
        assertFalse(saveGameHandler.simulateNextMove());

        saveGameHandler.simulateLastMove();
        assertTrue(saveGameHandler.simulateNextMove());
    }

    @Test
    @DisplayName("Testing simulating last move")
    void testSimulateLastMove() {
        saveGameHandler.createTutorialModel();
        assertEquals(0, saveGameHandler.getModel().getHistory().getEvents().size());

        assertFalse(saveGameHandler.simulateLastMove());
        assertEquals(0, saveGameHandler.getModel().getHistory().getEvents().size());

        saveGameHandler.simulateNextMove();
        saveGameHandler.simulateNextMove();
        assertEquals(2, saveGameHandler.getModel().getHistory().getEvents().size());

        assertTrue(saveGameHandler.simulateLastMove());
        assertEquals(1, saveGameHandler.getModel().getHistory().getEvents().size());
    }

    @Test
    @DisplayName("Testing reset moves")
    void testResetMoves() {
        saveGameHandler.createTutorialModel();
        saveGameHandler.simulateNextMove();
        saveGameHandler.simulateNextMove();
        assertEquals(2, saveGameHandler.getModel().getHistory().getEvents().size());

        saveGameHandler.resetMoves();
        assertEquals(0, saveGameHandler.getModel().getHistory().getEvents().size());

        saveGameHandler.resetMoves();
        assertEquals(0, saveGameHandler.getModel().getHistory().getEvents().size());
    }

    @Test
    @DisplayName("Test creating a save file")
    void testCreateSaveFile() {
        assertThrowsExactly(IllegalArgumentException.class, () -> SaveGameHandler.createSaveFile(null, null));
        assertThrowsExactly(IllegalArgumentException.class, () -> SaveGameHandler.createSaveFile(null, new File(FILE_DIRECTORY)));
        assertThrowsExactly(IllegalArgumentException.class, () -> SaveGameHandler.createSaveFile(model, null));
        assertThrowsExactly(FileNotFoundException.class, () -> SaveGameHandler.createSaveFile(model, new File("")));

        assertFalse(new File(FILE_DIRECTORY).exists());
        assertDoesNotThrow(() -> SaveGameHandler.createSaveFile(model, new File(FILE_DIRECTORY)));
        assertTrue(new File(FILE_DIRECTORY).exists());
        assertTrue(new File(FILE_DIRECTORY).isFile());

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_DIRECTORY));
            String line = reader.readLine();

            Pattern metaFataPattern = Pattern.compile(METADATA_REGEX);
            Pattern passPattern = Pattern.compile(PASS_REGEX);
            Pattern movePattern = Pattern.compile(MOVE_REGEX);

            assertTrue(metaFataPattern.matcher(line).matches());
            assertNull(reader.readLine());

            model.makeMove(0,0);
            SaveGameHandler.createSaveFile(model, new File(FILE_DIRECTORY));
            reader = new BufferedReader(new FileReader(FILE_DIRECTORY));
            line = reader.readLine();
            assertTrue(metaFataPattern.matcher(line).matches());
            line = reader.readLine();
            assertTrue(movePattern.matcher(line).find());
            assertNull(reader.readLine());

            model.reset();
            SaveGameHandler.createSaveFile(model, new File(FILE_DIRECTORY));
            reader = new BufferedReader(new FileReader(FILE_DIRECTORY));
            line = reader.readLine();
            assertTrue(metaFataPattern.matcher(line).matches());
            assertNull(reader.readLine());


            model.pass();
            SaveGameHandler.createSaveFile(model, new File(FILE_DIRECTORY));
            reader = new BufferedReader(new FileReader(FILE_DIRECTORY));
            line = reader.readLine();
            assertTrue(metaFataPattern.matcher(line).matches());
            line = reader.readLine();
            assertTrue(passPattern.matcher(line).matches());
            assertNull(reader.readLine());
        } catch (Exception ex) {
            // To check if an exception happens
            fail();
        }
    }
    //endregion

    //region Test MoveHistory
    @Test
    @DisplayName("Verifying expected behavior with different values for the MoveHistory constructor")
    void testMoveHistoryConstructor() {
        assertDoesNotThrow(() -> new MoveHistory(new GoBoardModel(19, 0, 0)));

        assertThrowsExactly(IllegalArgumentException.class, () -> new MoveHistory(null));
    }


    @Test
    @DisplayName("Testing getter getEvents() and also the listener implementation")
    void testGetEvents() {
        assertEquals(0, moveHistory.getEvents().size());

        model.makeMove(0, 0);
        assertEquals(1, moveHistory.getEvents().size());

        model.pass();
        assertEquals(2, moveHistory.getEvents().size());

        model.reset();
        assertEquals(0, moveHistory.getEvents().size());

        model.makeMove(0,0);
        assertEquals(1, moveHistory.getEvents().size());
    }
    //endregion
}
