package com.gogame.view;

import com.gogame.controller.GameScreenController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TutorialDialog extends Alert {
    private static final String TUTORIAL_DIRECTORY = "src/main/resources/tutorials/";
    private final List<String> tutorials;

    private String tutorialPath;

    public TutorialDialog() {
        super(AlertType.CONFIRMATION);
        tutorials = getTutorials();
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

        for (String tutorial : tutorials) {
            ToggleButton button = new ToggleButton(tutorial);
            button.setToggleGroup(tutorialGroup);

            flowPane.getChildren().add(button);
        }

        tutorialGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            tutorialPath = TUTORIAL_DIRECTORY + tutorialGroup.getSelectedToggle().toString().split("'")[1] +".txt";
        });

        getDialogPane().setContent(pane);
    }


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

    public String getTutorialPath() {
        return tutorialPath;
    }
}
