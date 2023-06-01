package com.gogame.view;

import javafx.scene.layout.BorderPane;

/**
 * Abstract class that is used for Views of the GoBoard in JavaFX.
 */
abstract class View {
    /**
     * Returns the root pane for a view
     * @return BorderPane node for a scene
     */
    public abstract BorderPane getPane();

    /**
     * Draws the scene
     */
    protected abstract void drawScene();
}
