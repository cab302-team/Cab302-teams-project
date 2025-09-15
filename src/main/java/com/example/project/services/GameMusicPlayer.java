package com.example.project.services;

import com.example.project.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameMusicPlayer
{
    private static MediaPlayer gameMusicPlayer;

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
            return;
        }

        String path = rawPath.toExternalForm();
        Media media = new Media(path);
        gameMusicPlayer = new MediaPlayer(media);
        gameMusicPlayer.play();
        gameMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public static void pause(){
        gameMusicPlayer.pause();
    }

    public static void resume(){
        gameMusicPlayer.play();
    }
}
