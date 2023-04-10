package com.gogame.view;

import com.gogame.controller.GameSettingsController;
import com.gogame.model.GoBoardModel;
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

    private BorderPane pane;

    private final GameSettingsController controller;

    //endregion

    //region Constructor
    public GameSettingsView() {
        controller = new GameSettingsController(this);

        drawScene();
    }
    //endregion

    //region Getter/Setter
    @Override
    public BorderPane getPane() {
        return this.pane;
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

        for (int i : GoBoardModel.getSizes()) {
            RadioButton button = new RadioButton(i + "x" + i);
            button.setToggleGroup(boardSizeButtonGroup);
            button.setOnMouseClicked(e -> controller.setBoardSize(i));
            hBox.getChildren().add(button);
            button.setSelected(i == controller.getBoardSize());
            }

        hBox.setSpacing(10);
        vBox.getChildren().add(hBox);

        //
        // Activate komi and set it
        CheckBox komiCheckBox = new CheckBox("Activate komi");
        Spinner<Double> komiSetting = new Spinner<>();

        SpinnerValueFactory<Double> valueFactoryKomi = new SpinnerValueFactory.DoubleSpinnerValueFactory(0,7.5,controller.getKomi(),0.5);
        komiSetting.setValueFactory(valueFactoryKomi);

        komiCheckBox.selectedProperty().addListener(e -> {
            controller.changeKomiActive();
            komiSetting.setDisable(!komiSetting.isDisabled());
        });
        komiSetting.setDisable(!controller.isKomiActive());

        komiSetting.valueProperty().addListener((observable, oldValue, newValue) -> {
            controller.setKomi(komiSetting.getValue());
        });
        VBox vBoxKomi = new VBox(komiCheckBox, komiSetting);
        vBoxKomi.setSpacing(10);
        vBox.getChildren().add(vBoxKomi);


        // TODO: change max to 6 when small board is chosen.
        // Activate handicap and set it
        CheckBox handicapCheckBox = new CheckBox("Activate handicap");
        Spinner<Integer> handicapSetting = new Spinner<>();

        SpinnerValueFactory<Integer> valueFactoryHandicap = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9, controller.getHandicap());
        handicapSetting.setValueFactory(valueFactoryHandicap);

        handicapCheckBox.selectedProperty().addListener(e -> {
            controller.changeHandicapActive();
            handicapSetting.setDisable(!handicapSetting.isDisabled());
        });
        handicapSetting.setDisable(!controller.isHandicapActive());

        handicapSetting.valueProperty().addListener((observable, oldValue, newValue) -> {
            controller.setHandicap(handicapSetting.getValue());
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
