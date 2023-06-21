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

/**
 * A dialog window for choosing a tutorial. The dialog displays a list of available tutorials as toggle buttons,
 * allowing the user to select a tutorial. The selected tutorial file can be retrieved using the `getSelectedTutorial()`
 * method.
 */
public class TutorialDialog extends Alert {
    private static final String TUTORIAL_DIRECTORY = "./tutorials/";
    private Map<String, File> tutorials;

    private File selectedTutorial;

    /**
     * Constructs a new TutorialDialog. The dialog displays a list of available tutorials as toggle buttons.
     * The selected tutorial file can be retrieved using the `getSelectedTutorial()` method.
     */
    public TutorialDialog() {
        super(AlertType.CONFIRMATION);
        getTutorials();

        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/icon.png"))));
        stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylesheet.css")).toExternalForm());

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

        tutorialGroup.selectedToggleProperty().addListener((observableValue, oldVal, newVal) -> {
            if(newVal == null) oldVal.setSelected(true);
            selectedTutorial = tutorials.get(tutorialGroup.getSelectedToggle().toString().split("'")[1]);
        });

        getDialogPane().setContent(pane);
    }

    /**
     * Retrieves the list of available tutorials from the tutorial directory. The tutorials are stored in a map
     * where the tutorial name is the key and the tutorial file is the value.
     */
    private void getTutorials() {
        Map<String, File> tempTutorials = new HashMap<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(TUTORIAL_DIRECTORY))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    String fileName = path.getFileName().toString();
                    String tutorialName = fileName.substring(0, fileName.length() - 4);
                    tempTutorials.put(tutorialName,path.toFile());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.tutorials = tempTutorials;
    }

    /**
     * Returns the selected tutorial file.
     * @return The selected tutorial file, or null if no tutorial is selected.
     */
    public File getSelectedTutorial() {
        return selectedTutorial;
    }
}
