package com.example.project.models.tiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LetterTileTest {

    @Test
    void getLetterTest() {
        LetterTile tile = new LetterTile('a');

        assertEquals('A', tile.getLetter(), "getLetter() should return 'A'.");
    }

    @Test
    void getValueTest() {
        LetterTile tile = new LetterTile('Z');

        assertEquals(10, tile.getValue(), "getValue() should return 10 for 'Z'.");
    }

    @Test
    void getFXMLPathTest() {
        LetterTile tile = new LetterTile('A');
        assertEquals("/com/example/project/SingleTiles/letterTileView.fxml", tile.getFXMLPath(), "The FXML path doesn't match expected path.");
    }
}