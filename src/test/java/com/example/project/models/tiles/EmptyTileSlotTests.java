package com.example.project.models.tiles;

import com.example.project.testHelpers.MockAudioSystemExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EmptyTileSlot}.
 */
@ExtendWith(MockAudioSystemExtension.class)
class EmptyTileSlotTests {

    /**
     * the is helper method to access the private "tile" field through the reflection.
     */
    private LetterTile getStoredTile(EmptyTileSlot slot) {
        try {
            Field field = EmptyTileSlot.class.getDeclaredField("tile");
            field.setAccessible(true);
            return (LetterTile) field.get(slot);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not access EmptyTileSlot.tile", e);
        }
    }

    @Test
    void setTile_ShouldStoreTile() {
        EmptyTileSlot slot = new EmptyTileSlot();
        LetterTile tile = new LetterTile('A');

        slot.setTile(tile);

        assertEquals(tile, getStoredTile(slot),
                "setTile() should store the given LetterTile in the slot.");
    }

    @Test
    void setTile_ShouldAcceptNull() {
        EmptyTileSlot slot = new EmptyTileSlot();

        slot.setTile(null);

        assertNull(getStoredTile(slot),
                "setTile(null) should clear the slot.");
    }

    @Test
    void getFXMLPath_ShouldReturnCorrectPath() {
        EmptyTileSlot slot = new EmptyTileSlot();

        assertEquals("/com/example/project/SingleTiles/emptyTileSlot.fxml",
                slot.getFXMLPath(),
                "getFXMLPath() should return the expected FXML path.");
    }
}