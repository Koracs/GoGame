package com.gogame.model;

public class GoField {


    private Stone stone;
    //todo add field for name? A3, C5,...


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
        if(this.stone == Stone.NONE) {
            return true;
        }
        return false;
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
