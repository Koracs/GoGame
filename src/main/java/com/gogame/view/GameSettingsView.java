package com.gogame.view;

import com.gogame.controller.GameSettingsController;
import com.gogame.model.GameSettingsModel;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.regex.Pattern;

public class GameSettingsView {
    //region Fields
    // Set style of button
    private final String STYLE = "-fx-background-color:\n" +
            "            linear-gradient(#ffd65b, #e68400),\n" +
            "            linear-gradient(#ffef84, #f2ba44),\n" +
            "            linear-gradient(#ffea6a, #efaa22),\n" +
            "            linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n" +
            "            linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));\n" +
            "    -fx-background-radius: 30;\n" +
            "    -fx-background-insets: 0,1,2,3,0;\n" +
            "    -fx-text-fill: #654b00;\n" +
            "    -fx-font-weight: bold;\n" +
            "    -fx-font-size: 14px;\n" +
            "    -fx-padding: 10 20 10 20;";

    // Pane of this class
    private BorderPane pane;

    // MVC variables
    private GameSettingsController controller;
    private GameSettingsModel model;

    // Constants
    private final String SIZE_9 = "9x9";
    private final String SIZE_13 = "13x13";
    private final String SIZE_19 = "19x19";
    //endregion

    // Constructor
    public GameSettingsView() {
        model = new GameSettingsModel(this);
        controller = new GameSettingsController(model, this);
        this.setController(controller);
        this.setModel(model);
        drawScene();
    }

    //region Getter/Setter
    public BorderPane getPane() {
        return this.pane;
    }

    public void setModel(GameSettingsModel model) {
        this.model = model;
    }

    public void setController(GameSettingsController controller) {
        this.controller = controller;
        controller.setView(this);
    }
    //endregion

    //region Methods
    private void drawScene() {
        pane = new BorderPane();
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        pane.setCenter(vBox);

        // Set field size
        ToggleGroup boardSizeButtonGroup = new ToggleGroup();

        RadioButton size_nine = new RadioButton(SIZE_9);
        size_nine.setToggleGroup(boardSizeButtonGroup);
        size_nine.setOnMouseClicked(e -> model.setBoardSize(9));
        RadioButton size_thirteen = new RadioButton(SIZE_13);
        size_thirteen.setToggleGroup(boardSizeButtonGroup);
        size_thirteen.setOnMouseClicked(e -> model.setBoardSize(13));
        RadioButton size_nineteen = new RadioButton(SIZE_19);
        size_nineteen.setToggleGroup(boardSizeButtonGroup);
        size_nineteen.setSelected(true);
        size_nineteen.setOnMouseClicked(e -> model.setBoardSize(19));

        HBox hBox = new HBox(size_nine, size_thirteen, size_nineteen);
        hBox.setSpacing(10);
        vBox.getChildren().add(hBox);

        // Activate komi and set it
        CheckBox komiCheckBox = new CheckBox("Activate komi");
        TextField komiSetting = new TextField("0.5");
        komiCheckBox.selectedProperty().addListener(e -> {
            model.changeKomiActive();
            komiSetting.setDisable(!komiSetting.isDisabled());
        });
        komiSetting.setDisable(true);
        komiSetting.textProperty().addListener((observable, oldValue, newValue) -> {
            if(Pattern.matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$", newValue)) {
                model.setKomi(Double.parseDouble(newValue));
            }
        });
        VBox vBoxKomi = new VBox(komiCheckBox, komiSetting);
        vBoxKomi.setSpacing(10);
        vBox.getChildren().add(vBoxKomi);

        // Activate handicap and set it
        CheckBox handicapCheckBox = new CheckBox("Activate handicap");
        TextField handicapSetting = new TextField("0");
        handicapCheckBox.selectedProperty().addListener(e -> {
            model.changeHandicapActive();
            handicapSetting.setDisable(!handicapSetting.isDisabled());
        });
        handicapSetting.setDisable(true);
        handicapSetting.textProperty().addListener((observable, oldValue, newValue) -> {
            if(Pattern.matches("/^\\d+$/", newValue)) {
                model.setHandicap(Integer.parseInt(newValue));
            }
        });
        VBox vBoxHandicap = new VBox(handicapCheckBox, handicapSetting);
        vBoxHandicap.setSpacing(10);
        vBox.getChildren().add(vBoxHandicap);

        // Start game button
        Button startGame = new Button("Start Game");
        startGame.setStyle(STYLE);
        startGame.setOnMouseClicked(e -> controller.changeSceneToGameScene());
        pane.setBottom(startGame);
    }
    //endregion

}
