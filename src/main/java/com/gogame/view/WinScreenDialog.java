package com.gogame.view;

import com.gogame.model.GoBoardModel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The WinScreenDialog displays the Result of a game of go. It shows the points of each player after the game has ended.
 */
public class WinScreenDialog extends Alert {
    private final Font LABEL_FONT = Font.font("Verdana", 15);
    private final Font LABEL_FONT_BOLD = Font.font("Verdana", FontWeight.BOLD, 15);
    private final Font TEXT_FONT = Font.font("Verdana", 18);
    private final Font TEXT_FONT_BOLD = Font.font("Verdana", FontWeight.BOLD, 18);

    /**
     * Constructs a WinScreenDialog for the corresponding model.
     * @param model The GoBoardModel to be associated with the win screen.
     */
    public WinScreenDialog(GoBoardModel model) {
        super(AlertType.INFORMATION);
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/icon.png"))));
        stage.getScene().getStylesheets().add(getClass().getResource("/Stylesheet.css").toExternalForm());

        setHeaderText(null);
        setGraphic(null);

        if(model.isPlayerResigned()) {
            setTitle("Game Ended! - Player resigned");
        } else {
            setTitle("Game Ended!");
        }

        BorderPane winScreenPane = new BorderPane();

        Text winText = new Text(model.getGameState().toString());
        winText.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        winText.setEffect(ds);
        StackPane textPane = new StackPane(winText);
        winScreenPane.setTop(textPane);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(20);
        gridPane.setVgap(10);

        Label capturePoints = new Label("Capture points:");
        capturePoints.setFont(LABEL_FONT);
        GridPane.setHalignment(capturePoints, HPos.RIGHT);
        gridPane.add(capturePoints, 0, 1);

        Label territoryPoints = new Label("Territory points:");
        territoryPoints.setFont(LABEL_FONT);
        GridPane.setHalignment(territoryPoints, HPos.RIGHT);
        gridPane.add(territoryPoints, 0, 2);

        Label komi = new Label("Komi:");
        komi.setFont(LABEL_FONT);
        GridPane.setHalignment(komi, HPos.RIGHT);
        gridPane.add(komi, 0, 3);

        Label score = new Label("Score:");
        score.setFont(LABEL_FONT_BOLD);
        GridPane.setHalignment(score, HPos.RIGHT);
        gridPane.add(score, 0, 4);

        Text white = new Text("White");
        white.setFont(TEXT_FONT);
        gridPane.add(white, 1, 0);
        Text black = new Text("Black");
        black.setFont(TEXT_FONT);
        gridPane.add(black, 2, 0);


        Text capturedByWhite = new Text(String.valueOf(model.getCapturedByWhite()));
        GridPane.setHalignment(capturedByWhite, HPos.CENTER);
        capturedByWhite.setFont(TEXT_FONT);
        gridPane.add(capturedByWhite, 1, 1);
        Text capturedByBlack = new Text(String.valueOf(model.getCapturedByBlack()));
        GridPane.setHalignment(capturedByBlack, HPos.CENTER);
        capturedByBlack.setFont(TEXT_FONT);
        gridPane.add(capturedByBlack, 2, 1);

        Text territoryByWhite = new Text(String.valueOf(model.getPointsWhite() - model.getCapturedByWhite()));
        GridPane.setHalignment(territoryByWhite, HPos.CENTER);
        territoryByWhite.setFont(TEXT_FONT);
        gridPane.add(territoryByWhite, 1, 2);
        Text territoryByBlack = new Text(String.valueOf(model.getPointsBlack() - model.getCapturedByBlack()));
        GridPane.setHalignment(territoryByBlack, HPos.CENTER);
        territoryByBlack.setFont(TEXT_FONT);
        gridPane.add(territoryByBlack, 2, 2);

        Text komiWhite = new Text(String.valueOf(model.getKomi()));
        GridPane.setHalignment(komiWhite, HPos.CENTER);
        komiWhite.setFont(TEXT_FONT);
        gridPane.add(komiWhite, 1, 3);
        Text komiBlack = new Text("0");
        GridPane.setHalignment(komiBlack, HPos.CENTER);
        komiBlack.setFont(TEXT_FONT);
        gridPane.add(komiBlack, 2, 3);

        Text scoreWhite = new Text(String.valueOf(model.getPointsWhite()));
        GridPane.setHalignment(scoreWhite, HPos.CENTER);
        scoreWhite.setFont(TEXT_FONT_BOLD);
        gridPane.add(scoreWhite, 1, 4);
        Text scoreBlack = new Text(String.valueOf(model.getPointsBlack()));
        GridPane.setHalignment(scoreBlack, HPos.CENTER);
        scoreBlack.setFont(TEXT_FONT_BOLD);
        gridPane.add(scoreBlack, 2, 4);


        winScreenPane.setCenter(gridPane);
        getDialogPane().setContent(winScreenPane);
    }
}
