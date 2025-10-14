package com.example.project.models.tiles;

import com.example.project.testHelpers.MockAudioSystemExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockAudioSystemExtension.class)
class LetterTileTest {

    @Test
    void getLetterTest() {
        LetterTileModel tile = new LetterTileModel('a');

        assertEquals('A', tile.getLetter(), "getLetter() should return 'A'.");
    }

    @Test
    void getValueTest() {
        LetterTileModel tile = new LetterTileModel('Z');

        assertEquals(10, tile.getValue(), "getValue() should return 10 for 'Z'.");
    }

    @Test
    void getFXMLPathTest() {
        LetterTileModel tile = new LetterTileModel('A');
        assertEquals("/com/example/project/SingleTiles/letterTileView.fxml", tile.getFXMLPath(), "The FXML path doesn't match expected path.");
    }
}