package com.example.project.models.popups;

import com.example.project.services.GameSoundPlayer;

/**
 * Parent class for popup windows
 */
public abstract class Popup {

    /**
     * gets FXML path
     * @return string path
     */
    public abstract String getFXMLPath();

//    protected final GameSoundPlayer paperSoundPlayer = new GameSoundPlayer("com/example/project/Sounds/PaperFlip.mp3");
//
//    /**
//     * gets paper sound player
//     * @return sound player
//     */
//    public GameSoundPlayer getpaperSoundPlayer() {return paperSoundPlayer;}
}
