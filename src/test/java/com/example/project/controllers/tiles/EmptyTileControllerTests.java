package com.example.project.controllers.tiles;

import com.example.project.models.tiles.EmptyTileSlotModel;
import javafx.scene.layout.StackPane;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EmptyTileSlotController}.
 * Focus only on controller behavior, not the model.
 */
public class EmptyTileControllerTests {

    @Test
    void setLetter_ShouldUpdateView() {
        var controller = new EmptyTileSlotController();
        controller.root = new StackPane();
        controller.slotForLetterTile = new StackPane();
        // mock the model instead of actually interacting with emptyslots
        var mockSlot = mock(EmptyTileSlotModel.class);
        controller.bind(mockSlot);

        var mockLetterCon = mock(LetterTileController.class);
        when(mockLetterCon.getRoot()).thenReturn(new StackPane());

        controller.setLetter(mockLetterCon);
        verify(mockSlot).setTile(any());
    }

    @Test
    void bind_ShouldAssignModel() {
        var controller = new EmptyTileSlotController();
        var mockSlot = mock(EmptyTileSlotModel.class);

        controller.bind(mockSlot);

        assertEquals(mockSlot, controller.getModel());
    }

    @Test
    void clearLetterTile_WithBind_ShouldClearView() {
        var controller = new EmptyTileSlotController();
        var slotPane = new StackPane();
        controller.root = new StackPane();
        controller.slotForLetterTile = slotPane;

        var mockSlot = mock(EmptyTileSlotModel.class);
        controller.bind(mockSlot);

        var mockLetterCon = mock(LetterTileController.class);
        when(mockLetterCon.getRoot()).thenReturn(new StackPane());
        controller.setLetter(mockLetterCon);

        // now clear
        controller.setLetter(null);

        assertTrue(slotPane.getChildren().isEmpty(), "Slot pane should be cleared");
    }

    @Test
    void clearLetterTile_WithoutBind_ShouldThrow() {
        var controller = new EmptyTileSlotController();
        controller.root = new StackPane();
        controller.slotForLetterTile = new StackPane();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.setLetter(null));
        assertEquals("model was null. call bind first.", ex.getMessage());
    }

    @Test
    void getRoot_ShouldReturnRoot() {
        var controller = new EmptyTileSlotController();
        var root = new StackPane();
        controller.root = root;

        assertSame(root, controller.getRoot());
    }
}