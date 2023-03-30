package com.gogame.view;

import com.gogame.controller.GameSettingsController;
import com.gogame.model.GameSettingsModel;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.regex.Pattern;

public class GameSettingsView extends View {
    //region Fields
    // Set style of button
    private final String STYLE = """
            -fx-background-color:
                        linear-gradient(#ffd65b, #e68400),
                        linear-gradient(#ffef84, #f2ba44),
                        linear-gradient(#ffea6a, #efaa22),
                        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),
                        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));
                -fx-background-radius: 30;
                -fx-background-insets: 0,1,2,3,0;
                -fx-text-fill: #654b00;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                -fx-padding: 10 20 10 20;
            """;

    // Pane of this class
    private BorderPane pane;

    // MVC variables
    private GameSettingsController controller;
    private GameSettingsModel model;

    //endregion

    //region Constructor
    public GameSettingsView() {
        model = new GameSettingsModel(this);
        controller = new GameSettingsController(model, this);
        this.setController(controller);
        this.setModel(model);
        drawScene();
    }
    //endregion

    //region Getter/Setter
    @Override
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
    @Override
    protected void drawScene() {
        pane = new BorderPane();
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        pane.setCenter(vBox);

        // Set field size
        ToggleGroup boardSizeButtonGroup = new ToggleGroup();
        HBox hBox = new HBox();

        for (int i : model.getSizes()) {
            RadioButton button = new RadioButton(i + "x" + i);
            button.setToggleGroup(boardSizeButtonGroup);
            button.setOnMouseClicked(e -> model.setBoardSize(i));
            hBox.getChildren().add(button);
            button.setSelected(i == model.getBoardSize());
        }

        hBox.setSpacing(10);
        vBox.getChildren().add(hBox);

        // Activate komi and set it
        CheckBox komiCheckBox = new CheckBox("Activate komi");
        TextField komiSetting = new TextField("0.5");
        komiCheckBox.selectedProperty().addListener(e -> {
            model.changeKomiActive();
            komiSetting.setDisable(!komiSetting.isDisabled());
        });
        komiSetting.setDisable(!model.isKomiActive());
        komiSetting.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Pattern.matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$", newValue)) { //todo only set value on "start game"?
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
        handicapSetting.setDisable(!model.isHandicapActive());
        handicapSetting.textProperty().addListener((observable, oldValue, newValue) -> { //todo only set value on "start game"?
            if (Pattern.matches("/^\\d+$/", newValue)) {
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
