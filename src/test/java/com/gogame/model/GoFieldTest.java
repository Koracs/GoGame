package com.gogame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoFieldTest {

    private GoField black;
    private GoField white;
    private GoField preset;
    private GoField empty;


    @BeforeEach
    void setUp() {
        black = new GoField(0,0);
        black.setStone(Stone.BLACK);

        white = new GoField(1,0);
        white.setStone(Stone.WHITE);

        preset = new GoField(0,1);
        preset.setStone(Stone.PRESET);

        empty = new GoField(1,1);
    }

    @Test
    @DisplayName("Verifying expected behavior with different values for the GoField constructor")
    void testConstructor(){
        assertDoesNotThrow(() -> new GoField(0,0));

        assertThrowsExactly(IllegalArgumentException.class, () -> new GoField(-1,0));
        assertThrowsExactly(IllegalArgumentException.class, () -> new GoField(0,-1));
    }

    @Test
    @DisplayName("Testing getter getRow()")
    void getRow() {
        assertEquals(0,black.getRow());
        assertEquals(1,white.getRow());
    }

    @Test
    @DisplayName("Testing getter getCol()")
    void getCol() {
        assertEquals(0,black.getCol());
        assertEquals(1,preset.getCol());
    }

    @Test
    @DisplayName("Verifying correct output of getStone() with every possible stone")
    void getStone() {
        assertEquals(Stone.BLACK,black.getStone());
        assertEquals(Stone.WHITE,white.getStone());
        assertEquals(Stone.PRESET,preset.getStone());
        assertEquals(Stone.NONE,empty.getStone());
    }

    @Test
    @DisplayName("Verifying if isEmpty() shows correct result for every possible stone")
    void isEmpty() {
        assertTrue(empty.isEmpty());
        assertTrue(preset.isEmpty());

        assertFalse(black.isEmpty());
        assertFalse(white.isEmpty());

    }

    @Test
    @DisplayName("Verifying if isPreset() shows correct result for every possible stone")
    void isPreset() {
        assertTrue(preset.isPreset());

        assertFalse(empty.isPreset());
        assertFalse(black.isPreset());
        assertFalse(white.isPreset());
    }

    @Test
    @DisplayName("Testing setter setStone()")
    void setStone() {
        empty.setStone(Stone.BLACK);
        assertEquals(Stone.BLACK,empty.getStone());

        empty.setStone(Stone.WHITE);
        assertEquals(Stone.WHITE,empty.getStone());

        empty.setStone(Stone.NONE);
        assertEquals(Stone.NONE,empty.getStone());

        empty.setStone(Stone.PRESET);
        assertEquals(Stone.PRESET,empty.getStone());
    }

    @Test
    @DisplayName("Testing removal of Stones")
    void removeStone() {
        white.removeStone();
        assertEquals(Stone.NONE,white.getStone());
    }
    @Test
    @DisplayName("Testing behaviour of preset stones")
    void testWasPreset() {
        preset.removeStone();
        assertEquals(Stone.PRESET,preset.getStone());

        preset.setStone(Stone.BLACK);
        preset.removeStone();
        assertEquals(Stone.PRESET,preset.getStone());

        black.setStone(Stone.PRESET);
        assertEquals(Stone.PRESET,black.getStone());
        black.removeStone();
        assertEquals(Stone.PRESET,black.getStone());


    }

    @Test
    @DisplayName("Testing toString()")
    void testToString(){
        String output = """
                GoField{row=0, col=0, stone=BLACK}""";
        assertEquals(output,black.toString());
    }

}