package com.example.project.models.tiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UpgradeTile}.
 */
class UpgradeTileTests {

    @Test
    void constructor_ShouldInitializeFieldsCorrectly() {
        UpgradeTile tile = new UpgradeTile("Speed Boost", "Increases speed by 20%", "/path/image.png");

        assertEquals("Speed Boost", tile.getName());
        assertEquals("Increases speed by 20%", tile.getDescription());
        assertEquals(2.0, tile.getCost());
        assertEquals("/path/image.png", tile.getAbilityImagePath());
    }

    @Test
    void getFXMLPath_ShouldReturnCorrectPath() {
        UpgradeTile tile = new UpgradeTile("Any", "Any", "/any.png");

        assertEquals("/com/example/project/SingleTiles/upgradeTileView.fxml", tile.getFXMLPath());
    }

    @Test
    void add_ShouldNotThrow_WhenCalled() {
        UpgradeTile tile1 = new UpgradeTile("One", "First", "/img1.png");
        UpgradeTile tile2 = new UpgradeTile("Two", "Second", "/img2.png");

        assertDoesNotThrow(() -> tile1.add(tile2));
    }
}