package com.gogame.view;

import com.gogame.listener.GameEvent;
import com.gogame.listener.GameListener;
import com.gogame.model.GoBoardModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class CaptureStatus extends FlowPane implements GameListener {

    private static final Label labelWhite = new Label("Captured by White: ");
    private static final Label labelBlack = new Label("Captured by Black: ");
    private static final Text capturedByWhite = new Text("0");
    private static final Text capturedByBlack = new Text("0");

    public CaptureStatus(GoBoardModel model){
        model.addGameListener(this);
        this.setPadding(new Insets(30));
        this.setHgap(10);
        this.setVgap(10);
        this.getChildren().add(labelWhite);
        this.getChildren().add(capturedByWhite);
        this.getChildren().add(labelBlack);
        this.getChildren().add(capturedByBlack);
        this.setMaxWidth(120);
    }

    @Override
    public void moveCompleted(GameEvent event) {
        GoBoardModel model = (GoBoardModel) event.getSource();
        capturedByWhite.setText(String.valueOf(model.getCapturedByWhite()));
        capturedByBlack.setText(String.valueOf(model.getCapturedByBlack()));
    }

    @Override
    public void resetGame(GameEvent event) {
        capturedByWhite.setText("0");
        capturedByBlack.setText("0");
    }

    @Override
    public void playerPassed(GameEvent event) {

    }

    @Override
    public void gameEnded(GameEvent event) {

    }
    //Buttons for capture status

}
