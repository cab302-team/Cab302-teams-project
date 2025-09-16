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
            new UpgradeTile("Grandma's Glasses", "Add +2 to the score multiplier for every identical pair of letters next to each other.", "/com/example/project/upgradeTileImages/GrandmasGlasses_small.png"),
            new UpgradeTile("Lucky Coin", "20% Chance your total word score is doubled." +
                    "alphabetical order.", "/com/example/project/upgradeTileImages/LuckyCoin_small.png"),
            new UpgradeTile("Loaded Dice", "Value is doubled for a random letter in your word.", "/com/example/project/upgradeTileImages/LoadedDice_small.png")
    );

    private static final Random random = new Random();

    /**
     * Gets random upgrade tile.
     * @return returns upgrade tile.
     */
    public static UpgradeTile getRandomUpgradeTile()
    {
        var randomNum = random.nextInt(0, upgradeTiles.size());
        return upgradeTiles.get(randomNum);
    }

    /**
     * Gets the upgrade tile at index.
     * @param index tile to get.
     * @return an upgrade tile model.
     */
    public static UpgradeTile getTile(int index){
        return upgradeTiles.get(index);
    }
}
