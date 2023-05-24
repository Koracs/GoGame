package com.gogame.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TutorialDialog extends Alert {
    private static final String TUTORIAL_DIRECTORY = "src/main/resources/tutorials/";
    private Map<String, File> tutorials;

    private File selectedTutorial;

    public TutorialDialog() {
        super(AlertType.CONFIRMATION);
        getTutorials();

        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/icon.png"))));
        stage.getScene().getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());

        setHeaderText(null);
        setGraphic(null);
        setTitle("Choose Tutorial");

        BorderPane pane = new BorderPane();
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.TOP_CENTER);
        flowPane.setPadding(new Insets(30));
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        pane.setCenter(flowPane);

        ToggleGroup tutorialGroup = new ToggleGroup();

        for (String tutorial : tutorials.keySet()) {
            ToggleButton button = new ToggleButton(tutorial);
            button.setToggleGroup(tutorialGroup);

            flowPane.getChildren().add(button);
        }

        tutorialGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            selectedTutorial = tutorials.get(tutorialGroup.getSelectedToggle().toString().split("'")[1]);
        });

        getDialogPane().setContent(pane);
    }


    private void getTutorials() {
        Map<String, File> tutorials = new HashMap<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(TUTORIAL_DIRECTORY))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    String fileName = path.getFileName().toString();
                    String tutorialName = fileName.substring(0, fileName.length() - 4);
                    tutorials.put(tutorialName,path.toFile());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.tutorials = tutorials;
    }

    public File getSelectedTutorial() {
        return selectedTutorial;
    }
}
