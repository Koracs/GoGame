package com.gogame.model;

public class GoField {
    private Stone stone;


    public GoField(){
        this.stone = Stone.NONE;
    }
    public GoField(Stone stone){
        this.stone = stone;
    }

    public Stone getStone() {
        return stone;
    }

    public boolean isEmpty() {
        return this.stone == Stone.NONE||this.stone == Stone.PRESET;
    }

    public void setStone(Stone stone) {
        this.stone = stone;
    }

    public void removeStone(){
        this.stone = Stone.NONE;
    }

    @Override
    public String toString() {
        return stone + " ";
    }
}
