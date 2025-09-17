package com.example.project.services;

import com.example.project.Application;
import javafx.scene.media.AudioClip;

/**
 * plays an audio clip as a sound effect for the game.
 */
public class GameSoundPlayer
{
    private final AudioClip soundClip;

    private final Logger logger = new Logger();

    /**
     * creates a new instance of GameSoundPlayer
     * @param filePath filpath to sound.
     */
    public GameSoundPlayer(String filePath)
    {
        var rawPath = Application.class.getResource(filePath);
        if (rawPath == null)
        {
            logger.logMessage("sound file not found.");
            throw new IllegalArgumentException("sound file not found.");
        }

        String path = rawPath.toExternalForm();
        soundClip = new AudioClip(path);
    }

    /**
     * creates a new instance of GameSoundPlayer
     * @param filePath filpath to sound.
     * @param volumeOverride volume to set the clip at
     */
    public GameSoundPlayer(String filePath, double volumeOverride)
    {
        this(filePath);
        soundClip.setVolume(volumeOverride);
    }

    /**
     * play
     */
    public void play(){
        this.soundClip.play();
    }
}
