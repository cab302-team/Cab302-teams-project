package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.EmptyTileSlot;
import com.example.project.models.tiles.LetterTile;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EmptyTileController.
 */
public class EmptyTileControllerTests {

    // 🔹 Stub class goes here
    static class StubLetterTileController extends LetterTileController {
        private final Node root;
        private final LetterTile model;

        StubLetterTileController(LetterTile model, Node root) {
            this.model = model;
            this.root = root;
        }

        @Override
        public LetterTile getModel() {
            return model;
        }

        @Override
        public Node getRoot() {
            return root;
        }
    }

    @Test
    void clearLetterTile_WithoutBind_ShouldThrow() {
        var controller = new EmptyTileController();
        controller.setRoot(new StackPane());
        controller.setSlotForLetterTile(new StackPane());

        RuntimeException ex = assertThrows(RuntimeException.class, controller::clearLetterTile);
        assertEquals("model was null. call bind first.", ex.getMessage());
    }

    @Test
    void clearLetterTile_WithBind_ShouldNotThrow() {
        var controller = new EmptyTileController();
        controller.setRoot(new StackPane());
        controller.setSlotForLetterTile(new StackPane());

        controller.bind(new EmptyTileSlot());

        assertDoesNotThrow(controller::clearLetterTile);
    }

    @Test
    void setLetter_ShouldUpdateModelAndView() {
        var controller = new EmptyTileController();
        controller.setRoot(new StackPane());
        var slotPane = new StackPane();   // keep reference
        controller.setSlotForLetterTile(slotPane);

        var slot = new EmptyTileSlot();
        controller.bind(slot);

        var tile = new LetterTile('A');
        var fakeNode = new StackPane();
        var letterController = new StubLetterTileController(tile, fakeNode);

        controller.setLetter(letterController);

        // Model updated
        assertEquals(tile, slot.getTile());

        // View updated
        assertTrue(slotPane.getChildren().contains(fakeNode));
    }

    @Test
    void bind_ShouldAssignModel() {
        var controller = new EmptyTileController();
        controller.setRoot(new StackPane());
        controller.setSlotForLetterTile(new StackPane());

        var slot = new EmptyTileSlot();
        controller.bind(slot);

        // We can’t access model directly (protected), but safe clear proves it's bound
        assertDoesNotThrow(controller::clearLetterTile);
    }

    @Test
    void getRoot_ShouldReturnRoot() {
        var controller = new EmptyTileController();
        var root = new StackPane();
        controller.setRoot(root);
        controller.setSlotForLetterTile(new StackPane());

        assertSame(root, controller.getRoot());
    }

    @Test
    void getFXMLPath_ShouldReturnCorrectPath() {
        var slot = new EmptyTileSlot();
        assertEquals("/com/example/project/SingleTiles/emptyTileSlot.fxml", slot.getFXMLPath());
    }
}