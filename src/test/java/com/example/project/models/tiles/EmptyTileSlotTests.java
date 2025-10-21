package com.example.project.models.tiles;

import com.example.project.testHelpers.MockAudioSystemExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EmptyTileSlotModel}.
 */
@ExtendWith(MockAudioSystemExtension.class)
class EmptyTileSlotTests
{
    @Test
    void setTile() {
        EmptyTileSlotModel slot = new EmptyTileSlotModel();
        LetterTileModel tile = new LetterTileModel('A');

        slot.setTile(tile);

        assertEquals(tile, slot.getTile(),
                "setTile() should store the given LetterTile in the slot.");
    }

    @Test
    void getFXMLPath_ShouldReturnCorrectPath()
    {
        EmptyTileSlotModel slot = new EmptyTileSlotModel();

        assertEquals("/com/example/project/SingleTiles/emptyTileSlot.fxml",
                slot.getFXMLPath(),
                "getFXMLPath() should return the expected FXML path.");
    }
}