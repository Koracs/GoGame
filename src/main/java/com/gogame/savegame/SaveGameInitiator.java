package com.gogame.savegame;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameState;
import com.gogame.model.GoBoardModel;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SaveGameInitiator {
    //region Constants
    private final String METADATA_REGEX = "\\d(\\d)?;\\d;[0-7]\\.0|5";
    private final String PASS_REGEX = "Black|White player passed.";
    private final String MOVE_REGEX = "\\d(\\d)?;\\d(\\d)?- (White)|(Black)";
    private final String HANDICAP_REGEX = "\\d(\\d)?;\\d(\\d)?- Place handicap stones.";
    //endregion

    private int size;
    private double komi;
    private int handicap;

    GoBoardModel model;

    private List<String> moveLines;

    public SaveGameInitiator() {
        moveLines = new ArrayList<>();
    }

    public SaveGameInitiator(GoBoardModel model) {
        this.model = model;
    }

    public GoBoardModel createModel(String filePath) {
        try {
            readData(filePath);
            model = new GoBoardModel(size, komi, handicap);
            simulateMoves();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return model;
    }

    private void readData(String filePath) throws IOException {
        Pattern metaFataPattern = Pattern.compile(METADATA_REGEX);
        Pattern passPattern = Pattern.compile(PASS_REGEX);
        Pattern movePattern = Pattern.compile(MOVE_REGEX);
        Pattern handicapPattern = Pattern.compile(HANDICAP_REGEX);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
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
        for(String line : moveLines) {
            String[] temp = line.split(";");

            if(temp.length == 1) {
                model.pass();
            } else {
                model.makeMove(Integer.parseInt(temp[0]), Integer.parseInt(temp[1].split("-")[0]));
            }
        }
    }

    public void createSaveFile(File file) throws IOException {
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
