package com.example.project.models.tiles;

import com.example.project.services.GameSoundPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Tile}.
 */
class TileTests {

    /**
     * here is a simple concrete subclass for testing abstract Tile.
     */
    static class TestTile extends Tile {
        @Override
        public String getFXMLPath() {
            return "/test/path.fxml";
        }
    }

    @Test
    void getHoverSoundPlayer_ShouldReturnNonNullPlayer() {
        Tile tile = new TestTile();

        GameSoundPlayer player = tile.getHoverSoundPlayer();

        assertNotNull(player, "getHoverSoundPlayer() should return a non-null GameSoundPlayer.");
    }

    @Test
    void getClackSoundPlayer_ShouldReturnNonNullPlayer() {
        Tile tile = new TestTile();

        GameSoundPlayer player = tile.getClackSoundPlayer();

        assertNotNull(player, "getClackSoundPlayer() should return a non-null GameSoundPlayer.");
    }

    @Test
    void getFXMLPath_ShouldReturnOverriddenValue() {
        Tile tile = new TestTile();

        assertEquals("/test/path.fxml", tile.getFXMLPath(),
                "getFXMLPath() should return the overridden value.");
    }

    @Test
    void cornerRadius_ShouldBeTwenty() {
        assertEquals(20, Tile.CORNER_RADIUS,
                "CORNER_RADIUS should be 20.");
    }
}