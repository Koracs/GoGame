package com.gogame.model;

/**
 * The GoField class represents a field on the Go board.
 */
public class GoField {
    private final int row;
    private final int col;
    private Stone stone;

    private boolean wasPreset;

    /**
     * Constructs a GoField with the specified row and column indices.
     * @param row The row index of the field.
     * @param col The column index of the field.
     * @throws IllegalArgumentException if the row or column indices are negative.
     */
    public GoField(int row, int col) {
        if (row < 0 || col < 0) throw new IllegalArgumentException("Row and column must be non-negative.");
        this.row = row;
        this.col = col;
        this.stone = Stone.NONE;
        wasPreset = false;
    }

    /**
     * Returns the row index of the field.
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index of the field.
     * @return The column index.
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the stone placed on the field.
     * @return The stone on this field.
     */
    public Stone getStone() {
        return stone;
    }

    /**
     * Checks if the field is empty / preset (no stone placed).
     * @return True if the field is empty / preset, false otherwise.
     */
    public boolean isEmpty() {
        return this.stone == Stone.NONE || this.stone == Stone.PRESET;
    }

    /**
     * Checks if the field is a preset / handicap field.
     * @return True if the field is a preset / handicap field, false otherwise.
     */
    public boolean isPreset() {
        return this.stone == Stone.PRESET;
    }

    /**
     * Sets the stone on the field.
     *
     * @param stone The stone to be set on the field.
     */
    public void setStone(Stone stone) {
        if (stone == Stone.NONE) {
            removeStone();
            return;
        }
        if (!wasPreset) wasPreset = (stone == Stone.PRESET);
        this.stone = stone;
    }

    /**
     * Removes the stone from the field.
     * If the field was a preset field, it sets the stone to Stone.PRESET, otherwise sets it to Stone.NONE.
     */
    public void removeStone() {
        if (wasPreset) this.stone = Stone.PRESET;
        else this.stone = Stone.NONE;
    }

    /**
     * Returns a string representation of the GoField object.
     * @return A string representation of the GoField object.
     */
    @Override
    public String toString() {
        return "GoField{" +
               "row=" + row +
               ", col=" + col +
               ", stone=" + stone +
               '}';
    }
}
