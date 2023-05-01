package com.gogame.controller;

import com.gogame.view.StartScreenView;
import com.gogame.view.TutorialSettingsView;
import com.gogame.view.TutorialView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TutorialSettingsController {
    //region Fields
    private final TutorialSettingsView view;
    private static final String TUTORIAL_DIRECTORY = "src/main/resources/tutorials/";

    //endregion

    // Constructor
    public TutorialSettingsController(TutorialSettingsView view) {
        this.view = view;
    }

    //region Methods

    public List<String> getTutorials() {
        List<String> tutorials = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(TUTORIAL_DIRECTORY))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    String tutorialName = path.getFileName().toString();
                    tutorials.add(tutorialName.substring(0, tutorialName.length() - 4));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tutorials;
    }

    public void changeSceneToTutorialScene(String selectedTutorial) {
        String path = TUTORIAL_DIRECTORY + selectedTutorial + ".txt";
        TutorialView nextView = new TutorialView(path);

        Scene s = view.getPane().getScene();
        Window w = view.getPane().getScene().getWindow();
        if (w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }

    public void changeSceneToStartScreen() {
        Scene s = view.getPane().getScene();
        Window w = s.getWindow();
        StartScreenView nextView = new StartScreenView();
        if (w instanceof Stage stage) {
            Scene scene = new Scene(nextView.getPane(), s.getWidth(), s.getHeight());
            scene.getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());
            stage.setScene(scene);
        }
    }


    //endregion
}
