package com.example.project.models.popups;

import com.example.project.services.sound.GameSoundPlayer;

/**
 * Parent class for popup windows
 */
public abstract class Popup {

    /**
     * gets FXML path
     * @return string path
     */
    public abstract String getFXMLPath();

    protected final GameSoundPlayer paperSoundPlayer = new GameSoundPlayer("/com/example/project/Sounds/PaperFlip.wav");

    protected final GameSoundPlayer reversePaperSoundPlayer = new GameSoundPlayer("/com/example/project/Sounds/PaperFlipReversed.wav");
    /**
     * gets paper sound player
     * @return sound player
     */
    public GameSoundPlayer getPaperSoundPlayer() {return paperSoundPlayer;}

    /**
     * gets reversed paper sound player
     * @return sound player
     */
    public GameSoundPlayer getReversePaperSoundPlayer() {return reversePaperSoundPlayer;}
}
