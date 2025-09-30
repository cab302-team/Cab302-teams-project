package com.example.project.models.tiles;

import com.example.project.models.gameScreens.LevelModel;
import com.example.project.services.Session;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * upgrade effect methods
 */
public class UpgradeEffects {

    /**
     * Adds +2 to the score multiplier for every identical pair of letters next to each other
     */
    public static void glassesEffect() {
        LevelModel levelModel = Session.getInstance().getLevelModel();

        // for each letter in word, if the current letter matches the previous letter add 2 to the modifier
        for (int i = 1; i <= levelModel.getWordRowTilesProperty().size(); i++) {
            Character previousLetter = levelModel.getWordRowTilesProperty().get(i-1).getLetter();
            Character currentLetter = levelModel.getWordRowTilesProperty().get(i).getLetter();
            if (previousLetter == currentLetter) {
                int newMulti = levelModel.wordMultiProperty().get() + 2;
                levelModel.setWordMulti(newMulti);
            }
        }
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
