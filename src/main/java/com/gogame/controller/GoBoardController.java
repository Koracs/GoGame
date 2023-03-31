package com.gogame.controller;

import com.gogame.listener.GameState;
import com.gogame.model.*;
import com.gogame.view.GoBoardView;

import javafx.scene.input.MouseEvent;

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
        int row = (int)Math.round((e.getY()) / view.getScale()-1);
        int col = (int)Math.round((e.getX()) / view.getScale()-1);

        if (model.getGameState() == GameState.PLACE_HANDICAP){
            model.makeHandicapMove(row, col);
        } else {
            model.makeMove(row,col);
        }
    }

    public void resetModel(){
        model.reset();
    }

    public void passPlayer() {
        model.pass();
    }


    /*public void openImportFile() {
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
                //todo Repaint view ---------------- needs to be implemented with possible different size
                view.enableGameControlButtons();
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
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy");
            LocalDateTime now = LocalDateTime.now();
            FileWriter fileWriter = new FileWriter(selectedDirectory.getAbsolutePath() + "\\" + FILENAME + dtf.format(now) + ".txt");
            fileWriter.write(model.getGameDataStorage());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }*/
    //endregion
}
