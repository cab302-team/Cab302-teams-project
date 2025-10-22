package com.example.project.models.tiles;

import com.example.project.models.gameScreens.LevelModel;
import com.example.project.services.Session;

import java.util.Random;

/**
 * upgrade effect methods
 */
public class UpgradeEffects
{
    /**
     * Adds +2 to the score multiplier for every identical pair of letters next to each other
     * @param levelModel model to use.
     */
    public static void glassesEffect(LevelModel levelModel) {
        // for each letter in word, if the current letter matches the previous letter add 2 to the modifier
        for (int i = 1; i < levelModel.getWordWindowTilesProperty().size(); i++) {
            Character previousLetter = levelModel.getWordWindowTilesProperty().get(i-1).getLetter();
            Character currentLetter = levelModel.getWordWindowTilesProperty().get(i).getLetter();
            if (previousLetter.equals(currentLetter)) {
                int newMulti = levelModel.wordMultiProperty().get() + 2;
                levelModel.setWordMulti(newMulti);
            }
        }
    }

    /**
     * Value is doubled for a random letter in the played word
     * @param levelModel model to use.
     */
    public static void diceEffect(LevelModel levelModel) {
        Random random = new Random();
        int randomNum = random.nextInt(levelModel.getWordWindowTilesProperty().size());
        int newScore = levelModel.wordPointsProperty().get() + levelModel.getWordWindowTilesProperty().get(randomNum).getValue();
        levelModel.setWordPoints(newScore);
    }

    /**
     * 20% Chance the total word score is doubled
     * @param levelModel model to use.
     */
    public static void coinEffect(LevelModel levelModel) {
        Random random = new Random();
        double chance = 0.2;
        if (random.nextDouble() < chance) {
            int newScore = levelModel.wordPointsProperty().get() * 2;
            levelModel.setWordPoints(newScore);
        }
    }

    /**
     * If played word is a palindrome, doubles word score.
     * @param levelModel model to use.
     */
    public static void mirrorEffect(LevelModel levelModel) {
        String word = levelModel.getCurrentWord();
        String reversedWord = new StringBuilder(word).reverse().toString();

        if (word.equals(reversedWord)) {
            int newScore = levelModel.wordPointsProperty().get() * 2;
            levelModel.setWordPoints(newScore);
        }
    }

    /**
     * Add +1 to score multiplier for every consecutive letter alphabetical order.
     * @param levelModel model to use.
     */
    public static void braceletEffect(LevelModel levelModel) {

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // for each letter in the word, if the next letter matches the next letter in the alphabet, add 1 to modifier
        for (int i = 0; i < levelModel.getWordWindowTilesProperty().size()-1; i++) {
            Character currentLetter = levelModel.getWordWindowTilesProperty().get(i).getLetter();
            Character nextLetter = levelModel.getWordWindowTilesProperty().get(i+1).getLetter();
            Character nextAlphabeticalLetter = alphabet.charAt(alphabet.indexOf(currentLetter)+1);

            if (nextLetter.equals(nextAlphabeticalLetter)) {
                int newMulti = levelModel.wordMultiProperty().get() + 1;
                levelModel.setWordMulti(newMulti);
            }
        }
    }

    /**
     * adds 5 to the word score for every x in the tile rack.
     * @param levelModel model to use effect on.
     */
    public static void buttonEffect(LevelModel levelModel) {
        for (LetterTileModel tile : levelModel.getTileRackTilesProperty()) {
            if (tile.getLetter().equals('X')) {
                int newScore = levelModel.wordPointsProperty().get() + 5;
                levelModel.setWordPoints(newScore);
            }
        }
    }
}
