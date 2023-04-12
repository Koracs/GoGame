package com.gogame.controller;

import com.gogame.listener.GameState;
import com.gogame.model.GoBoardModel;
import com.gogame.view.GameScreenView;
import com.gogame.view.TutorialView;
import com.gogame.view.WinScreenView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameScreenController {
    //region Fields
    private final GameScreenView view;
    private final GoBoardModel model;

    // Constants
    private final String FILENAME = "gamedata_";
    //endregion

    // Constructor
    public GameScreenController(GameScreenView view, GoBoardModel model){
        this.view = view;
        this.model = model;
    }

    //region Methods
    public void changeSceneToWinScreen(GameState gameState) {
        // Switch player to get the winner
        //model.switchPlayer();
        WinScreenView nextView = new WinScreenView(gameState.toString()); //Todo implement winner via gameState
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage) {
            Stage s = (Stage) w;
            Scene scene = new Scene(nextView.getPane(), 500, 600);
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            s.setScene(scene);
        }
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
    //endregion

}
