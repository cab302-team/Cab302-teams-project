package com.example.project.services.shopItems;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for {@link UpgradeTiles}
 */
class UpgradeTilesTest
{
    @Test
    void getRandomUpgradeTile()
    {
        var rand = new Random();
        rand.setSeed(222);
        var expected = rand.nextInt(0, 6);
        UpgradeTiles.random.setSeed(222);

        var expectedTile = UpgradeTiles.getTile(expected);
        assertEquals(expectedTile, UpgradeTiles.getRandomUpgradeTile());
    }

    @Test
    void getUpgradeByName_exists(){
        var tile = UpgradeTiles.getUpgradeByName("Lost Button");
        assertNotNull(tile);
        assertEquals("Lost Button", tile.getName());
    }

    @Test
    void getUpgradeByName_DoesntExist(){
        var tile = UpgradeTiles.getUpgradeByName("asdf; ;alskdj asd;kljas");
        assertNull(tile);
    }
}