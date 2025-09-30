package com.example.project.models.tiles;

import com.example.project.models.gameScreens.LevelModel;
import com.example.project.services.Session;

/**
 * upgrade effect methods
 */
public class UpgradeEffects {

    /**
     * Adds +2 to the score multiplier for every identical pair of letters next to each other
     */
    public static void glassesEffect() {
        LevelModel levelModel = Session.getInstance().getLevelModel();
        // TODO: levelModel.setWordMulti(levelModel.getWordMulti() + 2);
    }

    /**
     * Value is doubled for a random letter in the played word
     */
    public static void diceEffect() {
        // TODO: add body
    }

    /**
     * 20% Chance the total word score is doubled
     */
    public static void coinEffect() {
        // TODO: add body
    }

}
