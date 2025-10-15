package com.example.project.models.tiles;

import com.example.project.models.gameScreens.LevelModel;

import java.util.Random;

/**
 * upgrade effect methods
 */
public class UpgradeEffects
{
    private static LevelModel levelModel;

    /**
     * Default constructor.
     * @param model model
     */
    public UpgradeEffects(LevelModel model){
        levelModel = model;
    }

    /**
     * Adds +2 to the score multiplier for every identical pair of letters next to each other
     */
    public static void glassesEffect() {
        // for each letter in word, if the current letter matches the previous letter add 2 to the modifier
        for (int i = 1; i < levelModel.getWordRowTilesProperty().size(); i++) {
            Character previousLetter = levelModel.getWordRowTilesProperty().get(i-1).getLetter();
            Character currentLetter = levelModel.getWordRowTilesProperty().get(i).getLetter();
            if (previousLetter.equals(currentLetter)) {
                int newMulti = levelModel.wordMultiProperty().get() + 2;
                levelModel.setWordMulti(newMulti);
            }
        }
    }

    /**
     * Value is doubled for a random letter in the played word
     */
    public static void diceEffect() {
        Random random = new Random();
        int randomNum = random.nextInt(levelModel.getWordRowTilesProperty().size());
        int newScore = levelModel.wordPointsProperty().get() + levelModel.getWordRowTilesProperty().get(randomNum).getValue();
        levelModel.setWordPoints(newScore);
    }

    /**
     * 20% Chance the total word score is doubled
     */
    public static void coinEffect() {
        Random random = new Random();
        double chance = 0.2;
        if (random.nextDouble() < chance) {
            int newScore = levelModel.wordPointsProperty().get() * 2;
            levelModel.setWordPoints(newScore);
        }
    }

}
