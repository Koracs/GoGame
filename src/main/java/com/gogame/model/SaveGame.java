package com.gogame.model;

import com.gogame.controller.GameScreenController;
import com.gogame.controller.GoBoardController;
import com.gogame.view.GameScreenView;
import com.gogame.view.TutorialView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveGame {
    private final String FILENAME = "gamedata_";
    private final String EXT = ".txt";

    private final GoBoardController goBoardController;
    private final GameScreenController gameScreenController;
    private StringBuilder gameDataStorage;

    public SaveGame(GoBoardController goBoardController, GameScreenController gameScreenController) {
        this.goBoardController = goBoardController;
        this.gameScreenController = gameScreenController;
        this.gameDataStorage = new StringBuilder(goBoardController.getSize() + ";" + goBoardController.getHandicap() + ";" + goBoardController.getKomi() + "\n");
    }

    public String getGameDataStorage() {
        return gameDataStorage.toString();
    }

    public void resetData() {
        this.gameDataStorage = new StringBuilder(goBoardController.getSize() + ";" + goBoardController.getHandicap() + ";" + goBoardController.getKomi() + "\n");
    }

    public void storeData(String s) {
        this.gameDataStorage.append(s);
    }

    public void setGameDataStorage(StringBuilder sb) {
        this.gameDataStorage = sb;
    }

    public void importGameFile() {
        GameScreenView view = gameScreenController.getView();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(view.getPane().getScene().getWindow());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));
            String line = reader.readLine();
            StringBuilder sb = new StringBuilder();

            // Read metadata
            if (line != null) {
                goBoardController.resetModel();
                sb.append(line);

                line = reader.readLine();

                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }

                reader.close();
                setGameDataStorage(sb);

                // Switch to tutorial view
                //todo Make it also possible to play the game again
                TutorialView nextView = new TutorialView(goBoardController.getModel());
                Window w = view.getPane().getScene().getWindow();
                if (w instanceof Stage) {
                    Stage s = (Stage) w;
                    s.setScene(new Scene(nextView.getPane(), 500, 600));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportGameFile() {
        GameScreenView view = gameScreenController.getView();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(view.getPane().getScene().getWindow());
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
            Popup statusPopup = new Popup();

            Label label = new Label("Game successfully exported!");
            label.setMinWidth(80);
            label.setMinHeight(50);

            Button button = new Button("Ok");
            button.setOnAction(e -> {
                statusPopup.hide();
            });

            VBox vBox = new VBox(label, button);
            vBox.getStyleClass().add("popup");
            vBox.setAlignment(Pos.CENTER);
            statusPopup.getContent().add(vBox);
            statusPopup.centerOnScreen();
            statusPopup.show(view.getPane().getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
