package com.example.project.models.gameScreens;


import com.example.project.services.GameSoundPlayer;

import java.util.List;

/**
 * plays a note from the F major scale ascending in pitch each time for a nice score combo sound.
 */
public class wordTileScoreChimeAscending
{
    private final GameSoundPlayer F_4 = new GameSoundPlayer("/com/example/project/Sounds/scoreSounds/F_4.mp3");
    private final GameSoundPlayer G_4 = new GameSoundPlayer("/com/example/project/Sounds/scoreSounds/G_4.mp3");
    private final GameSoundPlayer A_4 = new GameSoundPlayer("/com/example/project/Sounds/scoreSounds/A_4.mp3");
    private final GameSoundPlayer Bb_4 = new GameSoundPlayer("/com/example/project/Sounds/scoreSounds/Bb_4.mp3");
    private final GameSoundPlayer C_4 = new GameSoundPlayer("/com/example/project/Sounds/scoreSounds/C_4.mp3");
    private final GameSoundPlayer D_4 = new GameSoundPlayer("/com/example/project/Sounds/scoreSounds/D_5.mp3");
    private final GameSoundPlayer E_4 = new GameSoundPlayer("/com/example/project/Sounds/scoreSounds/E_5.mp3");
    private final GameSoundPlayer F_5 = new GameSoundPlayer("/com/example/project/Sounds/scoreSounds/F_5.mp3");
    private final GameSoundPlayer G_5 = new GameSoundPlayer("/com/example/project/Sounds/scoreSounds/G_5.mp3");

    private final List<GameSoundPlayer> theCMajorScale = List.of(F_4, G_4, A_4, Bb_4, C_4, D_4, E_4, F_5, G_5);

    private int currentNote = 0;

    /**
     * resets back to 0.
     */
    public void reset(){
        currentNote = 0;
    }

    /**
     * plays the note then increments the int index.
     */
    public void playNextNote()
    {
        if (theCMajorScale.size() == currentNote){
            reset();
        }

        theCMajorScale.get(currentNote).getClip().start();
        currentNote++;
    }
}
