package com.gogame.model;

public class GoField {
    private final int row;
    private final int col;
    private Stone stone;


    public GoField(int row, int col){
        this.row = row;
        this.col = col;
        this.stone = Stone.NONE;
    }
    public GoField(int row, int col,Stone stone){
        this.row = row;
        this.col = col;
        this.stone = stone;
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
        return this.stone == Stone.NONE||this.stone == Stone.PRESET || this.stone == Stone.CAP_BLACK || this.stone == Stone.CAP_WHITE;
    }

    public boolean isPreset() {
        return this.stone == Stone.PRESET;
    }

    public boolean isAllowed(Stone currentPlayer) { //todo Check ko rule
        if(this.stone == Stone.NONE||this.stone == Stone.PRESET) {
            return true;
        }

        if(currentPlayer == Stone.BLACK && this.stone == Stone.CAP_BLACK) {
            return true;
        }

        if(currentPlayer == Stone.WHITE && this.stone == Stone.CAP_WHITE) {
            return true;
        }
        return false;
    }

    public boolean isOtherColor(Stone otherStone){
        if(this.stone == Stone.NONE || this.stone == Stone.PRESET || this.stone == Stone.CAP_BLACK || this.stone == Stone.CAP_WHITE
           || otherStone == Stone.NONE || otherStone == Stone.PRESET || otherStone == Stone.CAP_BLACK || otherStone == Stone.CAP_WHITE) return false;

        return !this.stone.equals(otherStone);
    }

    public boolean isNoEnemy(Stone otherStone){
        return this.isEmpty() || stone == otherStone;
    }

    public void setStone(Stone stone) {
        this.stone = stone;
    }
    public void removeStone(){
        this.stone = Stone.NONE;
    }

    public void setCapStone(Stone currentPlayer) {
        if(currentPlayer == Stone.BLACK) {
            this.stone = Stone.CAP_BLACK;
        } else if(currentPlayer == Stone.WHITE) {
            this.stone = Stone.CAP_WHITE;
        }
    }

    @Override
    public String toString() {
        return stone + " ";
    }
}
