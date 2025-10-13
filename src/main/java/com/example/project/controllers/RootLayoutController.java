package com.example.project.controllers;

import com.example.project.services.sound.GameMusicPlayer;
import com.example.project.services.SVGIcons;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


/**
 * Root layout that remains the root scene of the application to switch scenes within.
 */
public class RootLayoutController
{
    @FXML
    private BorderPane rootPane;

    @FXML
    private StackPane contentPane;

    @FXML
    private StackPane headerPane;

    @FXML
    Button soundToggle;

    private final GameMusicPlayer musicPlayer = new GameMusicPlayer();

    private boolean isPlaying;

    /**
     * @param page Set page content to a game scene.
     */
    public void setContent(Parent page) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(page);
    }

    @FXML
    void initialize()
    {
        soundToggle.setGraphic(SVGIcons.getCogIcon());
        musicPlayer.playGameMusicLoop();
        this.isPlaying = true;
    }

    @FXML
    private void toggleSound()
    {
        if (isPlaying){
            musicPlayer.mute();
        }
        else{
            musicPlayer.unMute();
        }

        isPlaying = !isPlaying;
    }
}
