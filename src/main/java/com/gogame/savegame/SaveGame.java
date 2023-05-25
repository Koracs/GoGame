package com.gogame.savegame;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.model.GoBoardModel;
import javafx.scene.control.Alert;
import javafx.stage.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
@Deprecated
public class SaveGame {
    //region Constants
    private final String METADATA_REGEX = "\\d(\\d)?;\\d;[0-7]\\.0|5";
    private final String PASS_REGEX = "Black|White player passed.";
    private final String MOVE_REGEX = "\\d(\\d)?;\\d(\\d)?- (White)|(Black)";
    private final String HANDICAP_REGEX = "\\d(\\d)?;\\d(\\d)?- Place handicap stones.";
    //endregion

    //region Fields
    private GoBoardController goBoardController;
    private GameScreenController gameScreenController;
    private StringBuilder gameDataStorage;
    private List<String> data;
    private String filePath;
    private String[] meta;
    private int index;
    //endregion

    // Constructor
    public SaveGame(GoBoardController goBoardController, GameScreenController gameScreenController) {
        this.goBoardController = goBoardController;
        this.gameScreenController = gameScreenController;
        this.gameDataStorage = new StringBuilder(goBoardController.getSize() + ";" + goBoardController.getHandicap() + ";" + goBoardController.getKomi() + "\n");
    }

    public SaveGame(String path) {
        this.filePath = path;
        this.goBoardController = null;
        this.gameScreenController = null;
        this.gameDataStorage = null;
    }

    //region Getter/Setter
    public String getGameDataStorage() {
        return gameDataStorage.toString();
    }

    public void setGameDataStorage(StringBuilder sb) {
        this.gameDataStorage = sb;
    }

    public void initSaveGame(GoBoardController goBoardController) {
        this.goBoardController = goBoardController;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    //endregion

    //region Methods
    public void resetData() {
        this.gameDataStorage = new StringBuilder(goBoardController.getSize() + ";" + goBoardController.getHandicap() + ";" + goBoardController.getKomi() + "\n");
    }

    public void storeData(String s) {
        this.gameDataStorage.append(s);
    }

    public GoBoardModel importFileData() {
        try {
            if(readData(filePath)) {
                index = 1;
                // Valid input check - now load game
                return new GoBoardModel(Integer.parseInt(meta[0]), Double.parseDouble(meta[2]), Integer.parseInt(meta[1]));
            }
        } catch (IOException e) {
            createAlert(Alert.AlertType.ERROR, "Runtime Exception", null, e.getMessage());
        }

        return null;
    }

    /*
    public void importGameFile(boolean tutorial) {
        if(gameScreenController == null || goBoardController == null || gameDataStorage == null || view == null) {
            createAlert(Alert.AlertType.ERROR, "Error", null, "Fields not initialized");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(view.getScene().getWindow());

        if(selectedFile == null) {
            // Closed file chooser manually
            return;
        }

        try {
            if(readData(selectedFile.getAbsolutePath())) {
                // Valid input check - now load game
                GoBoardModel newModel = new GoBoardModel(Integer.parseInt(meta[0]), Double.parseDouble(meta[2]), Integer.parseInt(meta[1]));
                newModel.setGameListeners(goBoardController.getModel().getGameListeners());
                goBoardController.getView().setBoardSize(Integer.parseInt(meta[0]));
                goBoardController.setViewModel(newModel);
                gameScreenController.setViewModel(newModel);

                if(!tutorial) {
                    simulateMove(data.size());
                }
            }
        } catch (IOException e) {
            createAlert(Alert.AlertType.ERROR, "Runtime Exception", null, e.getMessage());
        }
    }*/

    public void importGameFile(String filePath, boolean tutorial) {
        try {
            if(filePath == null || filePath.equals("")) {
                return;
            }

            if(readData(filePath)) {
                // Valid input check - now load game
                GoBoardModel newModel = new GoBoardModel(Integer.parseInt(meta[0]), Double.parseDouble(meta[2]), Integer.parseInt(meta[1]));
                newModel.setGameListeners(goBoardController.getModel().getGameListeners());
                goBoardController.getView().setBoardSize(Integer.parseInt(meta[0]));
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

    private boolean readData(String filePath) throws IOException {
        if(filePath == null || filePath.equals("")) {
            return false;
        }

        Pattern metadata = Pattern.compile(METADATA_REGEX);
        Pattern pass = Pattern.compile(PASS_REGEX);
        Pattern move = Pattern.compile(MOVE_REGEX);
        Pattern handicap = Pattern.compile(HANDICAP_REGEX);

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();
        data = new ArrayList<>();

        // Read data
        if (line != null) {
            // Check if valid input file
            if (!metadata.matcher(line).matches()) {
                createAlert(Alert.AlertType.ERROR, "Error", "Input file is not in the right format", line);
                return false;
            }

            data.add(line);

            line = reader.readLine();

            while (line != null) {
                if (move.matcher(line).find() || pass.matcher(line).matches() || handicap.matcher(line).matches()) {
                    data.add(line);
                } else {
                    // Wrong input
                    createAlert(Alert.AlertType.ERROR, "Error", "Input file is not in the right format", line);
                    return false;
                }
                line = reader.readLine();
            }

            reader.close();

            meta = data.get(0).split(";");
            int size = Integer.parseInt(meta[0]);

            int[] sizes = GoBoardModel.getSizes();
            boolean validInput = false;
            for (int i = 0; i < sizes.length; i++) {
                if (sizes[i] == size) {
                    validInput = true;
                }
            }

            if (!validInput) {
                // Invalid size
                createAlert(Alert.AlertType.ERROR, "Error", "Input file contains invalid line", meta[0]);
                return false;
            }
            return true;
        }
        createAlert(Alert.AlertType.ERROR, "Error", null, "No data in file");
        return false;
    }

    public void loadGradually(boolean forward) {
        if(goBoardController == null) {
            createAlert(Alert.AlertType.ERROR, "Error", null, "Fields not initialized");
            return;
        }

        if(forward) {
            if(index >= data.size()) {
                createAlert(Alert.AlertType.INFORMATION, "Information", null, "End of tutorial reached");
                return;
            }

            String[] temp = data.get(index).split(";");

            if(temp.length == 1) {
                goBoardController.passPlayer();
            } else {
                goBoardController.makeMove(Integer.parseInt(temp[0]), Integer.parseInt(temp[1].split("-")[0]));
            }

            index++;
        } else {
            if(index <= 1)
                return;
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

    public void exportGameFile(File file) {
        if(gameScreenController == null || goBoardController == null || gameDataStorage == null) {
            createAlert(Alert.AlertType.ERROR, "Error", null, "Fields not initialized");
            return;
        }

        if(file != null) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(gameDataStorage.toString());
                fileWriter.close();

                createAlert(Alert.AlertType.INFORMATION, "Information", null, "Game successfully exported!");
            } catch (IOException e) {
                createAlert(Alert.AlertType.ERROR, "Runtime Exception", null, e.getMessage());
            }
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