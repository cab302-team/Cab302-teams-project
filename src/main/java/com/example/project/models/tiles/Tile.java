package com.example.project.models.tiles;

import com.example.project.services.GameSoundPlayer;

/**
 * Parent tile class for objects in the scene that are tiles. Upgrades and tiles.
 */
public abstract class Tile
{
    /**
     * gets the fxml path.
     * @return string path.
     */
    public abstract String getFXMLPath();

    protected final GameSoundPlayer hoverSoundPlayer = new GameSoundPlayer("/com/example/project/Sounds/HoverEffect.wav", -7f);
    protected final GameSoundPlayer ClackSoundPlayer = new GameSoundPlayer("/com/example/project/Sounds/Clack1.wav");

    /**
     * gets the hover sound player
     * @return sound player
     */
    public GameSoundPlayer getHoverSoundPlayer() {return hoverSoundPlayer;}


    /**
     * gets the sound player that plays the click sound.
     * @return sound player.
     */
    public GameSoundPlayer getClackSoundPlayer() {return ClackSoundPlayer;}

    /**
     * corner radius (rounded corners).
     */
    public static final int CORNER_RADIUS = 20;
}
