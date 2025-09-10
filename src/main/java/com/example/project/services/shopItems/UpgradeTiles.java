package com.example.project.services.shopItems;

import com.example.project.models.tiles.UpgradeTile;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Static class to store all the possible upgrade tiles available in the game.
 */
public class UpgradeTiles
{

    private static final List<UpgradeTile> upgradeTiles = Arrays.asList(
            new UpgradeTile("Simplicity", "Add +1 to multiplier for every empty letter slot in the word.", "/com" +
                    "/example/project/upgradeTileImages/Monk_Free/Monk_29.png"),
            new UpgradeTile("Chain Reaction", "+1 score multiplier for every consecutive sequence of letters in " +
                    "alphabetical order.", "/com/example/project/upgradeTileImages/Engineer_Free/Engineer_17.png"),
            new UpgradeTile("Silent Killer", "Words ending with a silent “E” gain +5 bonus points.", "/com/example/project/upgradeTileImages/Hunter_Free/Hunter_15.png")
    );

    private static final Random random = new Random();

    /**
     * Gets random upgrade tile.
     * @return returns upgrade tile.
     */
    public static UpgradeTile getRandomUpgradeTile()
    {
        var randmNumber = random.nextInt(0, upgradeTiles.size());
        return upgradeTiles.get(randmNumber);
    }

    public static UpgradeTile getTile(int index){
        return upgradeTiles.get(index);
    }
}
