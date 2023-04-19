package com.gogame.model;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.view.GoBoardView;
import javafx.scene.control.Alert;
import javafx.stage.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SaveGame {
    //region Constants
    private final String FILENAME = "gamedata_";
    private final String EXT = ".txt";

    private final String METADATA_REGEX = "\\d(\\d)?;\\d;[0-7]\\.0|5";
    private final String PASS_REGEX = "Black|White player passed.";
    private final String MOVE_REGEX = "\\d(\\d)?;\\d(\\d)?- (White)|(Black)";
    private final String HANDICAP_REGEX = "\\d(\\d)?;\\d(\\d)?- Place handicap stones.";
    //endregion

    //region Fields
    private final GoBoardController goBoardController;
    private final GameScreenController gameScreenController;
    private final GoBoardView view;
    private StringBuilder gameDataStorage;
    private List<String> data;
    private int index;
    //endregion

    // Constructor
    public SaveGame(GoBoardController goBoardController, GameScreenController gameScreenController) {
        this.goBoardController = goBoardController;
        this.gameScreenController = gameScreenController;
        this.view = goBoardController.getView();
        this.gameDataStorage = new StringBuilder(goBoardController.getSize() + ";" + goBoardController.getHandicap() + ";" + goBoardController.getKomi() + "\n");
    }

    public SaveGame(GoBoardController goBoardController) {
        this.goBoardController = goBoardController;
        this.gameScreenController = null;
        this.view = goBoardController.getView();
        this.gameDataStorage = new StringBuilder(goBoardController.getSize() + ";" + goBoardController.getHandicap() + ";" + goBoardController.getKomi() + "\n");
    }

    //region Getter/Setter
    public String getGameDataStorage() {
        return gameDataStorage.toString();
    }

    public void setGameDataStorage(StringBuilder sb) {
        this.gameDataStorage = sb;
    }
    //endregion

    //region Methods
    public void resetData() {
        this.gameDataStorage = new StringBuilder(goBoardController.getSize() + ";" + goBoardController.getHandicap() + ";" + goBoardController.getKomi() + "\n");
    }

    public void storeData(String s) {
        this.gameDataStorage.append(s);
    }

    public void importGameFile(boolean tutorial) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(view.getScene().getWindow());

        if(selectedFile == null) {
            // Closed file chooser manually
            return;
        }

        Pattern metadata = Pattern.compile(METADATA_REGEX);
        Pattern pass = Pattern.compile(PASS_REGEX);
        Pattern move = Pattern.compile(MOVE_REGEX);
        Pattern handicap = Pattern.compile(HANDICAP_REGEX);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));
            String line = reader.readLine();
            data = new ArrayList<>();
            index = 1;

            // Read data
            if (line != null) {
                // Check if valid input file
                if(!metadata.matcher(line).matches()) {
                    createAlert(Alert.AlertType.INFORMATION, "Error", "Input file is not in the right format", line);
                    return;
                }

                data.add(line);

                line = reader.readLine();

                while (line != null) {
                    if(move.matcher(line).find() || pass.matcher(line).matches() || handicap.matcher(line).matches()) {
                        data.add(line);
                    } else {
                        // Wrong input
                        createAlert(Alert.AlertType.INFORMATION, "Error", "Input file is not in the right format", line);
                        return;
                    }
                    line = reader.readLine();
                }

                reader.close();


                String[] meta = data.get(0).split(";");
                int size = Integer.parseInt(meta[0]);

                int[] sizes = GoBoardModel.getSizes();
                boolean validInput = false;
                for(int i = 0;i < sizes.length;i++) {
                    if(sizes[i] == size) {
                        validInput = true;
                    }
                }

                if(!validInput) {
                    // Invalid size
                    createAlert(Alert.AlertType.INFORMATION, "Error", "Input file contains invalid line", meta[0]);
                    return;
                }

                // Valid input check - now load game
                GoBoardModel newModel = new GoBoardModel(Integer.parseInt(meta[0]), Double.parseDouble(meta[2]), Integer.parseInt(meta[1]));
                newModel.setGameListeners(goBoardController.getModel().getGameListeners());
                goBoardController.getView().setBoardSize(size);
                goBoardController.setViewModel(newModel);
                gameScreenController.setViewModel(newModel);

                if(!tutorial) {
                    simulateMove(data.size());
                }
            }
        } catch (IOException e) {
            createAlert(Alert.AlertType.ERROR, "Runtime Exception", null, e.getMessage());
        }
    }

    public void loadGradually(boolean forward) {
        if(forward) {
            if(index >= data.size())
                return;

            String[] temp = data.get(index).split(";");

            if(temp.length == 1) {
                goBoardController.passPlayer();
            } else {
                goBoardController.makeMove(Integer.parseInt(temp[0]), Integer.parseInt(temp[1].split("-")[0]));
            }

            index++;
        } else {
            index--;
            goBoardController.resetModel();
            simulateMove(index);
        }
    }

    private void simulateMove(int range) {
        for(int i = 1;i < range;i++) {
            String[] temp = data.get(i).split(";");

            if(temp.length == 1) {
                goBoardController.passPlayer();
            } else {
                goBoardController.makeMove(Integer.parseInt(temp[0]), Integer.parseInt(temp[1].split("-")[0]));
            }
        }
    }

    public void exportGameFile() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(view.getScene().getWindow());

        if(selectedDirectory == null) {
            // Closed directory chooser manually
            return;
        }

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy");
            LocalDateTime now = LocalDateTime.now();

            // Check for valid file name
            String filePath = selectedDirectory.getAbsolutePath() + "\\" + FILENAME + dtf.format(now);

            File temp = new File(filePath + EXT);
            if(temp.exists()) {
                int i = 1;
                temp = new File(filePath + "_" + i + EXT);
                while(temp.exists()) {
                    i++;
                    temp = new File(filePath + "_" + i + EXT);
                }

                filePath = filePath + "_" + i;
            }

            FileWriter fileWriter = new FileWriter(filePath + EXT);
            fileWriter.write(gameDataStorage.toString());
            fileWriter.close();

            createAlert(Alert.AlertType.INFORMATION, "Information", null, "Game successfully exported!");
        } catch (IOException e) {
            createAlert(Alert.AlertType.ERROR, "Runtime Exception", null, e.getMessage());
        }
    }

    private void createAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.initStyle(StageStyle.UTILITY);
        alert.setContentText(content);
        alert.showAndWait();
    }
    //endregion
}
