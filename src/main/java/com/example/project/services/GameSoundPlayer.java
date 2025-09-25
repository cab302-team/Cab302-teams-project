package com.example.project.services;

import com.example.project.Application;
import javax.sound.sampled.*;
import java.io.IOException;


/**
 * plays an audio clip as a sound effect for the game.
 */
public class GameSoundPlayer
{
    protected final Clip clip;

    private final Logger logger = new Logger();

    /**
     * creates a new instance of GameSoundPlayer
     * @param filePath filepath to sound.
     */
    public GameSoundPlayer(String filePath)
    {
        clip = loadClip(filePath);
    }

    /**
     * Gets the sound clip this plays.
     * @return clip.
     */
    public Clip getClip(){
        return this.clip;
    }

    /**
     * creates a new instance of GameSoundPlayer
     * @param filePath filepath to sound.
     * @param volumeOverride volume to set the clip at
     */
    public GameSoundPlayer(String filePath, float volumeOverride)
    {
        this(filePath);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volumeOverride);
    }

    private Clip loadClip(String filepath)
    {
        Clip loadedClip = null;

        try
        {
            var audioUrl = Application.class.getResource(filepath);
            if (audioUrl == null) {
                logger.logMessage("Sound file not found.");
                throw new IllegalArgumentException("Sound file not found.");
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
            loadedClip = AudioSystem.getClip();
            loadedClip.open(audioInputStream);
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            logger.logMessage("Error loading sound file: " + e.getMessage());
        }

        return loadedClip;
    }
}
