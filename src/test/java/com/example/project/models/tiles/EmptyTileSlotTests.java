package com.example.project.models.tiles;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EmptyTileSlotModel}.
 */
class EmptyTileSlotTests {

    /**
     * the is helper method to access the private "tile" field through the reflection.
     */
    private LetterTile getStoredTile(EmptyTileSlotModel slot) {
        try {
            Field field = EmptyTileSlotModel.class.getDeclaredField("tile");
            field.setAccessible(true);
            return (LetterTile) field.get(slot);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not access EmptyTileSlotModel.tile", e);
        }
    }

    @Test
    void setTile_ShouldStoreTile() {
        EmptyTileSlotModel slot = new EmptyTileSlotModel();
        LetterTile tile = new LetterTile('A');

        slot.setTile(tile);

        assertEquals(tile, getStoredTile(slot),
                "setTile() should store the given LetterTile in the slot.");
    }

    @Test
    void setTile_ShouldAcceptNull() {
        EmptyTileSlotModel slot = new EmptyTileSlotModel();

        slot.setTile(null);

        assertNull(getStoredTile(slot),
                "setTile(null) should clear the slot.");
    }

    @Test
    void getFXMLPath_ShouldReturnCorrectPath() {
        EmptyTileSlotModel slot = new EmptyTileSlotModel();

        assertEquals("/com/example/project/SingleTiles/emptyTileSlot.fxml",
                slot.getFXMLPath(),
                "getFXMLPath() should return the expected FXML path.");
    }
}