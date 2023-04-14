package com.gogame.view;

import com.gogame.controller.StartScreenController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class StartScreenView extends View {
    //region Fields
    private Stage primaryStage;

    // Pane of this class
    private BorderPane pane;

    // MVC variables
    private final StartScreenController controller;
    //endregion

    //region Constructor
    public StartScreenView(Stage stage) {
        this.controller = new StartScreenController(this);
        primaryStage = stage;
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

        // Create buttons for selecting game mode
        Button startGame = new Button("Start game");
        startGame.setOnMouseClicked(e -> controller.changeSceneToGameSettingsScreen());

        Button tutorial = new Button("Tutorial");
        tutorial.setOnMouseClicked(e -> controller.changeSceneToTutorialSettingScreen());

        Button settingsButton = new Button("Settings");
        Image image = new Image(getClass().getResourceAsStream("/pictures/settings.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(settingsButton.getFont().getSize());
        imageView.setFitWidth(settingsButton.getFont().getSize());
        settingsButton.setGraphic(imageView);
        settingsButton.setOnMouseClicked(e -> showSettingsPopUp());

        FlowPane pa = new FlowPane(startGame, tutorial, settingsButton);
        pa.setHgap(10);
        pa.setAlignment(Pos.CENTER);
        pane.setCenter(pa);
    }
    private void showSettingsPopUp() {
        Popup popup = new Popup();

        //Create Content for PopUp
        Label label = new Label("Settings");
        Button close = new Button("Close");
        Button setup1 = new Button("Set Background");
        Button setup2 = new Button("Turn Music Off");

        close.setOnAction(e -> popup.hide());

        VBox content = new VBox(label, close,setup1, setup2);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);

        // Set content and show popup
        popup.getContent().add(content);
        popup.setAutoHide(true);
        popup.show(primaryStage);

    }
    //endregion
}
