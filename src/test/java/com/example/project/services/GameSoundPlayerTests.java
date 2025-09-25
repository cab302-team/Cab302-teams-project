package com.example.project.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for {@link GameSoundPlayer}
 */
public class GameSoundPlayerTests
{
    @Test
    void test_GameSoundPlayer_success()
    {
        var validPath = "/com/example/project/Sounds/Clack1.wav";
        var soundPlayer = new GameSoundPlayer(validPath);
    }

    @Test
    void test_GameSoundPlayer_throwsIllegalArgumentException(){
        var path = "invalid-path";
        assertThrows(IllegalArgumentException.class, () -> new GameSoundPlayer(path));
    }

    @Test
    void test_getClip()
    {
        var validPath = "/com/example/project/Sounds/Clack1.wav";
        var soundPlayer = new GameSoundPlayer(validPath);
        var clip = soundPlayer.getClip();

        assertEquals();
    }
}
