package com.gogame.view;

import com.gogame.controller.GameSettingsController;
import com.gogame.model.GoBoardModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.regex.Pattern;

public class GameSettingsView extends View {
    //region Fields
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

        // Start game button
        Button startGame = new Button("Start Game");
        startGame.setOnMouseClicked(e -> controller.changeSceneToGameScene());

        FlowPane pa = new FlowPane(startGame);
        pa.setAlignment(Pos.CENTER);
        pa.setPadding(new Insets(30));
        pa.setHgap(10);
        pa.setVgap(10);

        // Back Button
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(10));
        Button backButton = new Button("Back");
        Image image = new Image(getClass().getResourceAsStream("/pictures/backArrow.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(backButton.getFont().getSize());
        imageView.setFitHeight(backButton.getFont().getSize());
        backButton.setGraphic(imageView);
        backButton.setOnMouseClicked(e -> controller.changeSceneToStartScreen());
        topBox.getChildren().add(backButton);

        pane.setBottom(pa);
        pane.setTop(topBox);
    }
    //endregion

}
