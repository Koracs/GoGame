package com.gogame.controller;

import com.gogame.model.*;
import com.gogame.view.GoBoardView;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * The GoBoardController handles interactions between the user / view and the go board model
 */
public class GoBoardController {
    //region Fields
    // MVC variables
    private final GoBoardModel model;
    private final GoBoardView view;

    private boolean drawMoveHistory;
    //endregion

    /**
     * Constructs a Controller for a GoBoard
     * @param model GoBoard model to be controlled
     * @param view GoBoard view to be controlled
     */
    public GoBoardController(GoBoardModel model, GoBoardView view) {
        this.model = model;
        this.view = view;
    }

    //region Methods
    /**
     * Invoked when the mouse is clicked on the view. It checks the button that was clicked
     * and either places a stone or a marking
     * @param e The MouseEvent representing the mouse click
     */
    public void mouseClicked(MouseEvent e){
        if(e.getButton() == MouseButton.PRIMARY) placeStone(e);
        else if(e.getButton() == MouseButton.SECONDARY) placeMarking(e);
    }

    /**
     * Places a stone on the board based on the coordinates of the mouse click
     * if the calculated indices are within the valid range
     * @param e The MouseEvent representing the mouse click event
     */
    private void placeStone(MouseEvent e) {
        int row = getNearestRow(e.getY());
        int col = getNearestCol(e.getX());
        if (row < 0 || col < 0 || row >= model.getSize() || col >= model.getSize()) return;

        model.makeMove(row, col);
    }

    /**
     * Returns the nearest row index for a given y-coordinate based on the current scale of the view
     * @param y The y-coordinate value
     * @return The nearest row index
     */
    public int getNearestRow(double y) {
        return (int) Math.round(y / view.getScale() - 1);
    }

    /**
     * Returns the nearest column index for a given x-coordinate based on the current scale of the view
     *
     * @param x The x-coordinate value
     * @return The nearest column index
     */
    public int getNearestCol(double x) {
        return (int) Math.round(x / view.getScale() - 1);
    }

    /**
     * Resets the model
     */
    public void resetModel() {
        model.reset();
    }

    /**
     * Lets the current player pass
     */
    public void passPlayer() {
        model.pass();
    }

    /**
     * Lets the current player resign
     */
    public void resign() {
        model.playerResigned();
    }

    /**
     * Makes a move on the game board by invoking the corresponding method in the model
     * @param row The row index of the move
     * @param col The column index of the move
     */
    public void makeMove(int row, int col) {
        if (row < 0 || col < 0 || row >= model.getSize() || col >= model.getSize()) return;
        model.makeMove(row, col);
    }

    /**
     * Places a marking on the board based on the coordinates of the mouse click
     * @param e The MouseEvent representing the mouse click
     */
    private void placeMarking(MouseEvent e) {
        int row = getNearestRow(e.getY());
        int col = getNearestCol(e.getX());
        if (row < 0 || col < 0 || row >= model.getSize() || col >= model.getSize()) return;

        view.setMarking(row, col);
    }
    /**
     * Checks if the drawMoveHistory flag is enabled
     * @return If the move history should be drawn
     */
    public boolean isDrawMoveHistory() {
        return drawMoveHistory;
    }

    /**
     * Sets the drawMoveHistory flag to control whether the move history should be drawn or not
     * @param drawMoveHistory True to enable drawing the move history, false to disable it
     */
    public void setDrawMoveHistory(boolean drawMoveHistory) {
        this.drawMoveHistory = drawMoveHistory;
        view.draw();
    }
    //endregion
}
