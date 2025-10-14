package com.example.project.services.shopItems;

import com.example.project.models.tiles.UpgradeEffects;
import com.example.project.models.tiles.UpgradeTile;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Static class to store all the possible upgrade tiles available in the game.
 */
public class UpgradeTiles {

    private static final List<UpgradeTile> upgradeTiles = Arrays.asList(
            new UpgradeTile.UpgradeBuilder()
                    .name("Grandma's Glasses")
                    .description("Add +2 to the score multiplier for every identical pair of letters next to each other.")
                    .imagePath("/com/example/project/upgradeTileImages/GrandmasGlasses_small.png")
                    .cost(2)
                    .upgradeEffect(UpgradeEffects::glassesEffect)
                    .build(),

            new UpgradeTile.UpgradeBuilder()
                    .name("Loaded Dice")
                    .description("Value is doubled for a random letter in your word.")
                    .imagePath("/com/example/project/upgradeTileImages/LoadedDice_small.png")
                    .cost(2)
                    .upgradeEffect(UpgradeEffects::diceEffect)
                    .build(),

            new UpgradeTile.UpgradeBuilder()
                    .name("Lucky Coin")
                    .description("20% Chance your total word score is doubled.")
                    .imagePath("/com/example/project/upgradeTileImages/LuckyCoin_small.png")
                    .cost(2)
                    .upgradeEffect(UpgradeEffects::coinEffect)
                    .build(),

            new UpgradeTile.UpgradeBuilder()
                    .name("Compact Mirror")
                    .description("Whenever a palindrome is played, double the word’s base score. (A palindrome is a word that is spelt the same backwards as forwards, e.g. LEVEL.)")
                    .imagePath("/com/example/project/upgradeTileImages/CompactMirror.png")
                    .cost(2)
                    .upgradeEffect(UpgradeEffects::mirrorEffect)
                    .build()

    );

    private static final Random random = new Random();

    /**
     * Gets random upgrade tile.
     * @return returns upgrade tile.
     */
    public static UpgradeTile getRandomUpgradeTile() {
        var randomNum = random.nextInt(0, upgradeTiles.size());
        return upgradeTiles.get(randomNum);
    }

    /**
     * Gets the upgrade tile at index.
     * @param index tile to get.
     * @return an upgrade tile model.
     */
    public static UpgradeTile getTile(int index) { return upgradeTiles.get(index); }


}


