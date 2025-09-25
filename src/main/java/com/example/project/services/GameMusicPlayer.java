package com.example.project.services;

import javax.sound.sampled.*;

/**
 * Plays the game music track on repeat.
 */
public class GameMusicPlayer extends GameSoundPlayer
{
    private static final String clipPath = "/com/example/project/Sounds/puzzleMusic_converted.wav";

    /**
     * Create new instance of game music player.
     */
    public GameMusicPlayer()
    {
        super(clipPath);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
