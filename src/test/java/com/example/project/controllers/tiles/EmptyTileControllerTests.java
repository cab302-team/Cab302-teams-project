package com.example.project.controllers.tiles;

import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTileModel;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EmptyTileSlotController}.
 * Focus only on controller behavior with observable updates.
 */
public class EmptyTileControllerTests {

    @Test
    void bind_ShouldAssignModelAndRenderInitialTile() {
        var controller = new EmptyTileSlotController();
        controller.root = new StackPane();
        controller.slotForLetterTile = new StackPane();

        var model = new EmptyTileSlotModel();
        var tile = new LetterTileModel('A');
        model.setTile(tile); // set BEFORE bind

        controller.bind(model); // bind should render the tile

        assertEquals(1, controller.slotForLetterTile.getChildren().size(),
                "Tile should be rendered in slot after bind.");
    }

    @Test
    void modelChange_ShouldUpdateViewAutomatically() {
        var controller = new EmptyTileSlotController();
        controller.root = new StackPane();
        controller.slotForLetterTile = new StackPane();

        var model = new EmptyTileSlotModel();
        controller.bind(model);

        assertEquals(0, controller.slotForLetterTile.getChildren().size(),
                "Initially, slot should be empty.");

        // Now change the model tile
        model.setTile(new LetterTileModel('B'));

        assertEquals(1, controller.slotForLetterTile.getChildren().size(),
                "After setting tile, slot should contain one node.");
    }

    @Test
    void modelSetTileToNull_ShouldClearSlot() {
        var controller = new EmptyTileSlotController();
        controller.root = new StackPane();
        controller.slotForLetterTile = new StackPane();

        var model = new EmptyTileSlotModel();
        var tile = new LetterTileModel('C');

        controller.bind(model);
        model.setTile(tile); // insert tile
        assertFalse(controller.slotForLetterTile.getChildren().isEmpty(),
                "Slot should have tile after setting.");

        model.setTile(null); // clear tile
        assertTrue(controller.slotForLetterTile.getChildren().isEmpty(),
                "Slot should be cleared after setting tile to null.");
    }

    @Test
    void getRoot_ShouldReturnRootPane() {
        var controller = new EmptyTileSlotController();
        var root = new StackPane();
        controller.root = root;

        assertSame(root, controller.getRoot());
    }
}