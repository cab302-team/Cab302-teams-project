package com.example.project.services;

import com.example.project.Application;
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
        var audioUrl = Application.class.getResource(filepath);
        if (audioUrl == null) {
            logger.logMessage("Sound file not found.");
            throw new IllegalArgumentException("Sound file not found.");
        }

//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
//            loadedClip = AudioSystem.getClip();
//            loadedClip.open(audioInputStream);

        loadedClip = convertFile(filepath);
        return loadedClip;
    }

    /**
     * File must be in a specific format for javax.sound.samples library. :(
     * @param filePath file location.
     * @return returns clip in correct format.
     */
    private Clip convertFile(String filePath) {
        Clip convertedClip = null;

        try {
            InputStream resourceStream = GameMusicPlayer.class.getResourceAsStream(filePath);
            if (resourceStream == null) return null;

            BufferedInputStream bufferedStream = new BufferedInputStream(resourceStream);
            AudioInputStream originalStream = AudioSystem.getAudioInputStream(bufferedStream);
            AudioFormat originalFormat = originalStream.getFormat();

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
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            logger.logMessage("Error loading sound file: " + e.getMessage());
        }

        return convertedClip;
    }
}
