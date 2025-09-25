package com.example.project.services;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Tests for {@link GameSoundPlayer}
 */
public class GameSoundPlayerTests
{
    private final String validPath = "/com/example/project/Sounds/Clack1.wav";


    @Test
    void test_GameSoundPlayer_success()
    {
        var soundPlayer = new GameSoundPlayer(validPath);
        assertNotNull(soundPlayer.getClip());
    }

    @Test
    void test_GameSoundPlayerWithVolume_success()
    {
        var validPath = "/com/example/project/Sounds/Clack1.wav";
        var soundPlayer = new GameSoundPlayer(validPath, 0.5f);
        assertNotNull(soundPlayer.getClip());
    }

    @Test
    void test_GameSoundPlayer_throwsIllegalArgumentException(){
        var path = "invalid-path";
        assertThrows(IllegalArgumentException.class, () -> new GameSoundPlayer(path));
    }

    @Test
    void test_GameSoundPlayer_AudioSystemThrowsUnsupportedAudioFileException()
    {
        try (MockedStatic<AudioSystem> mockedAudioSystem = mockStatic(AudioSystem.class))
        {
            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(any(InputStream.class)))
                    .thenThrow(new UnsupportedAudioFileException("Unsupported format"));

            var exception = assertThrows(IllegalArgumentException.class, () -> new GameSoundPlayer(validPath));
            assertEquals("Error loading sound file: Unsupported format", exception.getMessage());
        }
    }
}
