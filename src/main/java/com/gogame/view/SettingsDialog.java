package com.gogame.view;

import com.gogame.controller.GameScreenController;
import com.gogame.model.GoBoardModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class SettingsDialog extends Alert {

    public SettingsDialog(GameScreenController controller){
        super(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/icon.png"))));
        stage.getScene().getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());

        setHeaderText(null);
        setGraphic(null);
        setTitle("Game Settings");

        BorderPane AlertPane = new BorderPane();
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        AlertPane.setCenter(vBox);

        // Set field size
        ToggleGroup boardSizeButtonGroup = new ToggleGroup();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        for (int i : GoBoardModel.getSizes()) {
            RadioButton button = new RadioButton(i + "x" + i);
            button.setToggleGroup(boardSizeButtonGroup);
            button.setOnMouseClicked(e -> controller.setBoardSize(i));
            hBox.getChildren().add(button);
            button.setSelected(i == controller.getBoardSize());
        }

        hBox.setSpacing(20);
        hBox.setPadding(new Insets(10));
        vBox.getChildren().add(hBox);
        vBox.setAlignment(Pos.CENTER);

        // Activate komi and set it
        CheckBox komiCheckBox = new CheckBox("Activate komi");
        Spinner<Double> komiSetting = new Spinner<>();

        SpinnerValueFactory<Double> valueFactoryKomi = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 7.5, controller.getKomi(), 0.5);
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
        vBoxKomi.setAlignment(Pos.CENTER);
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
        vBoxHandicap.setAlignment(Pos.CENTER);
        vBox.getChildren().add(vBoxHandicap);

        getDialogPane().setContent(AlertPane);
    }
}
