package com.example.project.services;

import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 */
public class GameMusicPlayerTests
{
    @BeforeEach
    void setUp() {
        resetSingleton();
    }

    private void resetSingleton() {
        try {
            Field field = GameMusicPlayer.class.getDeclaredField("GameMusicPlayer");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
        }
    }
    @Test
    void class_HasCorrectStructure() {
        // Test class structure without creating instances
        assertNotNull(GameMusicPlayer.class);

        try {
            var method = GameMusicPlayer.class.getMethod("getInstance");
            assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("getInstance method should exist");
        }
    }

    @Disabled("Requires actual music file to exist")
    @Test
    void getInstance_WithValidMusicFile_ShouldNotReturnNull() {
        try {
            // This test should pass if the actual music file exists at the expected path
            MediaPlayer player = GameMusicPlayer.getInstance();
            assertNotNull(player, "getInstance() should return MediaPlayer when music file exists.");

        } catch (IllegalStateException e) {
            // Expected - JavaFX not available in unit tests
            assertTrue(e.getMessage().contains("Toolkit not initialized") || e.getMessage().contains("JavaFX"),
                    "Should fail with JavaFX-related error:" + e.getMessage());
        }
    }
}