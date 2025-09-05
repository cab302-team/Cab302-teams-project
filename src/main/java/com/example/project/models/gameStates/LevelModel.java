package com.example.project.models.gameStates;

import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The model for Level
 */
public class LevelModel
{
    /**
     * The hand size to start with at the start of the round in the player's tile rack.
     */
    private static Integer handSize = 9;

    /**
     * maximum word length in word view.
     */
    private Integer maxWordSize = 8;

    private List<UpgradeTile> upgrades = new ArrayList<>();

    public Integer getMaxWordSize() { return maxWordSize; }

    public Integer getHandSize() { return handSize; }

    public List<LetterTile> letterTiles = new ArrayList<>();

    private static final Random random = new Random();

    private Character getRandomLetter() {
        return (char) ('A' + random.nextInt(26));
    }

    public LevelModel(Integer handSize, Integer maxWordSize)
    {
        this.handSize = handSize;
        this.maxWordSize = maxWordSize;
        generateLetters();
    }

    public void addUpgrade(UpgradeTile tile)
    {
        this.upgrades.add(tile);
    }

    private void generateLetters()
    {
        for (int i = 0; i < handSize; i++)
        {
            var newLetter = new LetterTile(getRandomLetter());
            this.letterTiles.add(newLetter);
        }
    }

    public List<LetterTile> getLetterTiles()
    {
        return List.copyOf(letterTiles);
    }

    /**
     * @return Returns readonly list of upgrades.
     */
    public List<UpgradeTile> getUpgrades()
    {
        return List.copyOf(upgrades);
    }
}
