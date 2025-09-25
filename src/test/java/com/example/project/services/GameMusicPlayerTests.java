package com.example.project.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 */
public class GameMusicPlayerTests
{
    @Test
    void test_createdGameMusic_success()
    {
        var musicPlayer = new GameMusicPlayer();
        assertNotNull(musicPlayer.getClip());
    }
}