package com.gogame.model;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.view.TutorialView;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SaveGameModel {
    private final GoBoardModel gameModel;
    private StringBuilder gameDataStorage;

    public SaveGameModel(GoBoardModel gameModel) {
        this.gameModel = gameModel;

        gameModel.addGameListener(new GameListener() {
            @Override
            public void moveCompleted(GameEvent event) {
                storeData( event.getRow() + ";" + event.getCol() + "\n");
            }

            @Override
            public void resetGame(GameEvent event) {

            }
        });
    }

    public String getGameDataStorage() {
        return gameDataStorage.toString();
    }

    private void storeData(String s) {
        this.gameDataStorage.append(s);
    }

    public void importGameFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(view.getPane().getScene().getWindow());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));
            String line = reader.readLine();
            StringBuilder sb = new StringBuilder();

            // Read metadata
            if(line != null) {
                model.reset();
                sb.append(line);

                line = reader.readLine();

                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }

                reader.close();
                model.setGameDataStorage(sb);

                // Switch to tutorial view
                //todo Make it also possible to play the game again
                TutorialView nextView = new TutorialView(model);
                Window w = view.getPane().getScene().getWindow();
                if(w instanceof Stage) {
                    Stage s = (Stage) w;
                    s.setScene(new Scene(nextView.getPane(),500,600));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportGameFile() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(view.getPane().getScene().getWindow());
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy");
            LocalDateTime now = LocalDateTime.now();
            FileWriter fileWriter = new FileWriter(selectedDirectory.getAbsolutePath() + "\\" + FILENAME + dtf.format(now) + ".txt");
            fileWriter.write(model.getGameDataStorage());
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
            vBox.setStyle(" -fx-background-color: white;");
            vBox.setAlignment(Pos.CENTER);
            statusPopup.getContent().add(vBox);
            statusPopup.show(view.getPane().getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
