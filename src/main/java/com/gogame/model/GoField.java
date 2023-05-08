package com.gogame.model;

public class GoField {
    private final int row;
    private final int col;
    private Stone stone;

    private boolean wasPreset;


    public GoField(int row, int col){
        this.row = row;
        this.col = col;
        this.stone = Stone.NONE;
        wasPreset = false;
    }
    public GoField(int row, int col,Stone stone){
        this.row = row;
        this.col = col;
        this.stone = stone;
        wasPreset = (stone == Stone.PRESET);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    public Stone getStone() {
        return stone;
    }

    public boolean isEmpty() {
        return this.stone == Stone.NONE||this.stone == Stone.PRESET;
    }

    public boolean isPreset() {
        return this.stone == Stone.PRESET;
    }


    public boolean isNoEnemy(Stone otherStone){
        return this.isEmpty() || stone == otherStone;
    }

    public void setStone(Stone stone) {
        if(!wasPreset) wasPreset = (stone == Stone.PRESET);
        this.stone = stone;
    }
    public void removeStone(){
        if(wasPreset) this.stone = Stone.PRESET;
        else this.stone = Stone.NONE;
    }

    @Override
    public String toString() {
        return "GoField{" +
               "row=" + row +
               ", col=" + col +
               ", stone=" + stone +
               '}';
    }
}
