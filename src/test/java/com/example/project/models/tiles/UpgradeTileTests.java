package com.example.project.models.tiles;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit test for UpgradeTile model (safe for headless CI).
 */
public class UpgradeTileTests {


    @Test
    void builder_ShouldConstructTileWithCorrectValues() {
        // this is arranging (no sound-producing logic)
        Runnable dummyEffect = () -> {};


        UpgradeTile tile = new UpgradeTile.UpgradeBuilder()
                .name("Double Points")
                .description("Doubles score from word tiles")
                .imagePath("/images/double.png")
                .cost(3.5)
                .upgradeEffect(dummyEffect)
                .build();


        // this is asserting (pure model logic only)
        assertEquals("Double Points", tile.getName());
        assertEquals("Doubles score from word tiles", tile.getDescription());
        assertEquals("/images/double.png", tile.getAbilityImagePath());
        assertEquals(3.5, tile.getCost(), 0.001);
        assertEquals(dummyEffect, tile.getUpgradeEffect());
    }
}