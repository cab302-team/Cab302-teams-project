package com.example.project.controllers;

import com.example.project.services.GameMusicPlayer;
import com.example.project.services.SVGIcons;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;


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
        GameMusicPlayer.getInstance().play();
        isPlaying = true;
    }

    @FXML
    private void toggleSound()
    {
        if (isPlaying){
            GameMusicPlayer.getInstance().pause();
        }
        else{
            GameMusicPlayer.getInstance().play();
        }

        isPlaying = !isPlaying;
    }
}
