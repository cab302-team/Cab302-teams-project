package com.example.project.services;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * plays an audio clip as a sound effect for the game.
 */
public class GameSoundPlayer
{
    protected final Clip clip;

    private final Logger logger = new Logger();

    private float defaultVolume = 1f;

    /**
     * creates a new instance of GameSoundPlayer
     * @param filePath filepath to sound.
     */
    public GameSoundPlayer(String filePath)
    {
        clip = convertFile(filePath);
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
        defaultVolume = volumeOverride;
        changeVolume(volumeOverride);
    }

    /**
     * Change the current clips volume. 0 is muted 1 is default volume.
     * @param vol volume.
     */
    private void changeVolume(float vol)
    {
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
        volumeControl.setValue(vol);
    }

    /**
     * set clip volume to mute.
     */
    public void mute()
    {
        changeVolume(0f);
    }

    /**
     * Set clip to its default volume.
     */
    public void unMute(){
        changeVolume(defaultVolume);
    }

    /**
     * File must be in a specific format for javax.sound.samples library. :(
     * @param filePath file location.
     * @return returns clip in correct format.
     */
    private Clip convertFile(String filePath) {
        Clip convertedClip;

        try {
            InputStream resourceStream = GameMusicPlayer.class.getResourceAsStream(filePath);
            if (resourceStream == null)
            {
                logger.logMessage("Sound file not found.");
                throw new IllegalArgumentException("Sound file not found.");
            }

            BufferedInputStream bufferedStream = new BufferedInputStream(resourceStream);
            AudioInputStream originalStream = AudioSystem.getAudioInputStream(bufferedStream);

            AudioFormat targetFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    44100.0f, 16, 2, 4, 44100.0f, false
            );

            AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, originalStream);

            convertedClip = AudioSystem.getClip();
            convertedClip.open(convertedStream);

            convertedStream.close();
            originalStream.close();
            bufferedStream.close();
            resourceStream.close();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            logger.logMessage("Error loading sound file: " + e.getMessage());
            throw new IllegalArgumentException("Error loading sound file: " + e.getMessage());
        }

        return convertedClip;
    }
}
