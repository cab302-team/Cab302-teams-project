package com.example.project.models.tiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UpgradeTile}.
 */
class UpgradeTileTests {

    @Test
    void builder_ShouldConstructTileWithCorrectValues() {
        // Use a dummy Runnable that does nothing and doesn't call production audio
        Runnable dummyEffect = () -> {};

        UpgradeTile tile = new UpgradeTile.UpgradeBuilder()
                .name("Speed Boost")
                .description("Increases speed by 20%")
                .imagePath("/images/speed.png")
                .cost(4.99)
                .upgradeEffect(dummyEffect)
                .build();

        assertEquals("Speed Boost", tile.getName());
        assertEquals("Increases speed by 20%", tile.getDescription());
        assertEquals("/images/speed.png", tile.getAbilityImagePath());
        assertEquals(4.99, tile.getCost(), 0.001);
        assertEquals(dummyEffect, tile.getUpgradeEffect());
    }

    @Test
    void getFXMLPath_ShouldReturnCorrectPath() {
        UpgradeTile tile = new UpgradeTile.UpgradeBuilder().build();
        assertEquals("/com/example/project/SingleTiles/upgradeTileView.fxml", tile.getFXMLPath());
    }

    @Test
    void getUpgradeEffect_ShouldRunWithoutThrowing() {
        Runnable dummyEffect = () -> {}; // no audio, no side effects

        UpgradeTile tile = new UpgradeTile.UpgradeBuilder()
                .upgradeEffect(dummyEffect)
                .build();

        assertDoesNotThrow(() -> tile.getUpgradeEffect().run());
    }

    @Test
    void builder_ShouldAllowPartialFields() {
        UpgradeTile tile = new UpgradeTile.UpgradeBuilder()
                .name("Partial")
                .build();

        assertEquals("Partial", tile.getName());
        assertNull(tile.getDescription());
        assertEquals(0.0, tile.getCost());
        assertNull(tile.getAbilityImagePath());
        assertNull(tile.getUpgradeEffect());
    }
}