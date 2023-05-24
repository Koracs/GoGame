package com.gogame.savegame;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameState;
import com.gogame.model.GoBoardModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SaveGameHandler {
    //region Constants
    private final String METADATA_REGEX = "\\d(\\d)?;\\d;[0-7]\\.0|5";
    private final String PASS_REGEX = "Black|White player passed.";
    private final String MOVE_REGEX = "\\d(\\d)?;\\d(\\d)?- (White)|(Black)";
    private final String HANDICAP_REGEX = "\\d(\\d)?;\\d(\\d)?- Place handicap stones.";
    private final File file;
    //endregion

    private int size;
    private double komi;
    private int handicap;
    GoBoardModel model;

    private final List<String> moveLines;
    private int currentMove;

    /**
     * Constructs a handler for save files of a GoBoardModel
     * @param file Save file with list of moves for a GoBoardModel
     */
    public SaveGameHandler(File file) {
        moveLines = new ArrayList<>();
        this.file = file;
    }

    public GoBoardModel getModel() {
        return model;
    }

    /**
     * Creates a GoBoardModel to be used with gameplay interaction
     * @return Model is returned at latest state of moves
     */
    public GoBoardModel createGameModel() {
        createTutorialModel();
        simulateMoves();

        return model;
    }
    /**
     * Creates a GoBoardModel to be used with tutorial interaction
     * @return Model is returned without played moves
     */
    public GoBoardModel createTutorialModel() {
        try {
            readFileData();
            model = new GoBoardModel(size, komi, handicap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return model;
    }

    private void readFileData() throws IOException {
        Pattern metaFataPattern = Pattern.compile(METADATA_REGEX);
        Pattern passPattern = Pattern.compile(PASS_REGEX);
        Pattern movePattern = Pattern.compile(MOVE_REGEX);
        Pattern handicapPattern = Pattern.compile(HANDICAP_REGEX);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();

        if (!metaFataPattern.matcher(line).matches()) {
            throw new IOException("Could not read file. MetaData does not match!");
        }

        String[] metaData2 = line.split(";");
        size = Integer.parseInt(metaData2[0]);
        handicap = Integer.parseInt(metaData2[1]);
        komi = Double.parseDouble(metaData2[2]);

        line = reader.readLine();

        while (line != null) {
            if (movePattern.matcher(line).find() ||
                passPattern.matcher(line).matches() ||
                handicapPattern.matcher(line).matches()) {
                moveLines.add(line);
            } else {
                throw new IOException("Could not read line. Line does not match!");
            }
            line = reader.readLine();
        }

        reader.close();
    }

    private void simulateMoves() {
        for (int i = 0; i < moveLines.size(); i++) {
            simulateMove(i);
        }
    }
    private void simulateMoves(int moves) {
        for (int i = 0; i < moves; i++) {
            simulateMove(i);
        }
    }

    private void simulateMove(int moveNumber) {
        String[] move = moveLines.get(moveNumber).split(";");

        if(move.length == 1) {
            model.pass();
        } else {
            model.makeMove(Integer.parseInt(move[0]), Integer.parseInt(move[1].split("-")[0]));
        }
    }

    /**
     * Simulates the next move for the current Game
     * @return if there are more moves left to simulate
     */
    public boolean simulateNextMove() {
        if(currentMove >= moveLines.size()) return false;
        simulateMove(currentMove++);
        return true;
    }

    /**
     * Simulates the last move for the current Game
     * @return if there are previous moves left to simulate
     */
    public boolean simulateLastMove() {
        if(currentMove < 1) return false;
        model.reset();
        simulateMoves(--currentMove);
        return true;
    }

    public void resetMoves() {
        model.reset();
        currentMove = 0;
    }

    /**
     * Fills a given File with the move infos from the given GoBoardModel
     * @param model Model with moves to be saved
     * @param file in the File to write
     * @throws IOException If an I/O error regarding File operation occurs
     */
    public static void createSaveFile(GoBoardModel model, File file) throws IOException {
        List<GameEvent> moves = model.getHistory().getEvents();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(model.getSize() + ";" + model.getHandicap() + ";" + model.getKomi() + System.lineSeparator());
        for (GameEvent event : moves) {
            String move;
            if (event.getState() == GameState.BLACK_PASSED || event.getState() == GameState.WHITE_PASSED)
                move = event.getState().toString();
            else
                move = event.getRow() + ";" + event.getCol() + "- " + event.getState();

            fileWriter.write(move + System.lineSeparator());
        }
        fileWriter.close();
    }

}
