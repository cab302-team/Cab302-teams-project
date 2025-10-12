package com.example.project.models.tiles;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UpgradeTile}.
 */
class UpgradeTileTests {

    @Test
    void builder_ShouldConstructTileCorrectly() {
        Runnable dummyEffect = () -> System.out.println("Upgraded!");

        UpgradeTile tile = new UpgradeTile.UpgradeBuilder()
                .name("Speed Boost")
                .description("Increases speed by 20%")
                .imagePath("/path/image.png")
                .cost(3.5)
                .upgradeEffect(dummyEffect)
                .build();

        assertEquals("Speed Boost", tile.getName());
        assertEquals("Increases speed by 20%", tile.getDescription());
        assertEquals(3.5, tile.getCost());
        assertEquals("/path/image.png", tile.getAbilityImagePath());
        assertSame(dummyEffect, tile.getUpgradeEffect());
    }

    @Test
    void getFXMLPath_ShouldReturnExpectedPath() {
        UpgradeTile tile = new UpgradeTile.UpgradeBuilder()
                .name("Dummy")
                .description("Test")
                .imagePath("/img.png")
                .cost(1.0)
                .upgradeEffect(() -> {})
                .build();

        assertEquals("/com/example/project/SingleTiles/upgradeTileView.fxml", tile.getFXMLPath());
    }

    @Test
    void upgradeEffect_ShouldExecuteWithoutError() {
        AtomicBoolean executed = new AtomicBoolean(false);

        Runnable effect = () -> executed.set(true);

        UpgradeTile tile = new UpgradeTile.UpgradeBuilder()
                .name("Effect Test")
                .description("Run effect")
                .imagePath("/img.png")
                .cost(1.0)
                .upgradeEffect(effect)
                .build();

        assertDoesNotThrow(() -> tile.getUpgradeEffect().run());
        assertTrue(executed.get(), "Upgrade effect should have executed and set flag to true");
    }
}