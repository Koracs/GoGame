package com.gogame.model;

import com.gogame.view.GoBoardView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyHandler {
    public static GoBoardView view = null;

    public static void handleKeyPressed(KeyEvent event) {
        if(view != null) {
            if(event.getCode().isLetterKey() && event.getCode() != KeyCode.P) {
                view.moveHoverKeyboard(event);
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.P) {
                view.setStoneKeyboard();
            }
        }
    }
}
