package com.example.project.models.tiles;

import com.example.project.services.sound.GameSoundPlayer;
import com.example.project.testHelpers.MockAudioSystemExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TileModel}.
 */
@ExtendWith(MockAudioSystemExtension.class)
class TileTests {

    /**
     * here is a simple concrete subclass for testing abstract Tile.
     */
    static class TestTile extends TileModel {
        @Override
        public String getFXMLPath() {
            return "/test/path.fxml";
        }
    }

    @Test
    void getHoverSoundPlayer_ShouldReturnNonNullPlayer() {
        TileModel tile = new TestTile();

        GameSoundPlayer player = tile.getHoverSoundPlayer();

        assertNotNull(player, "getHoverSoundPlayer() should return a non-null GameSoundPlayer.");
    }

    @Test
    void getClackSoundPlayer_ShouldReturnNonNullPlayer() {
        TileModel tile = new TestTile();

        GameSoundPlayer player = tile.getClackSoundPlayer();

        assertNotNull(player, "getClackSoundPlayer() should return a non-null GameSoundPlayer.");
    }

    @Test
    void getFXMLPath_ShouldReturnOverriddenValue() {
        TileModel tile = new TestTile();

        assertEquals("/test/path.fxml", tile.getFXMLPath(),
                "getFXMLPath() should return the overridden value.");
    }

    @Test
    void cornerRadius_ShouldBeTwenty() {
        assertEquals(20, TileModel.CORNER_RADIUS,
                "CORNER_RADIUS should be 20.");
    }
}