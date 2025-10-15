package com.example.project.services.sound;

import com.example.project.services.Logger;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * plays an audio clip as a sound effect for the game.
 */
public class GameSoundPlayer
{
    private Logger logger = new Logger();
    protected final Clip clip;
    private float defaultGain = 0f;

    /**
     * creates a new instance of GameSoundPlayer
     * @param filePath filepath to sound.
     */
    public GameSoundPlayer(String filePath)
    {
        clip = convertFile(filePath);
    }

    protected GameSoundPlayer(String filePath, Logger logger)
    {
        this.logger = logger;
        clip = convertFile(filePath);
    }

    protected GameSoundPlayer(String filePath, Logger logger, float gainAmount)
    {
        this.logger = logger;
        clip = convertFile(filePath);
        defaultGain = gainAmount;
        changeVolume(gainAmount);
    }

    /**
     * creates a new instance of GameSoundPlayer
     * @param filePath filepath to sound.
     * @param gainAmount volume to set the clip at
     */
    public GameSoundPlayer(String filePath, float gainAmount)
    {
        this(filePath);
        defaultGain = gainAmount;
        changeVolume(gainAmount);
    }

    /**
     * Reset clip and then play.
     */
    public void replay() {
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Change the current clips volume. -80f muted 0f is normal volume.
     * @param vol volume.
     */
    private void changeVolume(float vol)
    {
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(vol);
    }

    /**
     * set clip volume to mute.
     */
    public void mute()
    {
        changeVolume(-80f);
    }

    /**
     * Set clip to its default volume.
     */
    public void unMute(){
        changeVolume(defaultGain);
    }

    /**
     * File must be in a specific format for javax.sound.samples library. :(
     * @param filePath file location.
     * @return returns clip in correct format.
     */
    private Clip convertFile(String filePath) {
        Clip convertedClip;

        InputStream resourceStream = GameMusicPlayer.class.getResourceAsStream(filePath);
        if (resourceStream == null)
        {
            var errorMessage = String.format("Sound file not found. No sound file at %s", filePath);
            this.logger.logError(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        try {
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
            var message = "Error loading sound file: " + e.getMessage();
            this.logger.logError(message);
            throw new IllegalArgumentException(message);
        }

        return convertedClip;
    }
}
