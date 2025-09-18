package com.example.project.controllers.tileViewControllers;

import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EmptyTileController}.
 * Focus only on controller behavior, not the model.
 */
public class EmptyTileControllerTests {

    @Test
    void clearLetterTile_WithoutBind_ShouldThrow() {
        var controller = new EmptyTileController();
        controller.setRoot(new StackPane());
        controller.setSlotForLetterTile(new StackPane());

        RuntimeException ex = assertThrows(RuntimeException.class, controller::clearLetterTile);
        assertEquals("model was null. call bind first.", ex.getMessage());
    }
}