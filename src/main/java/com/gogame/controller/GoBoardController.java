package com.gogame.controller;

import com.gogame.model.*;
import com.gogame.view.GoBoardView;
import com.gogame.view.WinScreenView;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GoBoardController {
    //region Fields
    // MVC variables
    private GoBoardModel model;
    private GoBoardView view;

    // Constants
    private final String FILENAME = "gamedata_";
    //endregion

    // Constructor
    public GoBoardController(GoBoardModel model, GoBoardView view) {
        this.model = model;
        this.view = view;
    }

    //region Getter/Setter
    public void setModel(GoBoardModel model) {
        this.model = model;
    }

    public void setView(GoBoardView view) {
        this.view = view;
    }
    //endregion

    //region Methods
    public void mouseClicked(MouseEvent e) {
        //System.out.println("controller clicked at X: " + e.getX() + " Y: " + e.getY() + " TileSize: " + view.getScale());
        int x = (int)Math.round((e.getX()) / view.getScale()-1);
        int y = (int)Math.round((e.getY()) / view.getScale()-1);

        System.out.println(x + " " + y);
        model.makeMove(x,y);
    }

    public void resetModel(){
        model.reset();
    }

    public void passPlayer() {
        System.out.println("Player " + model.getCurrentPlayer() + " passed!");
        model.pass();
    }

    public void changeSceneToWinScreen() {
        // Switch player to get the winner
        model.switchPlayer();
        WinScreenView nextView = new WinScreenView(model.getCurrentPlayer().toString());
        Window w = view.getPane().getScene().getWindow();
        if(w instanceof Stage) {
            Stage s = (Stage) w;
            s.setScene(new Scene(nextView.getPane(),500,600));
        }
    }

    public void openImportFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(view.getScene().getWindow());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));
            String line = reader.readLine();

            // Read metadata
            if(line != null) {
                resetModel();
                //todo Repaint view ---------------- needs to be implemented
                view.draw();
                line = reader.readLine();
            }

            //todo Check if the input file is in right format
            while (line != null) {
                String[] temp = line.split(";");
                if(temp.length == 1) {
                    passPlayer();
                } else {
                    model.makeMove(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
                }

                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportFile() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(view.getScene().getWindow());
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_YYYY");
            LocalDateTime now = LocalDateTime.now();
            FileWriter fileWriter = new FileWriter(selectedDirectory.getAbsolutePath() + "\\" + FILENAME + dtf.format(now) + ".txt");
            fileWriter.write(model.getGameDataStorage());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    //endregion
}
