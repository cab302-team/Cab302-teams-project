package com.example.project.services;

import com.example.project.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * plays a media player music track.
 */
public class GameMusicPlayer
{
    private static MediaPlayer gameMusicPlayer;

    private static final Logger logger = new Logger();

    /**
     * gets the singleton instance.
     * @return the game soundtrack player.
     */
    public static MediaPlayer getInstance(){
        if (gameMusicPlayer == null){
            initialiseLevelMusic();
        }

        return gameMusicPlayer;
    }

    private static void initialiseLevelMusic()
    {
        var rawPath = Application.class.getResource("/com/example/project/Sounds/puzzleMusic.wav");
        if (rawPath == null)
        {
            logger.logMessage("sound file not found.");
            return;
        }

        String path = rawPath.toExternalForm();
        Media media = new Media(path);
        gameMusicPlayer = new MediaPlayer(media);
        gameMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        gameMusicPlayer.play();
    }
}
